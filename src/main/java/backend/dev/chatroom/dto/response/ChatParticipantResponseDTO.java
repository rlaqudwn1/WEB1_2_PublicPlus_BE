package backend.dev.chatroom.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "채팅방 참가자 응답 DTO")
public class ChatParticipantResponseDTO {

    @Schema(description = "참가자 ID", example = "12345")
    private Long participantId;

    @Schema(description = "유저 ID", example = "user123")
    private String userId;

    public ChatParticipantResponseDTO(Long participantId, String userId) {
        this.participantId = participantId;
        this.userId = userId;
    }

    public Long getParticipantId() {
        return participantId;
    }

    public void setParticipantId(Long participantId) {
        this.participantId = participantId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
