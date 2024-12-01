package backend.dev.chatroom.repository;

import backend.dev.chatroom.entity.ChatRoom;
import backend.dev.chatroom.entity.ChatParticipant;
import backend.dev.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    // 기존 findByParticipantsContaining 수정
    @Query("SELECT c FROM ChatRoom c JOIN c.chatParticipants p WHERE p.user = :user")
    Optional<ChatRoom> findByParticipantsContaining(@Param("user") User user);

    // 두 사용자가 모두 포함된 채팅방 검색
    @Query("SELECT c FROM ChatRoom c JOIN c.chatParticipants p1 JOIN c.chatParticipants p2 " +
            "WHERE p1.user = :user1 AND p2.user = :user2")
    Optional<ChatRoom> findByBothParticipants(@Param("user1") User user1, @Param("user2") User user2);


}