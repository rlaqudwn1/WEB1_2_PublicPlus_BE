package backend.dev.chatroom.service;

import backend.dev.chatroom.dto.request.ChatRoomRequestDTO;
import backend.dev.chatroom.dto.response.ChatRoomResponseDTO;
import backend.dev.chatroom.entity.*;
import backend.dev.chatroom.exception.ChatRoomNotFoundException;
import backend.dev.chatroom.exception.UnauthorizedAccessException;
import backend.dev.chatroom.repository.ChatRoomRepository;
import backend.dev.chatroom.repository.ChatParticipantRepository;
import backend.dev.user.entity.User;
import backend.dev.user.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final UserRepository userRepository;

    public ChatRoomService(ChatRoomRepository chatRoomRepository,
                           ChatParticipantRepository chatParticipantRepository,
                           UserRepository userRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatParticipantRepository = chatParticipantRepository;
        this.userRepository = userRepository;
    }

    // 현재 로그인된 사용자의 user_id 가져오기
    private String getCurrentUserId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Transactional
    public ChatRoomResponseDTO createChatRoom(ChatRoomRequestDTO requestDTO) {
        String userId = getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("ID가 " + userId + "인 사용자를 찾을 수 없습니다."));

        // 최대 인원 확인 및 검증
        if (requestDTO.getMaxParticipants() < 1) {
            throw new IllegalArgumentException("최소 1명 이상의 참여자가 필요합니다.");
        }

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setName(requestDTO.getChatRoomName());
        chatRoom.setType(ChatRoomType.valueOf(requestDTO.getChatRoomType().toUpperCase()));
        chatRoom.setMaxParticipants(requestDTO.getMaxParticipants());
        chatRoom.setCreatedAt(LocalDateTime.now());
        chatRoom.setUpdatedAt(LocalDateTime.now());
        chatRoom.setStartTime(LocalDateTime.now());

        chatRoomRepository.save(chatRoom);

        ChatParticipant hostParticipant = ChatParticipant.builder()
                .chatRoom(chatRoom)
                .user(user)
                .role(ChatParticipantRole.HOST)
                .host(true)
                .build();

        chatParticipantRepository.save(hostParticipant);

        return ChatRoomResponseDTO.fromEntity(chatRoom);
    }

    @Transactional
    public void joinChatRoom(Long chatRoomId) {
        String userId = getCurrentUserId();

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));


        int currentParticipants = chatRoom.getChatParticipants().size();
        if (currentParticipants >= chatRoom.getMaxParticipants()) {
            throw new IllegalStateException("채팅방의 최대 참여 인원을 초과했습니다.");
        }

        if (chatParticipantRepository.existsByChatRoomAndUser_Email(chatRoom, user.getEmail())) {
            throw new IllegalStateException("사용자가 이미 채팅방에 참가 중입니다.");
        }

        ChatParticipant chatParticipant = ChatParticipant.builder()
                .chatRoom(chatRoom)
                .user(user)
                .role(ChatParticipantRole.MEMBER)
                .host(false)
                .build();

        chatParticipantRepository.save(chatParticipant);
    }

    @Transactional
    public void leaveChatRoom(Long chatRoomId) {
        String userId = getCurrentUserId();
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ChatRoomNotFoundException("ID가 " + chatRoomId + "인 채팅방을 찾을 수 없습니다."));

        // 현재 사용자 가져오기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));

        // 현재 사용자가 채팅방 참가자인지 확인
        ChatParticipant participant = chatParticipantRepository.findByChatRoomAndUserEmail(chatRoom, user.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("사용자가 채팅방에 참가하고 있지 않습니다."));

        if (participant.isHost()) {
            // 방장이 나갈 경우 채팅방 삭제
            chatParticipantRepository.deleteAllByChatRoom(chatRoom);
            chatRoomRepository.delete(chatRoom);
        } else {
            // 참가자가 나갈 경우 참가자 삭제
            chatParticipantRepository.delete(participant);
        }
    }

    @Transactional(readOnly = true)
    public ChatRoomResponseDTO getChatRoomById(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ChatRoomNotFoundException("ID가 " + chatRoomId + "인 채팅방을 찾을 수 없습니다."));
        return ChatRoomResponseDTO.fromEntity(chatRoom);
    }

    @Transactional(readOnly = true)
    public List<ChatRoomResponseDTO> getAllChatRooms() {
        return chatRoomRepository.findAll()
                .stream()
                .map(chatRoom -> {
                    if (chatRoom == null) {
                        throw new IllegalArgumentException("ChatRoom 엔티티가 null입니다.");
                    }
                    return ChatRoomResponseDTO.fromEntity(chatRoom);
                })
                .collect(Collectors.toList());
    }
}
