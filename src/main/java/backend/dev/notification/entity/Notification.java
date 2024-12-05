package backend.dev.notification.entity;

import backend.dev.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "Notification")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notification_Id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "title", nullable = false)
    private String title;

    private String message;

    // 알림 메시지 템플릿
    private NotificationTitleType notificationTitleType;

    private boolean isRead;

    @CreatedDate
    private LocalDateTime createdDate;
}
