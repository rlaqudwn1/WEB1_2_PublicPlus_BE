package backend.dev.chatroom.controller;

import backend.dev.chatroom.dto.request.MessageRequestDTO;
import backend.dev.chatroom.dto.response.MessageResponseDTO;
import backend.dev.chatroom.service.MessageService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageService messageService;

    @MockBean
    private SimpMessagingTemplate simpMessagingTemplate;

    @Test
    public void testSendMessage() throws Exception {
        String messageJson = """
                {
                    "chatRoomId": 1,
                    "participantId": 202,
                    "content": "Hello World"
                }
                """;

        MessageResponseDTO mockResponse = MessageResponseDTO.builder()
                .messageId(1L)
                .chatRoomId(1L)
                .content("Hello World")
                .sender("홍길동")
                .sentAt(LocalDateTime.now())
                .build();

        Mockito.when(messageService.sendMessage(any(MessageRequestDTO.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(messageJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Hello World"))
                .andExpect(jsonPath("$.chatRoomId").value(1));
    }

    @Test
    public void testGetMessagesByChatRoom() throws Exception {
        List<MessageResponseDTO> mockMessages = List.of(
                MessageResponseDTO.builder()
                        .messageId(1L)
                        .chatRoomId(1L)
                        .content("Hello")
                        .sender("홍길동")
                        .sentAt(LocalDateTime.now())
                        .build()
        );

        Mockito.when(messageService.getMessagesByChatRoom(1L)).thenReturn(mockMessages);

        mockMvc.perform(get("/api/messages/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void testDeleteMessage() throws Exception {
        Mockito.doNothing().when(messageService).deleteMessage(anyLong(), anyLong(), anyString());

        mockMvc.perform(delete("/api/messages/1/1")
                        .param("requesterId", "test-user"))
                .andExpect(status().isOk())
                .andExpect(content().string("메시지가 성공적으로 삭제되었습니다."));
    }
}