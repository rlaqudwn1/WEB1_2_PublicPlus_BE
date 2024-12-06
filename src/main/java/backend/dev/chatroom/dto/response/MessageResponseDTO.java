package backend.dev.chatroom.dto.response;

import backend.dev.chatroom.entity.Message;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@Schema(description = "메시지 응답 DTO")
public class MessageResponseDTO {

    @Schema(description = "메시지 ID", example = "1001")
    private Long messageId;

    @Schema(description = "메시지 내용", example = "안녕하세요!")
    private String content;

    @Schema(description = "보낸 사람 닉네임", example = "홍길동")
    private String sender;

    @Schema(description = "채팅방 ID", example = "2002")
    private Long chatRoomId;

    @Schema(description = "메시지 전송 시간", example = "2024-11-28T15:30:00")
    private LocalDateTime sentAt;

    public static MessageResponseDTO fromEntity(Message message) {
        return MessageResponseDTO.builder()
                .messageId(message.getMessageId())
                .content(message.getContent())
                .sender(message.getParticipant().getUser().getNickname()) // 보낸 사람 닉네임
                .chatRoomId(message.getChatRoom().getChatRoomId())
                .sentAt(message.getSentAt())
                .build();
    }
}
