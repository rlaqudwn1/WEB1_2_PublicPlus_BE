package backend.dev.meeting.entity;

import backend.dev.meeting.dto.request.MeetingBoardRequestDTO;
import backend.dev.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "meeting_board")
public class MeetingBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mb_id", nullable = false, unique = true)
    private Long mbId; // 모임게시판고유아이디

    @Enumerated(EnumType.STRING)
    @Column(name = "sport_type", nullable = false)
    private SportType sportType; // 운동종목

    @Column(name = "mb_title", nullable = false)
    private String mbTitle; // 모임제목

    @Column(name = "mb_content", nullable = false, length = 500)
    private String mbContent; // 모임내용

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime; // 모임 시작 시간

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime; // 모임 종료 시간

    @Column(name = "mb_location", nullable = false)
    private String mbLocation; // 모임장소

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mb_host_id", nullable = false)
    private User mbHost;  // 주최자 (User 엔티티와 연관)

    @Column(name = "max_participants", nullable = false)
    private Integer maxParticipants; // 최대참여인원수

    @CreatedDate
    @Column(name = "mb_created_date", updatable = false)
    private LocalDateTime mbCreatedDate; // 모임생성일자

    @LastModifiedDate
    @Column(name = "mb_last_modified_date")
    private LocalDateTime mbLastModifiedDate; // 모임 수정 일자

    @Column(name = "mb_deleted_date")
    private LocalDateTime mbDeletedDate; // 모임삭제일자

    // 생성자
    @Builder
    public MeetingBoard(Long mbId, SportType sportType, String mbTitle, String mbContent,
                        LocalDateTime startTime, LocalDateTime endTime, String mbLocation,
                        User mbHost, Integer maxParticipants) {
        this.mbId = mbId;
        this.sportType = sportType;
        this.mbTitle = mbTitle;
        this.mbContent = mbContent;
        this.startTime = startTime;
        this.endTime = endTime;
        this.mbLocation = mbLocation;
        this.mbHost = mbHost;
        this.maxParticipants = maxParticipants;
    }

    // 추가 생성자
    public MeetingBoard(MeetingBoardRequestDTO dto, User host) {
        this.sportType = dto.getSportType();
        this.mbTitle = dto.getMbTitle();
        this.mbContent = dto.getMbContent();
        this.startTime = dto.getStartTime();
        this.endTime = dto.getEndTime();
        this.mbLocation = dto.getMbLocation();
        this.mbHost = host;
        this.maxParticipants = dto.getMaxParticipants();
    }
}
