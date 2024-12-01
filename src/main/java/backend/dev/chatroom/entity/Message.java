package backend.dev.chatroom.entity;

import backend.dev.chatroom.entity.ChatParticipant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    @ManyToOne
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "participant_id", nullable = false)
    private ChatParticipant participant;

    private String content;
    private LocalDateTime sentAt;
}