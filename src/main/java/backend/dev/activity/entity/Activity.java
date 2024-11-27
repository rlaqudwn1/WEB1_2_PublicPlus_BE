package backend.dev.activity.entity;

import backend.dev.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "activity")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long activityId; // 고유 ID

    @Column(nullable = false)
    private String title; // 활동 제목

    @Column(nullable = false)
    private String description; // 활동 설명

    @Column(nullable = false)
    private String location; // 활동 장소

    @Column(nullable = false)
    private LocalDateTime startTime; // 시작 시간

    @Column(nullable = false)
    private LocalDateTime endTime; // 종료 시간

    @Column(nullable = false)
    private int maxParticipants; // 최대 참석자 수

    @Column(nullable = false)
    private int currentParticipants; // 현재 참석자 수

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user; //

    private String googleEventId; // Google Calendar 연동 ID

    // 활동 수정 메서드들 (Change 메서드들)

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeDescription(String description) {
        this.description = description;
    }

    public void changeLocation(String location) {
        this.location = location;
    }

    public void changeStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void changeEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void changeMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public void changeGoogleEventId(String googleEventId) {
        this.googleEventId = googleEventId;
    }
    public void changeUser(User user) {
        this.user = user;
    }

    // 참가자 관련 메서드는 주석 처리됨, 필요에 따라 추가
    /*
    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participant> participants = new ArrayList<>();

    public void addParticipant(Participant participant) {
        if (participants.size() >= maxParticipants) {
            throw new IllegalStateException("Maximum number of participants reached.");
        }
        participants.add(participant);
        participant.setActivity(this);
    }

    public void removeParticipant(Participant participant) {
        participants.remove(participant);
        participant.setActivity(null);
    }

    public int getCurrentParticipants() {
        return participants.size();
    }
    */
}
