package backend.dev.meeting.dto.request;

import backend.dev.meeting.entity.SportType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class MeetingBoardRequestDTO {
    @NotNull(message = "운동종목은 필수 항목입니다.")
    private SportType sportType;

    @NotBlank(message = "제목은 필수 항목입니다.")
    private String mbTitle;

    @NotBlank(message = "내용은 필수 항목입니다.")
    @Size(max = 500, message = "Content can be up to 500 characters.")
    private String mbContent;

    @NotNull(message = "날짜는 필수 항목입니다.")
    private LocalDate mbDate;

    @NotNull(message = "시간은 필수 항목입니다.")
    private LocalTime mbTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @NotBlank(message = "장소는 필수 항목입니다.")
    private String mbLocation;

    @NotBlank(message = "주최자는 필수 항목입니다.")
    private String mbHost;

    @NotNull(message = "최대 참여자 수는 필수 항목입니다.")
    @Positive(message = "최대 참여자 수는 0보다 커야 합니다.")
    private Integer maxParticipants;
}

