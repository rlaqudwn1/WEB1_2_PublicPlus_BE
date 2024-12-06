package backend.dev.chatroom.controller;

import backend.dev.chatroom.dto.request.MessageRequestDTO;
import backend.dev.chatroom.dto.response.MessageResponseDTO;
import backend.dev.chatroom.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.stereotype.Controller;


@Controller
public class WebSocketMessageController {

    private final MessageService messageService;

    // 생성자를 통해 MessageService 주입
    public WebSocketMessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    // 메시지 저장 및 브로드캐스트
    @MessageMapping("/chat/send/{chatRoomId}")
    @SendTo("/topic/chatroom/{chatRoomId}")
    public MessageResponseDTO sendMessage(
            @DestinationVariable Long chatRoomId,
            @Valid MessageRequestDTO requestDTO
    ) {
        System.out.println("받은 메시지: " + requestDTO.getContent());
        System.out.println("받은 ChatRoomId: " + requestDTO.getChatRoomId());
        return messageService.sendMessage(requestDTO);
    }
}