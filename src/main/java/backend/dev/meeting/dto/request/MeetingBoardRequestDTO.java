package backend.dev.meeting.dto.request;

import backend.dev.meeting.entity.SportType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Schema(description = "모임 게시판 요청 DTO")
public class MeetingBoardRequestDTO {

    @NotNull(message = "운동 종목은 필수 항목입니다.")
    @Schema(description = "운동 종목 (예: SOCCER, BASEBALL)", example = "SOCCER")
    private SportType sportType; // 운동 종목

    @NotBlank(message = "제목은 필수 항목입니다.")
    @Schema(description = "모임 제목", example = "풋살 경기 참가자 모집")
    private String mbTitle; // 모임 제목

    @NotBlank(message = "내용은 필수 항목입니다.")
    @Size(max = 500, message = "Content can be up to 500 characters.")
    @Schema(description = "모임 내용", example = "이번 주 토요일 오후 3시에 함께 풋살하실 분 모집합니다!")
    private String mbContent; // 모임 내용

    @NotNull(message = "시작 시간은 필수 항목입니다.")
    @Schema(description = "모임 시작 시간", example = "2024-12-01T15:00:00")
    private LocalDateTime startTime; // 모임 시작 시간

    @NotNull(message = "종료 시간은 필수 항목입니다.")
    @Schema(description = "모임 종료 시간", example = "2024-12-01T18:00:00")
    private LocalDateTime endTime; // 모임 종료 시간

    @NotBlank(message = "장소는 필수 항목입니다.")
    @Schema(description = "모임 장소", example = "서울특별시 강남구 삼성동 풋살장")
    private String mbLocation; // 모임 장소

    @NotNull(message = "최대 참여자 수는 필수 항목입니다.")
    @Positive(message = "최대 참여자 수는 0보다 커야 합니다.")
    @Schema(description = "최대 참여자 수", example = "10")
    private Integer maxParticipants; // 최대 참여자 수
}

