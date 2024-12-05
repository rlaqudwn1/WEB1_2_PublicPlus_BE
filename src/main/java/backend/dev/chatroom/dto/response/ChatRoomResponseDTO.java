package backend.dev.chatroom.dto.response;

import backend.dev.chatroom.entity.ChatRoom;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "채팅방 응답 DTO")
public class ChatRoomResponseDTO {

    @Schema(description = "채팅방 ID", example = "101")
    private Long chatRoomId;

    @Schema(description = "채팅방 이름", example = "친구들과의 모임")
    private String chatRoomName;

    @Schema(description = "채팅방 타입 (GROUP 또는 PRIVATE)", example = "GROUP")
    private String chatRoomType;

    @Schema(description = "채팅방 생성 시간", example = "2024-11-28T10:15:30")
    private LocalDateTime createdAt;

    public ChatRoomResponseDTO(Long chatRoomId, String chatRoomName, String chatRoomType, LocalDateTime createdAt) {
        this.chatRoomId = chatRoomId;
        this.chatRoomName = chatRoomName;
        this.chatRoomType = chatRoomType;
        this.createdAt = createdAt;
    }

    public static ChatRoomResponseDTO fromEntity(ChatRoom chatRoom) {
        if (chatRoom == null) {
            throw new IllegalArgumentException("ChatRoom 엔티티가 null입니다.");
        }
        return new ChatRoomResponseDTO(
                chatRoom.getChatRoomId(),
                chatRoom.getName() != null ? chatRoom.getName() : "Unknown",
                chatRoom.getType() != null ? chatRoom.getType().toString() : "UNKNOWN",
                chatRoom.getCreatedAt() != null ? chatRoom.getCreatedAt() : LocalDateTime.now()
        );
    }
}
