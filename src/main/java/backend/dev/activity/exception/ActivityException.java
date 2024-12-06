package backend.dev.activity.exception;

public enum ActivityException {
    ACTIVITY_NOT_FOUND(404,"모임을 찾을 수 없습니다"),
    ACTIVITY_NOT_DELETED(400,"모임이 삭제되지 않았습니다"),
    ACTIVITY_NOT_MODIFIED(400,"모임이 변경되지 않았습니다"),
    ACTIVITY_FULL(400, "모임에 더 참가할 수 없습니다."),
    ACTIVITY_NOT_JOINED(400,"모임에 참가하고 있지 않습니다")
    ;

    private final int code;
    private final String message;

    ActivityException(int code, String message) {
        this.code = code;
        this.message = message;
    }
    public  ActivityTaskException getException(){
        return new ActivityTaskException(this.code,this.message);
    }
}
