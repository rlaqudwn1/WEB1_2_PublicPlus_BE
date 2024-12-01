package backend.dev.chatroom.service;

import backend.dev.chatroom.dto.request.MessageRequestDTO;
import backend.dev.chatroom.dto.response.MessageResponseDTO;
import backend.dev.chatroom.entity.ChatRoom;
import backend.dev.chatroom.entity.ChatParticipant;
import backend.dev.chatroom.entity.Message;
import backend.dev.chatroom.exception.UnauthorizedAccessException;
import backend.dev.chatroom.repository.ChatRoomRepository;
import backend.dev.chatroom.repository.MessageRepository;
import backend.dev.chatroom.repository.ChatParticipantRepository;
import backend.dev.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MessageServiceTest {

    @InjectMocks
    private MessageService messageService;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private ChatParticipantRepository chatParticipantRepository;

    private ChatRoom chatRoom;
    private ChatParticipant chatParticipant;
    private Message message;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // 기본 테스트 데이터 초기화
        chatRoom = new ChatRoom();
        chatRoom.setChatRoomId(101L);

        User user = new User(); // User 객체 생성
        user.setUserId("hostUser"); // 테스트용 userId 설정
        user.setNickname("홍길동"); // 테스트용 닉네임 설정

        chatParticipant = new ChatParticipant();
        chatParticipant.setParticipantId(202L);
        chatParticipant.setChatRoom(chatRoom);
        chatParticipant.setUser(user); // User 설정
        chatParticipant.setHost(true); // Host 설정

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
        when(messageRepository.save(any(Message.class))).thenReturn(message);

        // Act
        MessageResponseDTO responseDTO = messageService.sendMessage(requestDTO);

        // Assert
        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.getMessageId()).isEqualTo(message.getMessageId());
        assertThat(responseDTO.getContent()).isEqualTo("Hello, world!");
        verify(messageRepository, times(1)).save(any(Message.class));
    }

    @Test
    void sendMessage_ShouldThrowException_WhenChatRoomNotFound() {
        // Arrange
        MessageRequestDTO requestDTO = new MessageRequestDTO();
        requestDTO.setChatRoomId(999L); // 없는 채팅방 ID
        requestDTO.setParticipantId(chatParticipant.getParticipantId());
        requestDTO.setContent("Test message");

        when(chatRoomRepository.findById(requestDTO.getChatRoomId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> messageService.sendMessage(requestDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("채팅방을 찾을 수 없습니다.");
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
        assertThat(messages.get(0).getContent()).isEqualTo("Hello, world!");
        verify(messageRepository, times(1)).findByChatRoom_ChatRoomId(chatRoom.getChatRoomId());
    }

    @Test
    void deleteMessage_ShouldSucceed_WhenRequesterIsAuthorized() {
        // Arrange
        String requesterId = "hostUser";
        chatParticipant.setHost(true);

        when(chatRoomRepository.findById(chatRoom.getChatRoomId())).thenReturn(Optional.of(chatRoom));
        when(messageRepository.findById(message.getMessageId())).thenReturn(Optional.of(message));
        when(chatParticipantRepository.findByChatRoomAndUserId(chatRoom, requesterId))
                .thenReturn(Optional.of(chatParticipant));

        // Act
        messageService.deleteMessage(chatRoom.getChatRoomId(), message.getMessageId(), requesterId);

        // Assert
        verify(messageRepository, times(1)).delete(message);
    }

    @Test
    void deleteMessage_ShouldThrowException_WhenRequesterIsNotAuthorized() {
        // Arrange
        String requesterId = "unauthorizedUser";

        when(chatRoomRepository.findById(chatRoom.getChatRoomId())).thenReturn(Optional.of(chatRoom));
        when(messageRepository.findById(message.getMessageId())).thenReturn(Optional.of(message));
        when(chatParticipantRepository.findByChatRoomAndUserId(chatRoom, requesterId))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> messageService.deleteMessage(chatRoom.getChatRoomId(), message.getMessageId(), requesterId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("요청자를 해당 채팅방에서 찾을 수 없습니다.");
    }
}
