package backend.dev.chatroom.controller;

import backend.dev.chatroom.dto.request.MessageRequestDTO;
import backend.dev.chatroom.dto.response.MessageResponseDTO;
import backend.dev.chatroom.service.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class MessageControllerTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private MessageService messageService;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void sendMessage() throws Exception {
        // Given
        MessageRequestDTO requestDTO = new MessageRequestDTO();
        requestDTO.setChatRoomId(1L);
        requestDTO.setParticipantId(1L);
        requestDTO.setContent("Hello!");

        MessageResponseDTO responseDTO = MessageResponseDTO.builder()
                .messageId(1L)
                .content("Hello!")
                .sender("User1")
                .chatRoomId(1L)
                .sentAt(LocalDateTime.now())
                .build();

        Mockito.when(messageService.sendMessage(any(MessageRequestDTO.class)))
                .thenReturn(responseDTO);

        // When & Then
        mockMvc.perform(post("/api/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.messageId").value(1L))
                .andExpect(jsonPath("$.content").value("Hello!"))
                .andExpect(jsonPath("$.sender").value("User1"))
                .andExpect(jsonPath("$.chatRoomId").value(1L));
    }

    @Test
    void getMessagesByChatRoom() throws Exception {
        // Given
        List<MessageResponseDTO> responseDTOs = Collections.singletonList(
                MessageResponseDTO.builder()
                        .messageId(1L)
                        .content("Hello!")
                        .sender("User1")
                        .chatRoomId(1L)
                        .sentAt(LocalDateTime.now())
                        .build()
        );

        Mockito.when(messageService.getMessagesByChatRoom(eq(1L)))
                .thenReturn(responseDTOs);

        // When & Then
        mockMvc.perform(get("/api/messages/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].messageId").value(1L))
                .andExpect(jsonPath("$[0].content").value("Hello!"))
                .andExpect(jsonPath("$[0].sender").value("User1"))
                .andExpect(jsonPath("$[0].chatRoomId").value(1L));
    }

    @Test
    void deleteMessage() throws Exception {
        // Given
        String requesterId = "user123"; // 요청자 ID 추가
        Mockito.doNothing().when(messageService).deleteMessage(1L, 1L, requesterId);

        // When & Then
        mockMvc.perform(delete("/api/messages/1/1")
                        .param("requesterId", requesterId)) // 요청자 ID 추가
                .andExpect(status().isOk())
                .andExpect(content().string("메시지가 성공적으로 삭제되었습니다."));
    }
}