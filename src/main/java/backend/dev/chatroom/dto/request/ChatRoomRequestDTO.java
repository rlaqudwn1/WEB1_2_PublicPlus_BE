package backend.dev.chatroom.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "채팅방 생성 요청 DTO")
public class ChatRoomRequestDTO {

    @NotBlank(message = "Chat room name is required")
    @Schema(description = "채팅방 이름", example = "Fun Chat Room")
    private String chatRoomName;

    @NotBlank(message = "Chat room type is required")
    @Schema(description = "채팅방 타입 (GROUP 또는 PRIVATE)", example = "GROUP")
    private String chatRoomType; // GROUP or PRIVATE
}