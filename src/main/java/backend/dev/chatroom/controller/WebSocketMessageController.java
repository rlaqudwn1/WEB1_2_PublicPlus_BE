package backend.dev.chatroom.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketMessageController {

    // 1:1 채팅방에 메시지를 보내는 메서드
    @MessageMapping("/chat/send/{chatRoomId}")  // 채팅방 ID를 URL 경로에서 받음
    @SendTo("/topic/messages/{chatRoomId}") // 채팅방 ID별로 브로드캐스트
    public String sendMessage(@DestinationVariable String chatRoomId, String message) {
        return message; // 해당 채팅방의 메시지를 전송
    }
}