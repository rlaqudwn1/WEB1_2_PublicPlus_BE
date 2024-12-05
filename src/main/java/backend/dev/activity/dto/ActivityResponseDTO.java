package backend.dev.activity.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ActivityResponseDTO(

        @Schema(description = "모임의 제목입니다", example = "주간 미팅")
        String title,

        @Schema(description = "모임의 설명입니다", example = "주간 회의를 위한 미팅입니다.")
        String description,

        @Schema(description = "모임 지역입니다", example = "컨퍼런스 룸 A")
        String location,

        @Schema(description = "모임 시작시간입니다  (ISO 8601 로 이루어져있습니다)", example = "2024-12-01T10:00:00")
        String startTime,

        @Schema(description = "모임 종료시간입니다 (ISO 8601 format)", example = "2024-12-01T12:00:00")
        String endTime,

        @Schema(description = "최대 참가자 수 입니다", example = "20")
        int maxParticipants,

        @Schema(description = "현재 참가자 수 입니다", example = "1")
        int currentParticipants
) {
}
