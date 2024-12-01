package backend.dev.chatroom.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "메시지 요청 DTO")
public class MessageRequestDTO {

    @Schema(description = "채팅방 ID", example = "101")
    private Long chatRoomId; // 채팅방 ID

    @Schema(description = "참가자 ID", example = "202")
    private Long participantId; // 참가자 ID

    @Schema(description = "메시지 내용", example = "안녕하세요!")
    private String content; // 메시지 내용
}