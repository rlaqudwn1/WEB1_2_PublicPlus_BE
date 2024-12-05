package backend.dev.chatroom.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatRoomId;

    private String name; // 채팅방 이름

    @Enumerated(EnumType.STRING)
    private ChatRoomType type; // 채팅방 타입 (예: GROUP, PRIVATE)

    @Column(nullable = false)
    private LocalDateTime startTime; // 모임 시작 시간

    private boolean isDeleted = false; // 삭제 여부

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatParticipant> chatParticipants = new ArrayList<>(); // 채팅 참가자 목록

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages = new ArrayList<>(); // 메시지 목록

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private int maxParticipants = 1; // 최대 참여 인원
}
