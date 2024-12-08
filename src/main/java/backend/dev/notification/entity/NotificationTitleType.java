package backend.dev.notification.entity;

import lombok.Getter;

@Getter
public enum NotificationTitleType {
    CHAT_NEW_MESSAGE("새로운 채팅 메시지가 도착했습니다"),
    ACTIVITY_REMINDER("모임 시작 30분 전입니다"),
    ACTIVITY_INVITED("모임에 초대되었습니다"),
    ACTIVITY_PARTICIPATE(" 님이 모임에 참가하셨습니다."),
    ACTIVITY_QUIT(" 님이 모임에 퇴장하셨습니다."),
    ;
    private final String message;

    NotificationTitleType(String message) {
        this.message = message;
    }
}
