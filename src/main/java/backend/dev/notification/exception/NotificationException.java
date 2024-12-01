package backend.dev.notification.exception;

import lombok.Getter;

@Getter
public enum NotificationException {
    NOT_FOUND_FCM_TOKEN(404,"FCM토큰을 찾을 수 없습니다"),;
    private int code;
    private String message;
    NotificationException(final int code, final String message) {
        this.code = code;
        this.message = message;
    }
    public NotificationTaskException getNotificationException() {
        return new NotificationTaskException(message, code);
    }
}
