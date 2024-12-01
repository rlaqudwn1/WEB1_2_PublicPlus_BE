package backend.dev.activity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActivityUpdateDTO {

    @Schema(description = "모임 ID", example = "2")
    private Long activityId;

    @Schema(description = "모임 제목", example = "수정된 팀 회의 제목")
    private String title;

    @Schema(description = "구글 캘린더 이벤트 ID", example = "updated123xyz")
    private String eventId;

    @Schema(description = "모임 설명", example = "수정된 회의 설명입니다.")
    private String description;

    @Schema(description = "모임 장소", example = "서울 컨퍼런스룸 B")
    private String location;

    @Schema(description = "모임 시작 시간 (ISO 8601 형식)", example = "2024-12-01T09:30:00")
    private String startTime;

    @Schema(description = "모임 종료 시간 (ISO 8601 형식)", example = "2024-12-01T11:30:00")
    private String endTime;

    @Schema(description = "구글 캘린더 ID", example = "secondary (보조 캘린더)")
    private String googleCalenderId;

    @Schema(description = "모임 최대 참석자 수", example = "15")
    private int maxAttendees;
}
