package backend.dev.chatroom.scheduler;

import backend.dev.chatroom.entity.ChatRoom;
import backend.dev.chatroom.repository.ChatParticipantRepository;
import backend.dev.chatroom.repository.ChatRoomRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Component
public class ChatRoomScheduler {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;

    public ChatRoomScheduler(ChatRoomRepository chatRoomRepository, ChatParticipantRepository chatParticipantRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatParticipantRepository = chatParticipantRepository;
    }

    @Scheduled(cron = "0 0 * * * ?") // 매 1분마다 실행
    @Transactional
    public void cleanUpOldChatRooms() {
        LocalDateTime cutoffTime = LocalDateTime.now().minusHours(48);
        //LocalDateTime cutoffTime = LocalDateTime.now().minusMinutes(1);

        List<ChatRoom> oldRooms = chatRoomRepository.findAllByStartTimeBefore(cutoffTime);

        for (ChatRoom room : oldRooms) {
            System.out.println("삭제 대상 방: " + room.getChatRoomId() + ", 시작 시간: " + room.getStartTime());
            chatParticipantRepository.deleteAllByChatRoom(room);
            chatRoomRepository.delete(room);
        }
    }
}