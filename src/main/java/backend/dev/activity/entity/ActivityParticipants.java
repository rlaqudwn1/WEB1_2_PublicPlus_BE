package backend.dev.activity.entity;

import backend.dev.chatroom.entity.ChatRoom;
import backend.dev.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table
public class ActivityParticipants {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long participantId;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private ParticipantsRole role; // 역할


    public void changeParticipantId(Long participantId) {
        this.participantId = participantId;
    }

    public void changeRole(ParticipantsRole role) {this.role = role;}
    // 수정된 메서드: host 값 반환 메서드
    public void changeActivity(Activity activity) {this.activity = activity;}

    public void changeUser(User user) {
        this.user = user;
    }
}
