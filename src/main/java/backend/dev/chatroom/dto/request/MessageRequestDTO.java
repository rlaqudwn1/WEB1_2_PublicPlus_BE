package backend.dev.chatroom.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "메시지 요청 DTO")
public class MessageRequestDTO {

    @NotNull(message = "채팅방 ID는 필수입니다.")
    @Schema(description = "채팅방 ID", example = "101")
    private Long chatRoomId;

    @NotNull(message = "참가자 ID는 필수입니다.")
    @Schema(description = "참가자 ID", example = "202")
    private Long participantId;

    @NotBlank(message = "메시지 내용은 필수입니다.")
    @Schema(description = "메시지 내용", example = "안녕하세요!")
    private String content;
}
