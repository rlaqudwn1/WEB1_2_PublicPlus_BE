package backend.dev.notification.dto;

import backend.dev.notification.entity.Notification;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationDTO {
    private Long notificationId;
    private String title;
    private String message;
    private String userId;
    public static NotificationDTO toDTO(Notification notification) {
        return NotificationDTO.builder()
                .notificationId(notification.getNotification_Id())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .userId(notification.getUser().getId())
                .build();
    }

    public static Notification fromDTO(NotificationDTO dto) {
        return Notification.builder()
                .notification_Id(dto.getNotificationId())
                .title(dto.getTitle())
                .message(dto.getMessage())
                .build();
    }

}
