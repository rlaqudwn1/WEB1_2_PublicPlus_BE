package backend.dev.chatroom.controller;

import backend.dev.chatroom.dto.request.ChatRoomRequestDTO;
import backend.dev.chatroom.dto.response.ChatRoomResponseDTO;
import backend.dev.chatroom.service.ChatRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chatroom")
@Tag(name = "ChatRoom Controller", description = "채팅방 관련 API")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    public ChatRoomController(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    @Operation(summary = "그룹 채팅방 생성", description = "새로운 그룹 채팅방을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "채팅방 생성 성공",
                    content = @Content(schema = @Schema(implementation = ChatRoomResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\n  \"message\": \"잘못된 요청입니다.\"\n}")))
    })
    @PostMapping
    public ResponseEntity<ChatRoomResponseDTO> createChatRoom(
            @Valid @RequestBody ChatRoomRequestDTO requestDTO) {
        ChatRoomResponseDTO responseDTO = chatRoomService.createChatRoom(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @Operation(summary = "특정 채팅방 입장", description = "사용자가 특정 채팅방에 입장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "채팅방 입장 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\n  \"message\": \"사용자가 채팅방에 입장했습니다.\"\n}"))),
            @ApiResponse(responseCode = "404", description = "채팅방을 찾을 수 없음",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\n  \"message\": \"채팅방을 찾을 수 없습니다.\"\n}")))
    })
    @PostMapping("/{chatRoomId}/join")
    public ResponseEntity<Map<String, String>> joinChatRoom(@PathVariable Long chatRoomId) {
        chatRoomService.joinChatRoom(chatRoomId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "사용자가 채팅방에 입장했습니다.");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "특정 채팅방 정보 조회", description = "특정 채팅방의 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "채팅방 정보 조회 성공",
                    content = @Content(schema = @Schema(implementation = ChatRoomResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "채팅방을 찾을 수 없음",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\n  \"message\": \"채팅방을 찾을 수 없습니다.\"\n}")))
    })
    @GetMapping("/{chatRoomId}")
    public ResponseEntity<ChatRoomResponseDTO> getChatRoomById(@PathVariable Long chatRoomId) {
        ChatRoomResponseDTO responseDTO = chatRoomService.getChatRoomById(chatRoomId);
        return ResponseEntity.ok(responseDTO);
    }

    @Operation(summary = "모든 채팅방 조회", description = "현재 존재하는 모든 채팅방을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "채팅방 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = ChatRoomResponseDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<ChatRoomResponseDTO>> getAllChatRooms() {
        return ResponseEntity.ok(chatRoomService.getAllChatRooms());
    }

    @Operation(summary = "채팅방 나가기", description = "참가자가 채팅방을 나갑니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "채팅방 나가기 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\n  \"message\": \"채팅방을 성공적으로 나갔습니다.\"\n}"))),
            @ApiResponse(responseCode = "404", description = "채팅방을 찾을 수 없음",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\n  \"message\": \"채팅방을 찾을 수 없습니다.\"\n}")))
    })
    @DeleteMapping("/{chatRoomId}/leave")
    public ResponseEntity<Map<String, String>> leaveChatRoom(@PathVariable Long chatRoomId) {
        chatRoomService.leaveChatRoom(chatRoomId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "채팅방을 성공적으로 나갔습니다.");
        return ResponseEntity.ok(response);
    }
}
