package backend.dev.user.controller;

import backend.dev.googlecalendar.service.CalenderService;
import backend.dev.setting.exception.ErrorCode;
import backend.dev.setting.exception.PublicPlusCustomException;
import backend.dev.setting.jwt.JwtToken;
import backend.dev.user.DTO.ChangePasswordDTO;
import backend.dev.user.DTO.UserChangeInfoDTO;
import backend.dev.user.DTO.UserDTO;
import backend.dev.user.DTO.UserJoinDTO;
import backend.dev.user.DTO.UserLoginDTO;
import backend.dev.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")  // React 앱의 URL
@Tag(name ="사용자 컨트롤러",description = "사용자 관련 처리를 하는 컨트롤러입니다(회원가입,로그인,로그아웃,토큰 등등)")
public class UserController {

    private final UserService userService;
    private final CalenderService calenderService;
    @Operation(summary = "회원가입", description = "이메일, 암호, 닉네임을 입력하여 회원가입을 합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "400", description = "암호가 일치하지 않습니다")
    })
    @PostMapping("/join")
    public ResponseEntity<Void> join(@RequestBody UserJoinDTO userJoinDTO) {
        if (!userJoinDTO.isSame()) {
            throw new PublicPlusCustomException(ErrorCode.NOT_MATCH_PASSWORD);
        }
        userService.join(userJoinDTO);
        return ResponseEntity.noContent().build();
    }
    @Operation(summary = "로그인", description = "이메일, 암호를 입력하여 로그인합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상 반환"),
            @ApiResponse(responseCode = "400", description = "이메일이나 암호가 맞을 경우")
    })
    @PostMapping("/login")
    public ResponseEntity<JwtToken> login(@RequestBody UserLoginDTO userLoginDTO) {
        JwtToken login = userService.login(userLoginDTO);
        return ResponseEntity.ok(login);
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout() {
        userService.logout();
        return ResponseEntity.noContent().build();
    }
    @Operation(summary = "토큰 재발급", description = "HTTP헤더에 담긴 refresh토큰을 이용해 access토큰을 재발급합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상 반환"),
            @ApiResponse(responseCode = "400", description = "토큰 만료, refresh토큰이 아닌경우")
    })
    @PostMapping("/refresh/header")
    public ResponseEntity<JwtToken> resignAccessTokenByHeader(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken) {
        return ResponseEntity.ok(userService.resignAccessTokenByHeader(bearerToken));
    }
    @Operation(summary = "토큰 재발급", description = "(현재미구현)쿠키에 담긴 refresh토큰을 이용해 access토큰을 재발급합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상 반환"),
            @ApiResponse(responseCode = "400", description = "토큰 만료, refresh토큰이 아닌경우")
    })
    @PostMapping("/refresh/cookie")
    public ResponseEntity<JwtToken> resignAccessTokenByCookie(@CookieValue("refresh_token") String token) {
        return ResponseEntity.ok(userService.resignAccessTokenByCookie(token));
    }
    @Operation(summary = "회원 정보 조회", description = "UserId를 이용해 회원정보를 조회합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상 반환")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> findMyInformation(@PathVariable String userId) {
        UserDTO myInformation = userService.findMyInformation(userId);
        return ResponseEntity.ok(myInformation);
    }
    @Operation(summary = "비밀번호 변경", description = "UserId를 이용해 비밀번호를 변경합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상 반환"),
            @ApiResponse(responseCode = "400", description = "암호재확인 절차를 통과하지 못했을 경우")
    })
    @PostMapping("/password/{userId}")
    public ResponseEntity<Map<String, String>> changePassword(@PathVariable String userId, @RequestBody
    ChangePasswordDTO changePasswordDTO) {
        userService.changePassword(userId, changePasswordDTO);
        Map<String, String> responseMap = Map.of("message", "암호 변경 완료");
        return ResponseEntity.status(200).body(responseMap);
    }
    @Operation(summary = "닉네임 변경", description = "UserId를 이용해 닉네임을 변경합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상 반환"),
            @ApiResponse(responseCode = "400", description = "닉네임 변경규칙을 지키지 않았을 경우")
    })
    @PatchMapping("/nickname/{userId}")
    public ResponseEntity<Map<String, String>> updateNickname(@PathVariable String userId,
                                                              @RequestBody UserChangeInfoDTO userChangeInfoDTO) {
        userService.changeNickname(userId, userChangeInfoDTO);
        Map<String, String> responseMap = Map.of("message", "닉네임 수정 완료");
        return ResponseEntity.status(200).body(responseMap);
    }
    @Operation(summary = "소개글 변경", description = "UserId를 이용해 소개글을 변경합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상 반환")
    })
    @PatchMapping("/description/{userId}")
    public ResponseEntity<Map<String, String>> updateDescription(@PathVariable String userId,
                                                                 @RequestBody UserChangeInfoDTO userChangeInfoDTO) {
        userService.changeDescription(userId, userChangeInfoDTO);
        Map<String, String> responseMap = Map.of("message", "소개글 수정 완료");
        return ResponseEntity.status(200).body(responseMap);
    }
    @Operation(summary = "회원 탈퇴", description = "UserId를 이용해 회원정보를 삭제합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상 반환")
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        Map<String, String> responseMap = Map.of("message", "회원 탈퇴 완료");
        return ResponseEntity.status(200).body(responseMap);

    }
    @Operation(summary = "프로필 사진 변경", description = "UserId를 이용해 프로필 사진을 변경합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상 반환"),
            @ApiResponse(responseCode = "400", description = "이미지 파일이 아니거나, 파일을 넣지 않았을 경우"),
            @ApiResponse(responseCode = "500", description = "파일경로 생성이 실패할 경우 - 서버 문제")
    })
    @PostMapping(value = "profile/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> changeProfile(
            @PathVariable String userId, MultipartFile multipartFile
    ) throws IOException {
        userService.changeProfile(userId, multipartFile);
        Map<String, String> responseMap = Map.of("message", "프로필 사진 변경 완료");

        return ResponseEntity.status(200).body(responseMap);
    }
}
