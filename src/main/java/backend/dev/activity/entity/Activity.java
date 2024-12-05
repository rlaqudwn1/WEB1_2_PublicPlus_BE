package backend.dev.activity.entity;

import backend.dev.meeting.entity.MeetingBoard;
import backend.dev.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "activity")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long activity_Id; // 고유 ID

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private int maxParticipants;

    @Column(nullable = false)
    private int currentParticipants;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="board_id" )
    private MeetingBoard meetingBoard;

    @Builder.Default
    @OneToMany(mappedBy = "activity",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<ActivityParticipants> participants = new HashSet<>();

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

    public void changeCurrentParticipants(int currentParticipants) {this.currentParticipants = currentParticipants;}




    // 추가된 메서드
    public void addParticipant(ActivityParticipants participant) {
        // 참가자 수가 최대 참가자 수를 초과하면 예외 발생
        if (this.participants.size() >= this.maxParticipants) {
            throw new IllegalStateException("Maximum number of participants reached.");
        }
        this.participants.add(participant);
        participant.changeActivity(this);  // 양방향 관계 설정
    }

    public void removeParticipant(ActivityParticipants participant) {
        this.participants.remove(participant);
        participant.changeActivity(null);  // 양방향 관계 끊기
    }
}
