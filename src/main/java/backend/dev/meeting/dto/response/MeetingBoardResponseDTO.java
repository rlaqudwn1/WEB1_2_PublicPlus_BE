package backend.dev.meeting.dto.response;

import backend.dev.meeting.entity.MeetingBoard;
import backend.dev.meeting.entity.SportType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Schema(description = "모임 게시판 응답 DTO")
@AllArgsConstructor
@NoArgsConstructor
public class MeetingBoardResponseDTO {

    @Schema(description = "모임 게시판 ID", example = "1001")
    private Long mbId;

    @Schema(description = "운동 종목 (예: SOCCER, BASEBALL)", example = "SOCCER")
    private SportType sportType;

    @Schema(description = "모임 제목", example = "풋살 경기 참가자 모집")
    private String mbTitle;

    @Schema(description = "모임 내용", example = "이번 주 토요일 오후 3시에 함께 풋살하실 분 모집합니다!")
    private String mbContent;

    @Schema(description = "모임 시작 시간", example = "2024-12-01T15:00:00")
    private LocalDateTime startTime;

    @Schema(description = "모임 종료 시간", example = "2024-12-01T18:00:00")
    private LocalDateTime endTime;

    @Schema(description = "모임 장소", example = "서울특별시 강남구 삼성동 풋살장")
    private String mbLocation;

    @Schema(description = "최대 참여자 수", example = "10")
    private Integer maxParticipants;

    @Schema(description = "생성 일자", example = "2024-11-20T14:30:00")
    private LocalDateTime mbCreatedDate;

    @Schema(description = "마지막 수정 일자", example = "2024-11-22T18:00:00")
    private LocalDateTime mbLastModifiedDate;

    @Schema(description = "모임 주최자 ID", example = "user123")
    private String mbHostId;

    private String openChatLink;

    // 엔티티를 기반으로 생성자를 추가
    public MeetingBoardResponseDTO(MeetingBoard meetingBoard) {
        if (meetingBoard == null) {
            throw new IllegalArgumentException("MeetingBoard cannot be null");
        }
        this.mbId = meetingBoard.getMbId();
        this.mbTitle = meetingBoard.getMbTitle();
        this.mbContent = meetingBoard.getMbContent();
        this.sportType = meetingBoard.getSportType();
        this.startTime = meetingBoard.getStartTime();
        this.endTime = meetingBoard.getEndTime();
        this.mbLocation = meetingBoard.getMbLocation();
        this.maxParticipants = meetingBoard.getMaxParticipants();
        this.mbHostId = meetingBoard.getMbHost().getUserId();
        this.mbCreatedDate = meetingBoard.getMbCreatedDate();
        this.mbLastModifiedDate = meetingBoard.getMbLastModifiedDate();
        this.openChatLink = meetingBoard.getOpenChatLink();
    }
}
