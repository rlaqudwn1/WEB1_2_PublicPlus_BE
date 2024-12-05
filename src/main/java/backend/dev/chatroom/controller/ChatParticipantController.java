package backend.dev.chatroom.controller;

import backend.dev.chatroom.dto.response.ChatParticipantResponseDTO;
import backend.dev.chatroom.service.ChatParticipantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
@Tag(name = "Chat Participant Controller", description = "채팅 참가자 관련 API")
public class ChatParticipantController {

    private final ChatParticipantService chatParticipantService;

    public ChatParticipantController(ChatParticipantService chatParticipantService) {
        this.chatParticipantService = chatParticipantService;
    }
    @Operation(summary = "채팅 참가자 정보 조회", description = "특정 채팅방에 속한 참가자의 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "참가자 정보 조회 성공",
                    content = @Content(schema = @Schema(implementation = ChatParticipantResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\n  \"message\": \"잘못된 요청입니다.\"\n}")))
    })
    @GetMapping("/chatparticipant")
    public ChatParticipantResponseDTO getParticipant(
            @RequestParam("chatRoomId") Long chatRoomId) {
        return chatParticipantService.getParticipant(chatRoomId);
    }
}