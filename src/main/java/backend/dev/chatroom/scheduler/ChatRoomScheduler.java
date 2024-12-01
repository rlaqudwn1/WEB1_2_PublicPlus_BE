package backend.dev.chatroom.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ChatRoomScheduler {

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정 실행
    public void cleanUpOldChatRooms() {
    }
}