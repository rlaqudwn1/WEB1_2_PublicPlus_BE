package backend.dev.notification.controller;

import backend.dev.notification.repository.TokenRepository;
import backend.dev.notification.service.FCMService;
import backend.dev.setting.exception.ErrorCode;
import backend.dev.setting.exception.PublicPlusCustomException;
import backend.dev.user.DTO.users.UserLoginDTO;
import backend.dev.user.entity.User;
import backend.dev.user.repository.UserRepository;
import backend.dev.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/fcm")
@RequiredArgsConstructor
public class FCMController {

    private final UserRepository userRepository;
    private final TokenRepository fcmTokenRepository;
    private final UserService userService;
    private final FCMService fcmService;

    @PostMapping
    public ResponseEntity<?> handleFcmToken(@RequestBody UserLoginDTO userLoginDTO){
        log.info("??");
        User user = userRepository.findByEmail(userLoginDTO.email())
                .orElseThrow(() -> new PublicPlusCustomException(ErrorCode.NOT_FOUND_USER));
        String fcmToken = user.getFcmToken();

        return ResponseEntity.ok().build();
    }

//    @PostMapping("/updateFcmToken")
//    public ResponseEntity<UserDTO> updateFcmToken(@RequestParam String fcmToken, @RequestBody UserDTO userDTO) {
//        if (fcmToken != null && !fcmToken.isEmpty()) {
//            // FCM 토큰 유효성 검증
//            if (!fcmService.verifyToken(fcmToken)) {
//                throw new PublicPlusCustomException(ErrorCode.INVALID_FCM_TOKEN);
//            }
//
//            // 유효한 경우 FCM 토큰 업데이트
//            userService.updateFcmToken(userDTO.getEmail(), fcmToken);
//            return ResponseEntity.ok(userDTO);
//        } else {
//            throw new PublicPlusCustomException(ErrorCode.INVALID_FCM_TOKEN);
//        }
//    }


}
