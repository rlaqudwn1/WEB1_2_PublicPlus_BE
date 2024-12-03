package backend.dev.meeting.exception;

public class MeetingBoardNotFoundException extends RuntimeException {
    public MeetingBoardNotFoundException(String message) {
        super(message);
    }
}