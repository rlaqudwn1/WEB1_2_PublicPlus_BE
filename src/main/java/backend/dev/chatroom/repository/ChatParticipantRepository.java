package backend.dev.chatroom.repository;

import backend.dev.chatroom.entity.ChatRoom;
import backend.dev.chatroom.entity.ChatParticipant;
import backend.dev.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {

    // user_id로 참가자 찾기
    @Query("SELECT p FROM ChatParticipant p WHERE p.chatRoom = :chatRoom AND p.user.userId = :userId")
    Optional<ChatParticipant> findByChatRoomAndUserId(@Param("chatRoom") ChatRoom chatRoom, @Param("userId") String userId);

    boolean existsByChatRoomAndUser(ChatRoom chatRoom, User user);

    void deleteAllByChatRoom(ChatRoom chatRoom);
}