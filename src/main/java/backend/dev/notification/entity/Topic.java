package backend.dev.notification.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int topic_Id;
    @Column(nullable = false,unique=true)
    private String topic_Name;

    @OneToMany(mappedBy = "topic")
    private List<TopicSubscription> subscriptions = new ArrayList<>();  // 구독자 목록

}
