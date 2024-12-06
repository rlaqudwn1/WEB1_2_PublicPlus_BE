package backend.dev.chatroom.service;

import backend.dev.chatroom.dto.response.ChatParticipantResponseDTO;
import backend.dev.chatroom.entity.ChatParticipant;
import backend.dev.chatroom.entity.ChatRoom;
import backend.dev.user.entity.User;
import backend.dev.chatroom.repository.ChatParticipantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class ChatParticipantServiceTest {

    @InjectMocks
    private ChatParticipantService chatParticipantService;

    @Mock
    private ChatParticipantRepository chatParticipantRepository;

    private ChatParticipant chatParticipant;
    private ChatRoom chatRoom;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock ChatRoom
        chatRoom = new ChatRoom();
        chatRoom.setChatRoomId(1L);

        // Mock User
        user = new User();
        user.setUserId("user123");
        user.setNickname("TestUser");

        // Mock ChatParticipant
        chatParticipant = new ChatParticipant();
        chatParticipant.setParticipantId(101L);
        chatParticipant.setChatRoom(chatRoom);
        chatParticipant.setUser(user);
    }

    @Test
    void getParticipant_ShouldReturnParticipant_WhenParticipantExists() {
        // Arrange
        when(chatParticipantRepository.findFirstByChatRoom_ChatRoomId(1L))
                .thenReturn(Optional.of(chatParticipant));

        // Act
        ChatParticipantResponseDTO response = chatParticipantService.getParticipant(1L);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getParticipantId()).isEqualTo(chatParticipant.getParticipantId());
        assertThat(response.getUserId()).isEqualTo(user.getUserId());
        verify(chatParticipantRepository, times(1)).findFirstByChatRoom_ChatRoomId(1L);
    }

    @Test
    void getParticipant_ShouldThrowException_WhenParticipantDoesNotExist() {
        // Arrange
        when(chatParticipantRepository.findFirstByChatRoom_ChatRoomId(1L))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> chatParticipantService.getParticipant(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("참여자를 찾을 수 없습니다.");
        verify(chatParticipantRepository, times(1)).findFirstByChatRoom_ChatRoomId(1L);
    }
}
