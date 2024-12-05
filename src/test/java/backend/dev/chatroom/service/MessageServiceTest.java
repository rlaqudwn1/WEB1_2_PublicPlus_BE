package backend.dev.chatroom.service;

import backend.dev.chatroom.dto.request.MessageRequestDTO;
import backend.dev.chatroom.dto.response.MessageResponseDTO;
import backend.dev.chatroom.entity.ChatRoom;
import backend.dev.chatroom.entity.ChatParticipant;
import backend.dev.chatroom.entity.Message;
import backend.dev.user.entity.User;
import backend.dev.chatroom.exception.ChatRoomNotFoundException;
import backend.dev.chatroom.exception.ParticipantNotFoundException;
import backend.dev.chatroom.exception.UnauthorizedAccessException;
import backend.dev.chatroom.repository.ChatParticipantRepository;
import backend.dev.chatroom.repository.ChatRoomRepository;
import backend.dev.chatroom.repository.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
class MessageServiceTest {

    @Autowired
    private MessageService messageService;

    @MockBean
    private MessageRepository messageRepository;

    @MockBean
    private ChatRoomRepository chatRoomRepository;

    @MockBean
    private ChatParticipantRepository chatParticipantRepository;

    private ChatRoom chatRoom;
    private ChatParticipant chatParticipant;
    private Message message;
    private User user;

    @BeforeEach
    void setUp() {
        chatRoom = new ChatRoom();
        chatRoom.setChatRoomId(101L);

        user = new User();
        user.setUserId("testUser");
        user.setNickname("Test User");

        chatParticipant = new ChatParticipant();
        chatParticipant.setParticipantId(202L);
        chatParticipant.setChatRoom(chatRoom);
        chatParticipant.setHost(true);
        chatParticipant.setUser(user); // User 객체 설정

        message = new Message();
        message.setMessageId(1L);
        message.setChatRoom(chatRoom);
        message.setParticipant(chatParticipant);
        message.setContent("Hello, world!");
        message.setSentAt(LocalDateTime.now());
    }

    @Test
    void sendMessage_ShouldReturnMessageResponseDTO_WhenValidRequest() {
        // Arrange
        MessageRequestDTO requestDTO = new MessageRequestDTO();
        requestDTO.setChatRoomId(chatRoom.getChatRoomId());
        requestDTO.setParticipantId(chatParticipant.getParticipantId());
        requestDTO.setContent("Test message");

        when(chatRoomRepository.findById(requestDTO.getChatRoomId())).thenReturn(Optional.of(chatRoom));
        when(chatParticipantRepository.findById(requestDTO.getParticipantId())).thenReturn(Optional.of(chatParticipant));

        // Mock된 messageRepository.save가 올바른 Message 객체를 반환하도록 설정
        when(messageRepository.save(any(Message.class))).thenAnswer(invocation -> {
            Message savedMessage = invocation.getArgument(0, Message.class);
            savedMessage.setMessageId(1L); // 저장된 메시지의 ID를 설정
            return savedMessage;
        });

        // Act
        MessageResponseDTO responseDTO = messageService.sendMessage(requestDTO);

        // Assert
        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.getMessageId()).isEqualTo(1L);
        assertThat(responseDTO.getContent()).isEqualTo("Test message");
        verify(messageRepository, times(1)).save(any(Message.class));
    }

    @Test
    void getMessagesByChatRoom_ShouldReturnMessageList_WhenMessagesExist() {
        // Arrange
        when(messageRepository.findByChatRoom_ChatRoomId(chatRoom.getChatRoomId()))
                .thenReturn(List.of(message));

        // Act
        List<MessageResponseDTO> messages = messageService.getMessagesByChatRoom(chatRoom.getChatRoomId());

        // Assert
        assertThat(messages).hasSize(1);
        assertThat(messages.get(0).getContent()).isEqualTo(message.getContent());
        verify(messageRepository, times(1)).findByChatRoom_ChatRoomId(chatRoom.getChatRoomId());
    }

    @Test
    void deleteMessage_ShouldThrowUnauthorizedAccessException_WhenRequesterIsNotAuthorized() {
        // Arrange
        String unauthorizedUserId = "unauthorizedUser"; // 권한 없는 사용자

        // Requester User 설정
        User requesterUser = new User();
        requesterUser.setUserId(unauthorizedUserId);
        ChatParticipant requesterParticipant = new ChatParticipant();
        requesterParticipant.setUser(requesterUser);
        requesterParticipant.setHost(false); // 호스트 아님

        // Message User 설정
        User messageOwner = new User();
        messageOwner.setUserId("messageOwner");
        ChatParticipant messageParticipant = new ChatParticipant();
        messageParticipant.setUser(messageOwner);
        message.setParticipant(messageParticipant);

        // Mock Repository 설정
        when(chatRoomRepository.findById(chatRoom.getChatRoomId())).thenReturn(Optional.of(chatRoom));
        when(messageRepository.findById(message.getMessageId())).thenReturn(Optional.of(message));
        when(chatParticipantRepository.findByChatRoomAndUserEmail(chatRoom, unauthorizedUserId))
                .thenReturn(Optional.of(requesterParticipant));

        // Act & Assert
        assertThatThrownBy(() -> messageService.deleteMessage(chatRoom.getChatRoomId(), message.getMessageId(), unauthorizedUserId))
                .isInstanceOf(UnauthorizedAccessException.class)
                .hasMessage("메시지 작성자 또는 방장만 메시지를 삭제할 수 있습니다.");
    }


}
