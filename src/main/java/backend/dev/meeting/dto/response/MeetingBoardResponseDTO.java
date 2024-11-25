package backend.dev.meeting.dto.response;

import backend.dev.meeting.entity.SportType;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class MeetingBoardResponseDTO {
    private Long mbId; // 모임게시판 ID
    private SportType sportType; // 운동종목
    private String mbTitle; // 모임제목
    private String mbContent; // 모임내용
    private LocalDate mbDate; // 모임날짜
    private LocalTime mbTime; // 모임시간
    private String mbLocation; // 모임장소
    private String mbHost; // 주최자
    private Integer maxParticipants; // 최대참여인원수
    private LocalDateTime mbCreatedDate; // 생성일자
    private LocalDateTime mbLastModifiedDate; // 수정일자
}
