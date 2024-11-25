package backend.dev.notification.entity;

import backend.dev.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Data
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

}
