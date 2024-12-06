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
    @Query("SELECT p FROM ChatParticipant p JOIN FETCH p.user WHERE p.chatRoom = :chatRoom AND p.user.email = :email")
    Optional<ChatParticipant> findByChatRoomAndUserEmail(@Param("chatRoom") ChatRoom chatRoom, @Param("email") String email);

    @Query("SELECT p FROM ChatParticipant p WHERE p.chatRoom.chatRoomId = :chatRoomId ORDER BY p.participantId ASC")
    Optional<ChatParticipant> findFirstByChatRoom_ChatRoomId(@Param("chatRoomId") Long chatRoomId);

    boolean existsByChatRoomAndUser_Email(ChatRoom chatRoom, String email);

    void deleteAllByChatRoom(ChatRoom chatRoom);
}
