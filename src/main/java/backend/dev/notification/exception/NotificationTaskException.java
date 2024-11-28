package backend.dev.notification.exception;

public class NotificationTaskException extends RuntimeException {
    public NotificationTaskException(String message, int code) {
        super(message);
    }
}
