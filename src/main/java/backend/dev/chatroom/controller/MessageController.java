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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메시지 전송 성공",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\n  \"httpStatus\": \"BAD_REQUEST\",\n  \"message\": \"잘못된 요청입니다.\"\n}")))
    })
    @PostMapping
    public ResponseEntity<MessageResponseDTO> sendMessage(@RequestBody MessageRequestDTO requestDTO) {
        MessageResponseDTO responseDTO = messageService.sendMessage(requestDTO);

        simpMessagingTemplate.convertAndSend("/topic/chatroom/" + requestDTO.getChatRoomId(), responseDTO);

        return ResponseEntity.ok(responseDTO);
    }

    @Operation(summary = "메시지 조회", description = "특정 채팅방의 메시지를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메시지 조회 성공",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "채팅방을 찾을 수 없음",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\n  \"httpStatus\": \"NOT_FOUND\",\n  \"message\": \"채팅방을 찾을 수 없습니다.\"\n}")))
    })
    @GetMapping("/{chatRoomId}")
    public ResponseEntity<List<MessageResponseDTO>> getMessagesByChatRoom(@PathVariable Long chatRoomId) {
        return ResponseEntity.ok(messageService.getMessagesByChatRoom(chatRoomId));
    }

    @Operation(summary = "메시지 삭제", description = "특정 채팅방의 메시지를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메시지 삭제 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\n  \"message\": \"메시지가 성공적으로 삭제되었습니다.\"\n}"))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\n  \"httpStatus\": \"FORBIDDEN\",\n  \"message\": \"권한이 없습니다.\"\n}"))),
            @ApiResponse(responseCode = "404", description = "메시지를 찾을 수 없음",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\n  \"httpStatus\": \"NOT_FOUND\",\n  \"message\": \"메시지를 찾을 수 없습니다.\"\n}")))
    })
    @DeleteMapping("/{chatRoomId}/{messageId}")
    public ResponseEntity<String> deleteMessage(
            @PathVariable Long chatRoomId,
            @PathVariable Long messageId,
            @RequestParam String requesterId) {
        messageService.deleteMessage(chatRoomId, messageId, requesterId);
        return ResponseEntity.ok("메시지가 성공적으로 삭제되었습니다.");
    }
}
