package backend.dev.notification.service;

import backend.dev.notification.dto.NotificationDTO;
import com.google.firebase.messaging.*;

import java.util.Collections;

public class NotificationService {

    public void sendTopicSubscriptionNotification(NotificationDTO notificationDTO) throws FirebaseMessagingException {
        // 토픽 구독 설정
        TopicManagementResponse response = FirebaseMessaging.getInstance()
                .subscribeToTopic(Collections.singletonList(notificationDTO.getUserId()), "User");
        Notification notification = Notification.builder()
                .setTitle(notificationDTO.getTitle())
                .setBody(notificationDTO.getMessage())
                .build();
        Message message =Message.builder()
                .setToken("token")
                .setNotification(notification)
                .setTopic("topic")
                .build();
        // 만약 chat 구독자일 경우 chat_ 에 구독 아이디를 첨가해 고유한 topic을 생성한다
//        Message message = Message.builder()
//                .setToken("token") // 특정 FCM 토큰을 발송할 경우
//                .setNotification(notification)
//                .setTopic("chat_" + topic.getTopicId())  // "chat_" + topic_id 형태로 고유한 토픽 이름 생성
//                .build();
    }

    public NotificationDTO sendNotification(NotificationDTO notificationDTO) throws FirebaseMessagingException {
        // 알림 빌드 제목과 내용을 빌드
        Notification notification = Notification.builder()
                .setTitle(notificationDTO.getTitle())
                .setBody(notificationDTO.getMessage())
                .build();
        // 메세지 빌드 토큰과 알림을 통해서 빌드
        Message message = Message.builder()
                .setToken(notificationDTO.getUserId())
                .setNotification(notification).build();
        try{
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("푸시 알림 전송 성공: "+response);
            return notificationDTO;
        } catch (FirebaseMessagingException e) {
            System.out.println("v푸시 알림 전송 실패");
            throw new RuntimeException(e);
        }
    }

}
