package backend.dev.activity.dto;

import backend.dev.activity.entity.Activity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ActivityCreateDTO {

    @Schema(description = "모임의 제목", example = "Weekly Team Meeting")
    private String title;

    @Schema(description = "구글 캘린더의 Event ID", example = "abc123xyz")
    private String eventId;

    @Schema(description = "모임에 대한 설명", example = "This is our regular weekly meeting to discuss project updates.")
    private String description;

    @Schema(description = "모임 장소", example = "Seoul Conference Room A")
    private String location;

    @Schema(description = "모임 시작 시간 (ISO 8601 형식)", example = "2024-11-30T09:00:00")
    private String startTime;

    @Schema(description = "모임 종료 시간 (ISO 8601 형식)", example = "2024-11-30T11:00:00")
    private String endTime;

    @Schema(description = "구글 캘린더 ID", example = "primary(기본 캘린더)")
    private String googleCalenderId;

    @Schema(description = "모임 최대 참석자 수", example = "10")
    private int maxAttendees;
}

