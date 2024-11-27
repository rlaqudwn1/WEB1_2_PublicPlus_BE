package backend.dev.user.controller;

import backend.dev.googlecalendar.service.CalenderService;
import backend.dev.setting.exception.ErrorCode;
import backend.dev.setting.exception.PublicPlusCustomException;
import backend.dev.user.DTO.ChangePasswordDTO;
import backend.dev.user.DTO.UserChangeInfoDTO;
import backend.dev.user.DTO.UserDTO;
import backend.dev.user.DTO.UserJoinDTO;
import backend.dev.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")  // React 앱의 URL
public class UserController {

    private final UserService userService;
    private final CalenderService calenderService;

    @PostMapping("/join")
    public ResponseEntity<UserDTO> join(@RequestBody UserJoinDTO userJoinDTO) {
        if(userService.findUserByEmail(userJoinDTO.email()).isPresent()) throw new PublicPlusCustomException(ErrorCode.DUPLICATE_EMAIL);
        if(!userJoinDTO.isSame()) throw new PublicPlusCustomException(ErrorCode.NOT_MATCH_PASSWORD);
        UserDTO join = userService.join(userJoinDTO);
        return ResponseEntity.ok(join);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> findMyInformation(@PathVariable String userId) {
        UserDTO myInformation = userService.findMyInformation(userId);
        return ResponseEntity.ok(myInformation);
    }

    @PostMapping("/password/{userId}")
    public ResponseEntity<Map<String, String>> changePassword(@PathVariable String userId, @RequestBody
    ChangePasswordDTO changePasswordDTO) {
        userService.changePassword(userId, changePasswordDTO);
        Map<String, String> responseMap = Map.of("message", "암호 변경 완료");
        return ResponseEntity.status(200).body(responseMap);
    }

    @PatchMapping("/nickname/{userId}")
    public ResponseEntity<Map<String, String>> updateNickname(@PathVariable String userId, @RequestBody UserChangeInfoDTO userChangeInfoDTO) {
        userService.changeNickname(userId,userChangeInfoDTO);
        Map<String, String> responseMap =Map.of("message", "닉네임 수정 완료");
        return ResponseEntity.status(200).body(responseMap);
    }
    @PatchMapping("/description/{userId}")
    public ResponseEntity<Map<String, String>> updateDescription(@PathVariable String userId, @RequestBody UserChangeInfoDTO userChangeInfoDTO) {
        userService.changeDescription(userId,userChangeInfoDTO);
        Map<String, String> responseMap =Map.of("message", "소개글 수정 완료");
        return ResponseEntity.status(200).body(responseMap);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        Map<String, String> responseMap =Map.of("message", "회원 탈퇴 완료");
        return ResponseEntity.status(200).body(responseMap);

    }

    @PostMapping(value = "profile/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> changeProfile(
            @PathVariable String userId, MultipartFile multipartFile
    ) throws IOException {
        userService.changeProfile(userId, multipartFile);
        Map<String, String> responseMap = Map.of("message", "프로필 사진 변경 완료");

        return ResponseEntity.status(200).body(responseMap);
    }



}
