package backend.dev.meeting.entity;

import backend.dev.activity.entity.Activity;
import backend.dev.meeting.dto.request.MeetingBoardRequestDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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

    @Column(name = "mb_date", nullable = false)
    private LocalDate mbDate; // 모임날짜

    @Column(name = "mb_time", nullable = false)
    private LocalTime mbTime; // 모임시간

    @Column(name = "mb_location", nullable = false)
    private String mbLocation; // 모임장소

    @Column(name = "mb_host", nullable = false)
    private String mbHost;  // 주최자

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
    // 모임과 모임 게시판의 OneToOne 관계
    @OneToOne(fetch = FetchType.EAGER)
    private Activity activity;

    public MeetingBoard(MeetingBoardRequestDTO meetingBoardRequestDTO) {
    }
}
