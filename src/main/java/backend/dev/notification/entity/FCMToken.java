package backend.dev.notification.entity;

import backend.dev.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FCMToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fcmToken_Id;

    @Column(nullable = false, unique = true)
    private String fcm_token;

    @ManyToOne
    @JoinColumn(name ="user_id")
    private User user;

    @OneToMany(mappedBy = "token", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TopicSubscription> subscriptions = new ArrayList<>();

    public void updateToken(String fcm_token) {
        this.fcm_token = fcm_token;
    }

}
