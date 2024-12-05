package backend.dev.chatroom.repository;

import backend.dev.chatroom.entity.ChatRoom;
import backend.dev.chatroom.entity.ChatParticipant;
import backend.dev.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("SELECT c FROM ChatRoom c WHERE c.startTime < :cutoffTime")
    List<ChatRoom> findAllByStartTimeBefore(@Param("cutoffTime") LocalDateTime cutoffTime);


    // 두 사용자가 모두 포함된 채팅방 검색
    @Query("SELECT c FROM ChatRoom c JOIN c.chatParticipants p1 JOIN c.chatParticipants p2 " +
            "WHERE p1.user = :user1 AND p2.user = :user2")
    Optional<ChatRoom> findByBothParticipants(@Param("user1") User user1, @Param("user2") User user2);

}
