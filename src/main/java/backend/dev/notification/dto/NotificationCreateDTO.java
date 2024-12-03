package backend.dev.notification.dto;

import lombok.Data;

@Data
public class NotificationCreateDTO {
    //email을 통해서 서버에서 자동으로 조회할 수 있도록 처리함..
    private String title;
    private String body;
    private String email;
}
