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
    @PostMapping
    public ResponseEntity<ChatRoomResponseDTO> createChatRoom(
            @Valid @RequestBody ChatRoomRequestDTO requestDTO) {
        ChatRoomResponseDTO responseDTO = chatRoomService.createChatRoom(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @Operation(summary = "1:1 채팅방 생성 또는 조회",
            description = "사용자 간 1:1 채팅방을 생성하거나 기존 채팅방을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "1:1 채팅방 생성 또는 조회 성공",
                    content = @Content(schema = @Schema(implementation = ChatRoomResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping("/private")
    public ResponseEntity<ChatRoomResponseDTO> createPrivateChatRoom(
            @RequestParam String otherUserId) {
        ChatRoomResponseDTO responseDTO = chatRoomService.createOrFindPrivateChatRoom(otherUserId);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/{chatRoomId}/join")
    public ResponseEntity<Map<String, String>> joinChatRoom(@PathVariable Long chatRoomId) {
        chatRoomService.joinChatRoom(chatRoomId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "사용자가 채팅방에 입장했습니다.");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "참가자 강퇴", description = "주최자가 특정 참가자를 채팅방에서 강퇴합니다.")
    @PostMapping("/{chatRoomId}/kick")
    public ResponseEntity<String> kickParticipant(
            @PathVariable Long chatRoomId,
            @RequestParam String userId) {
        chatRoomService.kickUser(chatRoomId, userId);
        return ResponseEntity.ok("참가자가 채팅방에서 강퇴되었습니다.");
    }

    @Operation(summary = "그룹 채팅방 삭제", description = "그룹 채팅방을 삭제합니다.")
    @DeleteMapping("/{chatRoomId}")
    public ResponseEntity<Map<String, String>> deleteGroupChatRoom(@PathVariable Long chatRoomId) {
        chatRoomService.deleteGroupChatRoom(chatRoomId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "그룹 채팅방이 성공적으로 삭제되었습니다.");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "1:1 채팅방 삭제", description = "1:1 채팅방을 삭제합니다.")
    @DeleteMapping("/private/{chatRoomId}")
    public ResponseEntity<String> deletePrivateChatRoom(@PathVariable Long chatRoomId) {
        chatRoomService.deletePrivateChatRoom(chatRoomId);
        return ResponseEntity.ok("1:1 채팅방이 성공적으로 삭제되었습니다.");
    }

    @Operation(summary = "특정 채팅방 조회", description = "특정 채팅방의 정보를 조회합니다.")
    @GetMapping("/{chatRoomId}")
    public ResponseEntity<ChatRoomResponseDTO> getChatRoomById(@PathVariable Long chatRoomId) {
        ChatRoomResponseDTO responseDTO = chatRoomService.getChatRoomById(chatRoomId);
        return ResponseEntity.ok(responseDTO);
    }

    @Operation(summary = "모든 채팅방 조회", description = "현재 존재하는 모든 채팅방을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<ChatRoomResponseDTO>> getAllChatRooms() {
        return ResponseEntity.ok(chatRoomService.getAllChatRooms());
    }
}