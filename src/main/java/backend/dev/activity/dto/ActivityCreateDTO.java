package backend.dev.activity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ActivityCreateDTO {

    @Schema(description = "모임의 제목", example = "주간 팀 회의")
    private String title;

    @Schema(description = "구글 캘린더 이벤트 ID", example = "abc123xyz")
    private String eventId;

    @Schema(description = "모임 설명", example =  "주간 회의입니다.")
    private String description;

    @Schema(description = "모임 장소", example = "서울 컨퍼런스룸 A")
    private String location;

    @Schema(description = "모임 시작 시간 (ISO 8601 형식)", example = "2024-11-30T09:00:00")
    private String startTime;

    @Schema(description = "모임 종료 시간 (ISO 8601 형식)", example = "2024-11-30T11:00:00")
    private String endTime;

    @Schema(description = "구글 캘린더 ID", example = "primary (기본 캘린더)")
    private String googleCalenderId;

    @Schema(description = "모임 최대 참석자 수", example = "10")
    private int maxAttendees;
}
