package backend.dev.chatroom.service;

import backend.dev.chatroom.dto.request.MessageRequestDTO;
import backend.dev.chatroom.dto.response.MessageResponseDTO;
import backend.dev.chatroom.entity.ChatRoom;
import backend.dev.chatroom.entity.Message;
import backend.dev.chatroom.entity.ChatParticipant;
import backend.dev.chatroom.exception.ChatRoomNotFoundException;
import backend.dev.chatroom.exception.ParticipantNotFoundException;
import backend.dev.chatroom.exception.InvalidChatRoomException;
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

    @Transactional
    public MessageResponseDTO sendMessage(MessageRequestDTO requestDTO) {
        ChatRoom chatRoom = chatRoomRepository.findById(requestDTO.getChatRoomId())
                .orElseThrow(() -> new ChatRoomNotFoundException("채팅방을 찾을 수 없습니다."));

        ChatParticipant participant = chatParticipantRepository.findById(requestDTO.getParticipantId())
                .orElseThrow(() -> new ParticipantNotFoundException("참가자를 찾을 수 없습니다."));

        Message message = new Message();
        message.setChatRoom(chatRoom);
        message.setParticipant(participant);
        message.setContent(requestDTO.getContent());
        message.setSentAt(LocalDateTime.now());

        messageRepository.save(message);

        return MessageResponseDTO.fromEntity(message);
    }

    private ChatRoom findChatRoom(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ChatRoomNotFoundException("채팅방을 찾을 수 없습니다."));
    }

    private ChatParticipant findParticipant(Long participantId, ChatRoom chatRoom) {
        ChatParticipant participant = chatParticipantRepository.findById(participantId)
                .orElseThrow(() -> new ParticipantNotFoundException("참가자를 찾을 수 없습니다."));
        if (!participant.getChatRoom().equals(chatRoom)) {
            throw new InvalidChatRoomException("참가자가 해당 채팅방에 포함되어 있지 않습니다.");
        }
        return participant;
    }

    private Message createMessage(ChatRoom chatRoom, ChatParticipant participant, String content) {
        Message message = new Message();
        message.setChatRoom(chatRoom);
        message.setParticipant(participant);
        message.setContent(content);
        message.setSentAt(LocalDateTime.now());
        return messageRepository.save(message);
    }

    @Transactional(readOnly = true)
    public List<MessageResponseDTO> getMessagesByChatRoom(Long chatRoomId) {
        return messageRepository.findByChatRoom_ChatRoomId(chatRoomId).stream()
                .map(MessageResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteMessage(Long chatRoomId, Long messageId, String requesterId) {
        ChatRoom chatRoom = findChatRoom(chatRoomId);
        Message message = findMessage(messageId);
        ChatParticipant requester = findRequester(chatRoom, requesterId);
        validateRequesterPermission(requester, message);
        messageRepository.delete(message);
    }

    private Message findMessage(Long messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("메시지를 찾을 수 없습니다."));
    }

    private ChatParticipant findRequester(ChatRoom chatRoom, String requesterId) {
        return chatParticipantRepository.findByChatRoomAndUserEmail(chatRoom, requesterId)
                .orElseThrow(() -> new ParticipantNotFoundException("요청자를 해당 채팅방에서 찾을 수 없습니다."));
    }

    private void validateRequesterPermission(ChatParticipant requester, Message message) {
        System.out.println("Requester isHost: " + requester.isHost());
        System.out.println("Requester User ID: " + (requester.getUser() != null ? requester.getUser().getUserId() : "null"));
        System.out.println("Message User ID: " + (message.getParticipant().getUser() != null ? message.getParticipant().getUser().getUserId() : "null"));

        if (requester.getUser() != null && message.getParticipant().getUser() != null) {
            if (!message.getParticipant().getUser().getUserId().equals(requester.getUser().getUserId())
                    && !requester.isHost()) {
                throw new UnauthorizedAccessException("메시지 작성자 또는 방장만 메시지를 삭제할 수 있습니다.");
            }
        } else {
            throw new UnauthorizedAccessException("사용자 정보가 누락되었습니다.");
        }
    }
}
