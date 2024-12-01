package backend.dev.chatroom.repository;

import backend.dev.chatroom.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByChatRoom_ChatRoomId(Long chatRoomId);
}