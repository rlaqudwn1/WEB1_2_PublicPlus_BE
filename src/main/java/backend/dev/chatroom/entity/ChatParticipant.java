package backend.dev.chatroom.entity;

import backend.dev.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long participantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false) // 새 필드 추가
    private String email;

    @Enumerated(EnumType.STRING)
    private ChatParticipantRole role; // 역할

    @Column(nullable = false)
    private boolean host;

    @PrePersist
    public void setEmailFromUser() {
        this.email = this.user.getEmail(); // `user` 엔티티로부터 이메일 설정
    }

    // 추가된 메서드: setParticipantId
    /**
     * 참가자 ID를 설정합니다.
     * @param participantId 참가자 ID
     */
    public void setParticipantId(Long participantId) {
        this.participantId = participantId;
    }

    public void setHost(boolean host) {
        this.host = host;
    }

    // 수정된 메서드: host 값 반환 메서드
    public boolean isHost() {
        return this.host;
    }

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
