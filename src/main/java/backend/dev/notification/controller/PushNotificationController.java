package backend.dev.notification.controller;

import backend.dev.notification.dto.NotificationCreateDTO;
import backend.dev.notification.dto.NotificationDTO;
import backend.dev.notification.service.PushNotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/push")
@RequiredArgsConstructor
public class PushNotificationController {

    private final PushNotificationService pushNotificationService;

      @Operation(summary = "푸시알림 테스트입니다 ", description = " 로그인한 유저에게 푸시알람 보내기 테스트입니다")
        @ApiResponses(value = {
              @ApiResponse(responseCode = "", description = ""),
              @ApiResponse(responseCode = "", description = "")
        })
    @PostMapping("/send")
    public ResponseEntity<String> sendPushNotification(@RequestBody NotificationDTO request) {
        try {
            String result = pushNotificationService.sendPushNotification(request);
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            e.printStackTrace(); // 예외 디버깅
            return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage());
        }
    }
}
