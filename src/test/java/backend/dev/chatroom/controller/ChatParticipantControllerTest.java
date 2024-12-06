package backend.dev.chatroom.controller;

import backend.dev.chatroom.entity.ChatParticipant;
import backend.dev.user.entity.User;
import backend.dev.chatroom.repository.ChatParticipantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ChatParticipantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatParticipantRepository chatParticipantRepository;

    @BeforeEach
    void setUp() {
        // Mock User 및 ChatParticipant 데이터 설정
        User mockUser = User.builder()
                .userId("user123")
                .email("testuser@example.com")
                .nickname("TestNickname")
                .build();

        ChatParticipant mockParticipant = ChatParticipant.builder()
                .participantId(1L)
                .user(mockUser)
                .build();

        // chatRoomId = 1일 때 Mock 데이터 반환 설정
        Mockito.when(chatParticipantRepository.findFirstByChatRoom_ChatRoomId(1L))
                .thenReturn(Optional.of(mockParticipant));
    }

    @Test
    public void testGetParticipant() throws Exception {
        String result = mockMvc.perform(get("/api/chat/chatparticipant")
                        .param("chatRoomId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.participantId").value(1L))
                .andExpect(jsonPath("$.userId").value("user123"))
                .andReturn().getResponse().getContentAsString(); // JSON 응답 가져오기

        System.out.println("Test Response: " + result); // 로그로 출력
    }
}
