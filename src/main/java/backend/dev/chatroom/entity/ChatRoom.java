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

    private boolean isDeleted = false; // 삭제 여부

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatParticipant> chatParticipants = new ArrayList<>();

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "kicked_users", joinColumns = @JoinColumn(name = "chat_room_id"))
    @Column(name = "user_id")
    private List<String> kickedUsers = new ArrayList<>(); // 강퇴된 사용자 목록

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}