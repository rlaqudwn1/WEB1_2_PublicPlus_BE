package backend.dev.chatroom.controller;

import backend.dev.chatroom.dto.request.ChatRoomRequestDTO;
import backend.dev.chatroom.dto.response.ChatRoomResponseDTO;
import backend.dev.chatroom.service.ChatRoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ChatRoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatRoomService chatRoomService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        Locale.setDefault(Locale.KOREA); // 테스트 환경의 Locale 강제 설정
        mockMvc = MockMvcBuilders.standaloneSetup(new ChatRoomController(chatRoomService)).build();
    }

    @Test
    @WithMockUser(username = "testuser")
    void createChatRoom() throws Exception {
        // Given
        ChatRoomRequestDTO requestDTO = new ChatRoomRequestDTO();
        requestDTO.setChatRoomName("Test Room");
        requestDTO.setChatRoomType("GROUP");

        ChatRoomResponseDTO responseDTO = new ChatRoomResponseDTO();
        responseDTO.setChatRoomId(1L);
        responseDTO.setChatRoomName("Test Room");
        responseDTO.setChatRoomType("GROUP");

        Mockito.when(chatRoomService.createChatRoom(any())).thenReturn(responseDTO);

        // When & Then
        mockMvc.perform(post("/api/chatroom")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chatRoomId").value(1L))
                .andExpect(jsonPath("$.chatRoomName").value("Test Room"))
                .andExpect(jsonPath("$.chatRoomType").value("GROUP"));

        Mockito.verify(chatRoomService, Mockito.times(1)).createChatRoom(any());
    }

    @Test
    @WithMockUser(username = "testuser")
    void createPrivateChatRoom() throws Exception {
        // Given
        String otherUserId = "otheruser";
        ChatRoomResponseDTO responseDTO = new ChatRoomResponseDTO();
        responseDTO.setChatRoomId(1L);
        responseDTO.setChatRoomName("Private Room");
        responseDTO.setChatRoomType("PRIVATE");

        Mockito.when(chatRoomService.createOrFindPrivateChatRoom(anyString())).thenReturn(responseDTO);

        // When & Then
        mockMvc.perform(post("/api/chatroom/private")
                        .param("otherUserId", otherUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chatRoomId").value(1L))
                .andExpect(jsonPath("$.chatRoomName").value("Private Room"))
                .andExpect(jsonPath("$.chatRoomType").value("PRIVATE"));

        Mockito.verify(chatRoomService, Mockito.times(1)).createOrFindPrivateChatRoom(anyString());
    }

    @Test
    @WithMockUser(username = "testuser")
    void joinChatRoom() throws Exception {
        // Given
        Long chatRoomId = 1L;

        // When & Then
        mockMvc.perform(post("/api/chatroom/{chatRoomId}/join", chatRoomId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("사용자가 채팅방에 입장했습니다.")); // JSON 형식으로 메시지 검증

        Mockito.verify(chatRoomService, Mockito.times(1)).joinChatRoom(chatRoomId);
    }

    @Test
    @WithMockUser(username = "testuser")
    void deleteGroupChatRoom() throws Exception {
        Long chatRoomId = 1L;

        mockMvc.perform(delete("/api/chatroom/{chatRoomId}", chatRoomId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("그룹 채팅방이 성공적으로 삭제되었습니다."));
    }


    @Test
    @WithMockUser(username = "testuser")
    void getChatRoomById() throws Exception {
        // Given
        Long chatRoomId = 1L;
        ChatRoomResponseDTO responseDTO = new ChatRoomResponseDTO();
        responseDTO.setChatRoomId(chatRoomId);
        responseDTO.setChatRoomName("Test Room");
        responseDTO.setChatRoomType("GROUP");

        Mockito.when(chatRoomService.getChatRoomById(chatRoomId)).thenReturn(responseDTO);

        // When & Then
        mockMvc.perform(get("/api/chatroom/{chatRoomId}", chatRoomId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chatRoomId").value(chatRoomId))
                .andExpect(jsonPath("$.chatRoomName").value("Test Room"))
                .andExpect(jsonPath("$.chatRoomType").value("GROUP"));

        Mockito.verify(chatRoomService, Mockito.times(1)).getChatRoomById(chatRoomId);
    }
}
