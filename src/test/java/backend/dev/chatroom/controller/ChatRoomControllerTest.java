package backend.dev.chatroom.controller;

import backend.dev.chatroom.dto.request.ChatRoomRequestDTO;
import backend.dev.chatroom.dto.response.ChatRoomResponseDTO;
import backend.dev.chatroom.service.ChatRoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testCreateChatRoom() throws Exception {
        ChatRoomRequestDTO requestDTO = new ChatRoomRequestDTO("Test Room", "GROUP", 10);
        ChatRoomResponseDTO responseDTO = new ChatRoomResponseDTO(1L, "Test Room", "GROUP", LocalDateTime.now());

        Mockito.when(chatRoomService.createChatRoom(any())).thenReturn(responseDTO);

        mockMvc.perform(post("/api/chatroom")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chatRoomId").value(1L))
                .andExpect(jsonPath("$.chatRoomName").value("Test Room"))
                .andExpect(jsonPath("$.chatRoomType").value("GROUP"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testJoinChatRoom() throws Exception {
        Mockito.doNothing().when(chatRoomService).joinChatRoom(anyLong());

        mockMvc.perform(post("/api/chatroom/1/join"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("사용자가 채팅방에 입장했습니다."));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testLeaveChatRoom() throws Exception {
        Mockito.doNothing().when(chatRoomService).leaveChatRoom(anyLong());

        mockMvc.perform(delete("/api/chatroom/1/leave"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("채팅방을 성공적으로 나갔습니다."));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetChatRoomById() throws Exception {
        ChatRoomResponseDTO responseDTO = new ChatRoomResponseDTO(1L, "Test Room", "GROUP", LocalDateTime.now());

        Mockito.when(chatRoomService.getChatRoomById(anyLong())).thenReturn(responseDTO);

        mockMvc.perform(get("/api/chatroom/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chatRoomId").value(1L))
                .andExpect(jsonPath("$.chatRoomName").value("Test Room"))
                .andExpect(jsonPath("$.chatRoomType").value("GROUP"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetAllChatRooms() throws Exception {
        ChatRoomResponseDTO responseDTO = new ChatRoomResponseDTO(1L, "Test Room", "GROUP", LocalDateTime.now());

        Mockito.when(chatRoomService.getAllChatRooms()).thenReturn(Collections.singletonList(responseDTO));

        mockMvc.perform(get("/api/chatroom"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].chatRoomId").value(1L))
                .andExpect(jsonPath("$[0].chatRoomName").value("Test Room"))
                .andExpect(jsonPath("$[0].chatRoomType").value("GROUP"));
    }
}
