package backend.dev.chatroom.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "채팅방 생성 요청 DTO")
public class ChatRoomRequestDTO {

    @NotBlank(message = "채팅방 이름은 필수입니다.")
    @Schema(description = "채팅방 이름", example = "Fun Chat Room")
    private String chatRoomName;

    @NotBlank(message = "채팅방 타입은 필수입니다.")
    @Schema(description = "채팅방 타입 (GROUP 또는 PRIVATE)", example = "GROUP")
    private String chatRoomType; // GROUP or PRIVATE

    @Schema(description = "채팅방 최대 참여 인원 (1대1 채팅 시 2)", example = "10")
    private int maxParticipants;

    public ChatRoomRequestDTO(String chatRoomName, String chatRoomType, int maxParticipants) {
        this.chatRoomName = chatRoomName;
        this.chatRoomType = chatRoomType;
        this.maxParticipants = maxParticipants;
    }
}
