package backend.dev.notification.controller;

import backend.dev.notification.dto.NotificationDTO;
import backend.dev.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification API", description = "알림 관리 API")
public class NotificationController {
    private final PushNotificationController pushNotificationController;
    private final NotificationService notificationService;

    @Operation(summary = "알림 생성", description = "새로운 알림을 생성합니다(알림 생성 테스트용).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "알림 생성 성공"),
            @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없습니다"),
    })
    @PostMapping
    public NotificationDTO createNotification(@RequestBody NotificationDTO dto) {
        pushNotificationController.sendPushNotification(dto);
        return notificationService.createNotification(dto);
    }
    @Operation(summary = "모든 유저에 대한 알림 생성", description = "전체 유저에게 대한 알림 생성.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "알림 생성 성공"),
            @ApiResponse(responseCode = "400", description = "알림 에러"),
    })
    @PostMapping("/user")
    public ResponseEntity<NotificationDTO> toUserNotification(@RequestBody NotificationDTO dto) {
        return ResponseEntity.ok(notificationService.toUserNotification(dto));
    }

    @Operation(summary = "유저의 알림을 조회합니다", description = "현재 사용자에 대한 모든 알림을 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "알림 조회 성공"),
            @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없습니다"),
    })
    @GetMapping
    public List<NotificationDTO> getAllNotifications() {
        return notificationService.getAllNotificationsByUser();
    }

    @Operation(summary = "알림 조회", description = "Id로 알림을 조회해 읽음처리 합니다. ")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "알림 조회 성공"),
            @ApiResponse(responseCode = "404", description = "알림을 찾을 수 없음"),
    })
    @GetMapping("/{id}")
    public NotificationDTO getNotification(@PathVariable Long id) {
        return notificationService.getNotificationById(id);
    }

    @Operation(summary = "특정 알림 삭제", description = "ID로 특정 알림을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "알림 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "알림을 찾을 수 없음"),
            @ApiResponse(responseCode = "400", description = "알림이 삭제되지 않았습니다")
    })
    @DeleteMapping("/{id}")
    public void deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
    }

    @DeleteMapping("/all")
    public void deleteAllNotifications() {
        notificationService.deleteAllNotifications();
    }
}
