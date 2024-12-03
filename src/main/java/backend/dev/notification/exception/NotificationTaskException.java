package backend.dev.notification.exception;

import lombok.Getter;

@Getter
public class NotificationTaskException extends RuntimeException {
    private int code;
    public NotificationTaskException(String message, int code) {
        super(message);
        this.code = code;
    }
}
