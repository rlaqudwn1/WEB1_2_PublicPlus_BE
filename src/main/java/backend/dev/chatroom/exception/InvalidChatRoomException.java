package backend.dev.chatroom.exception;

public class InvalidChatRoomException extends RuntimeException {
    public InvalidChatRoomException(String message) {
        super(message);
    }
}
