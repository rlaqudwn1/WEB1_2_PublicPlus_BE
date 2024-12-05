package backend.dev.chatroom.service;

import backend.dev.chatroom.dto.request.ChatRoomRequestDTO;
import backend.dev.chatroom.dto.response.ChatRoomResponseDTO;
import backend.dev.chatroom.entity.ChatParticipant;
import backend.dev.chatroom.entity.ChatRoom;
import backend.dev.user.entity.User;
import backend.dev.chatroom.repository.ChatParticipantRepository;
import backend.dev.chatroom.repository.ChatRoomRepository;
import backend.dev.user.entity.Role;
import backend.dev.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class ChatRoomServiceTest {

    @Autowired
    private ChatRoomService chatRoomService;

    @MockBean
    private ChatRoomRepository chatRoomRepository;

    @MockBean
    private ChatParticipantRepository chatParticipantRepository;

    @MockBean
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        // User 초기화
        testUser = User.builder()
                .userId("user123")
                .email("testuser@example.com")
                .nickname("TestNickname")
                .password("password") // 필요한 필드
                .role(Role.USER)      // Role 기본값
                .build();

        // SecurityContext 설정
        SecurityContextHolder.clearContext();
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        testUser.getUserId(), null, Collections.emptyList()
                )
        );

        // Mock 설정
        Mockito.when(userRepository.findById(testUser.getUserId())).thenReturn(Optional.of(testUser));
        Mockito.when(chatRoomRepository.save(any(ChatRoom.class))).thenAnswer(invocation -> {
            ChatRoom chatRoom = invocation.getArgument(0);
            chatRoom.setChatRoomId(1L);
            return chatRoom;
        });
        Mockito.when(chatRoomRepository.findById(1L)).thenReturn(Optional.of(new ChatRoom()));
        Mockito.when(chatParticipantRepository.existsByChatRoomAndUser_Email(any(ChatRoom.class), any(String.class)))
                .thenReturn(false);
    }

    @Test
    void testCreateChatRoom() {
        // Given
        ChatRoomRequestDTO requestDTO = new ChatRoomRequestDTO("Test Room", "GROUP", 10);

        // When
        ChatRoomResponseDTO responseDTO = chatRoomService.createChatRoom(requestDTO);

        // Then
        assertNotNull(responseDTO);
        assertEquals("Test Room", responseDTO.getChatRoomName());
        assertEquals("GROUP", responseDTO.getChatRoomType());
    }

    @Test
    void testJoinChatRoom() {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setChatRoomId(1L);
        chatRoom.setName("Test Room");

        Mockito.when(chatRoomRepository.findById(1L)).thenReturn(Optional.of(chatRoom));

        assertDoesNotThrow(() -> chatRoomService.joinChatRoom(1L));
    }

    @Test
    void testLeaveChatRoom_AsHost() {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setChatRoomId(1L);

        ChatParticipant participant = new ChatParticipant();
        participant.setHost(true);
        participant.setChatRoom(chatRoom);

        Mockito.when(chatRoomRepository.findById(1L)).thenReturn(Optional.of(chatRoom));
        Mockito.when(chatParticipantRepository.findByChatRoomAndUserEmail(chatRoom, testUser.getEmail()))
                .thenReturn(Optional.of(participant));

        assertDoesNotThrow(() -> chatRoomService.leaveChatRoom(1L));
        Mockito.verify(chatRoomRepository, Mockito.times(1)).delete(chatRoom);
    }
}
