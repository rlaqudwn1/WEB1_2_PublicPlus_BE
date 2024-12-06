package backend.dev.chatroom.controller;

import backend.dev.chatroom.dto.request.MessageRequestDTO;
import backend.dev.chatroom.dto.response.MessageResponseDTO;
import backend.dev.chatroom.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@Tag(name = "Message Controller", description = "채팅 메시지 관련 API")
public class MessageController {

    private final MessageService messageService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public MessageController(MessageService messageService, SimpMessagingTemplate simpMessagingTemplate) {
        this.messageService = messageService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Operation(summary = "메시지 전송", description = "특정 채팅방에 메시지를 전송합니다.")
    @PostMapping
    public ResponseEntity<MessageResponseDTO> sendMessage(
            @RequestBody MessageRequestDTO requestDTO) {
        MessageResponseDTO responseDTO = messageService.sendMessage(requestDTO);
        simpMessagingTemplate.convertAndSend("/topic/chatroom/" + requestDTO.getChatRoomId(), responseDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @Operation(summary = "특정 채팅방 메시지 조회", description = "특정 채팅방에 포함된 메시지를 조회합니다.")
    @GetMapping("/{chatRoomId}")
    public ResponseEntity<List<MessageResponseDTO>> getMessagesByChatRoom(
            @PathVariable Long chatRoomId) {
        return ResponseEntity.ok(messageService.getMessagesByChatRoom(chatRoomId));
    }

    @Operation(summary = "메시지 삭제", description = "특정 메시지를 삭제합니다.")
    @DeleteMapping("/{chatRoomId}/{messageId}")
    public ResponseEntity<String> deleteMessage(
            @PathVariable Long chatRoomId,
            @PathVariable Long messageId,
            @RequestParam String requesterId) {
        String requester = SecurityContextHolder.getContext().getAuthentication().getName();
        messageService.deleteMessage(chatRoomId, messageId, requesterId);
        return ResponseEntity.ok("메시지가 성공적으로 삭제되었습니다.");
    }
}

