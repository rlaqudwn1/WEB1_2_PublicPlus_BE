package backend.dev.notification.dto;

import backend.dev.notification.entity.Notification;
import backend.dev.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationDTO {
    private Long notificationId;
    @Schema(description = "알림 제목입니다")
    private String title;
    @Schema(description = "알림 메시지입니다")
    private String message;

    public static NotificationDTO toDTO(Notification notification) {
        return NotificationDTO.builder()
                .notificationId(notification.getNotification_Id())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .build();
    }

    public static Notification fromDTO(NotificationDTO dto, User user) {
        return Notification.builder()
                .notification_Id(dto.getNotificationId())
                .title(dto.getTitle())
                .message(dto.getMessage())
                .user(user)
                .build();
    }

}
