package backend.dev.chatroom.service;

import backend.dev.chatroom.dto.request.ChatRoomRequestDTO;
import backend.dev.chatroom.dto.response.ChatRoomResponseDTO;
import backend.dev.chatroom.entity.ChatParticipantRole;
import backend.dev.chatroom.entity.ChatRoom;
import backend.dev.chatroom.entity.ChatRoomType;
import backend.dev.chatroom.entity.ChatParticipant;
import backend.dev.chatroom.exception.ChatRoomNotFoundException;
import backend.dev.chatroom.exception.UnauthorizedAccessException;
import backend.dev.chatroom.repository.ChatRoomRepository;
import backend.dev.chatroom.repository.ChatParticipantRepository;
import backend.dev.user.entity.User;
import backend.dev.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
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
        log.info("Id " + SecurityContextHolder.getContext().getAuthentication().getName());
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Transactional
    public ChatRoomResponseDTO createChatRoom(ChatRoomRequestDTO requestDTO) {
        String userId = getCurrentUserId();
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("ID가 " + userId + "인 사용자를 찾을 수 없습니다."));

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setName(requestDTO.getChatRoomName());
        chatRoom.setType(ChatRoomType.valueOf(requestDTO.getChatRoomType().toUpperCase()));
        chatRoom.setCreatedAt(LocalDateTime.now());
        chatRoom.setUpdatedAt(LocalDateTime.now());

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
    public ChatRoomResponseDTO createOrFindPrivateChatRoom(String otherUserId) {
        String currentUserId = getCurrentUserId();
        if (currentUserId == null || currentUserId.isEmpty()) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        // 현재 사용자와 상대방 사용자 조회
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new IllegalArgumentException("ID가 " + currentUserId + "인 사용자를 찾을 수 없습니다."));
        User otherUser = userRepository.findById(otherUserId)
                .orElseThrow(() -> new IllegalArgumentException("ID가 " + otherUserId + "인 사용자를 찾을 수 없습니다."));

        // 기존 1:1 채팅방이 있는지 확인
        Optional<ChatRoom> existingRoom = chatRoomRepository.findByBothParticipants(currentUser, otherUser);
        if (existingRoom.isPresent()) {
            return ChatRoomResponseDTO.fromEntity(existingRoom.get());
        }

        // 새로운 1:1 채팅방 생성
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setName(currentUser.getNickname() + " & " + otherUser.getNickname());
        chatRoom.setType(ChatRoomType.PRIVATE);
        chatRoom.setCreatedAt(LocalDateTime.now());
        chatRoom.setUpdatedAt(LocalDateTime.now());
        chatRoomRepository.save(chatRoom);

        // 현재 사용자를 HOST로 설정
        ChatParticipant currentParticipant = ChatParticipant.builder()
                .chatRoom(chatRoom)
                .user(currentUser)
                .role(ChatParticipantRole.HOST)
                .build();

        // 상대방을 MEMBER로 설정
        ChatParticipant otherParticipant = ChatParticipant.builder()
                .chatRoom(chatRoom)
                .user(otherUser)
                .role(ChatParticipantRole.MEMBER)
                .build();

        chatParticipantRepository.save(currentParticipant);
        chatParticipantRepository.save(otherParticipant);

        return ChatRoomResponseDTO.fromEntity(chatRoom);
    }

    @Transactional
    public void joinChatRoom(Long chatRoomId) {
        String userId = getCurrentUserId();
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("ID로 사용자를 찾을 수 없습니다: " + userId));

        if (chatParticipantRepository.existsByChatRoomAndUser(chatRoom, user)) {
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
    public void kickUser(Long chatRoomId, String targetUserId) {
        String requesterUserId = getCurrentUserId();
        if (requesterUserId == null || requesterUserId.isEmpty()) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));

        ChatParticipant host = chatParticipantRepository.findByChatRoomAndUserId(chatRoom, requesterUserId)
                .orElseThrow(() -> new IllegalArgumentException("요청자가 채팅방 참가자가 아닙니다."));

        if (!host.isHost()) {
            throw new UnauthorizedAccessException("호스트만 강퇴할 수 있습니다.");
        }

        ChatParticipant target = chatParticipantRepository.findByChatRoomAndUserId(chatRoom, targetUserId)
                .orElseThrow(() -> new IllegalArgumentException("대상을 찾을 수 없습니다."));

        chatParticipantRepository.delete(target);
    }

    @Transactional
    public void deleteGroupChatRoom(Long chatRoomId) {
        String requesterUserId = getCurrentUserId();
        if (requesterUserId == null || requesterUserId.isEmpty()) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ChatRoomNotFoundException("ID가 " + chatRoomId + "인 채팅방을 찾을 수 없습니다."));

        ChatParticipant host = chatParticipantRepository.findByChatRoomAndUserId(chatRoom, requesterUserId)
                .orElseThrow(() -> new UnauthorizedAccessException("호스트가 아니므로 채팅방을 삭제할 수 없습니다."));

        if (!host.isHost()) {
            throw new UnauthorizedAccessException("호스트만 채팅방을 삭제할 수 있습니다.");
        }

        chatParticipantRepository.deleteAllByChatRoom(chatRoom); // 참가자 삭제
        chatRoomRepository.delete(chatRoom); // 채팅방 삭제
    }

    @Transactional
    public void deletePrivateChatRoom(Long chatRoomId) {
        String userId = getCurrentUserId();
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ChatRoomNotFoundException("ID가 " + chatRoomId + "인 채팅방을 찾을 수 없습니다."));

        if (!chatRoom.getType().equals(ChatRoomType.PRIVATE)) {
            throw new IllegalArgumentException("이 채팅방은 1:1 채팅방이 아닙니다.");
        }

        ChatParticipant participant = chatParticipantRepository.findByChatRoomAndUserId(chatRoom, userId)
                .orElseThrow(() -> new UnauthorizedAccessException("이 채팅방을 삭제할 권한이 없습니다."));

        chatParticipantRepository.deleteAllByChatRoom(chatRoom); // 참가자 삭제
        chatRoomRepository.delete(chatRoom); // 채팅방 삭제
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
                .map(ChatRoomResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
