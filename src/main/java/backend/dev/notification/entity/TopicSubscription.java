package backend.dev.notification.entity;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class TopicSubscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subscriptionId;

    @ManyToOne
    @JoinColumn(name = "token_id")
    private FCMToken token;  // 구독한 FCMToken

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;  // 구독한 Topic
}
