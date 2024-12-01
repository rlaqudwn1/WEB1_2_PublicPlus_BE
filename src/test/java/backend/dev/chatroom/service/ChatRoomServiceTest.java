package backend.dev.chatroom.service;

import backend.dev.chatroom.dto.response.ChatRoomResponseDTO;
import backend.dev.chatroom.dto.request.ChatRoomRequestDTO;
import backend.dev.chatroom.entity.ChatRoom;
import backend.dev.chatroom.entity.ChatParticipant;
import backend.dev.chatroom.repository.ChatRoomRepository;
import backend.dev.chatroom.repository.ChatParticipantRepository;
import backend.dev.user.entity.User;
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
        // SecurityContextHolder 설정
        testUser = new User();
        testUser.setUserId("user123");
        testUser.setEmail("testuser@example.com");
        testUser.setNickname("TestNickname");

        // SecurityContext에 사용자 설정
        SecurityContextHolder.clearContext();
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        testUser.getUserId(), null, Collections.emptyList()
                )
        );

        // Mock 동작 설정
        Mockito.when(userRepository.findById(testUser.getUserId())).thenReturn(Optional.of(testUser));
        Mockito.when(chatRoomRepository.save(any(ChatRoom.class))).thenAnswer(invocation -> {
            ChatRoom chatRoom = invocation.getArgument(0);
            chatRoom.setChatRoomId(1L);
            return chatRoom;
        });
    }

    @Test
    void testCreateChatRoom() {
        // Given
        ChatRoomRequestDTO chatRoomRequestDTO = new ChatRoomRequestDTO();
        chatRoomRequestDTO.setChatRoomName("Test Room");
        chatRoomRequestDTO.setChatRoomType("GROUP");

        // When
        ChatRoomResponseDTO createdChatRoom = chatRoomService.createChatRoom(chatRoomRequestDTO);

        // Then
        assertNotNull(createdChatRoom);
        assertEquals("Test Room", createdChatRoom.getChatRoomName());
        assertEquals("GROUP", createdChatRoom.getChatRoomType());
        assertEquals(1L, createdChatRoom.getChatRoomId());
    }
}
