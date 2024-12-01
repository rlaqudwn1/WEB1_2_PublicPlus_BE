package backend.dev.chatroom.service;

import backend.dev.chatroom.dto.request.MessageRequestDTO;
import backend.dev.chatroom.dto.response.MessageResponseDTO;
import backend.dev.chatroom.entity.ChatRoom;
import backend.dev.chatroom.entity.Message;
import backend.dev.chatroom.entity.ChatParticipant;
import backend.dev.chatroom.exception.UnauthorizedAccessException;
import backend.dev.chatroom.repository.ChatRoomRepository;
import backend.dev.chatroom.repository.MessageRepository;
import backend.dev.chatroom.repository.ChatParticipantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;

    public MessageService(MessageRepository messageRepository,
                          ChatRoomRepository chatRoomRepository,
                          ChatParticipantRepository chatParticipantRepository) {
        this.messageRepository = messageRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.chatParticipantRepository = chatParticipantRepository;
    }

    // 메세지 전송
    @Transactional
    public MessageResponseDTO sendMessage(MessageRequestDTO requestDTO) {
        // 1. 채팅방 확인
        ChatRoom chatRoom = chatRoomRepository.findById(requestDTO.getChatRoomId())
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));

        // 2. 참가자 확인
        ChatParticipant chatParticipant = chatParticipantRepository.findById(requestDTO.getParticipantId())
                .orElseThrow(() -> new IllegalArgumentException("참가자를 찾을 수 없습니다."));

        if (!chatParticipant.getChatRoom().equals(chatRoom)) {
            throw new IllegalArgumentException("참가자가 해당 채팅방에 포함되어 있지 않습니다.");
        }

        // 3. 메세지 생성 및 저장
        Message message = new Message();
        message.setChatRoom(chatRoom);
        message.setParticipant(chatParticipant);
        message.setContent(requestDTO.getContent());
        message.setSentAt(LocalDateTime.now());
        Message savedMessage = messageRepository.save(message);

        // 4. DTO로 변환하여 반환
        return MessageResponseDTO.fromEntity(savedMessage);
    }

    // 특정 채팅방의 메세지 조회
    @Transactional(readOnly = true)
    public List<MessageResponseDTO> getMessagesByChatRoom(Long chatRoomId) {
        return messageRepository.findByChatRoom_ChatRoomId(chatRoomId).stream()
                .map(MessageResponseDTO::fromEntity) // 엔티티 -> DTO 변환
                .collect(Collectors.toList());
    }

    // 특정 채팅방의 메세지 삭제
    public void deleteMessage(Long chatRoomId, Long messageId, String requesterId) {
        // 1. 채팅방 및 메세지 조회
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("메세지를 찾을 수 없습니다."));

        // 2. 요청자 권한 확인
        ChatParticipant requester = chatParticipantRepository.findByChatRoomAndUserId(chatRoom, requesterId)
                .orElseThrow(() -> new IllegalArgumentException("요청자를 해당 채팅방에서 찾을 수 없습니다."));

        if (!message.getParticipant().getUser().getUserId().equals(requesterId) &&
                !requester.isHost()) {
            throw new UnauthorizedAccessException("메시지 작성자 또는 방장만 메세지를 삭제할 수 있습니다.");
        }

        // 3. 메세지 삭제
        messageRepository.delete(message);
    }
}