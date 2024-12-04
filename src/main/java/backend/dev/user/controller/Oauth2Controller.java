package backend.dev.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/oauth2")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "OAuth2Controller", description = "소셜 로그인 기능을 이용해 로그인합니다(스웨거로는 테스트 불가)")
public class Oauth2Controller {
    @Operation(summary = "소셜 로그인", description = "OAUTH2.0을 통해 로그인합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상 반환", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n  \"bearer\": \"Bearer\",\n  \"access_token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c\",\n  \"refresh_token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c\",\n  \"userId\": \"sda1fsd3-ds6au-fs3fds5\"}"))),
//            @ApiResponse(responseCode = "400", description = "입력한 이메일이나 암호가 회원 확인 로직을 통과하지 못했을 경우",
//                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErrorResponse.class),examples = @ExampleObject(value = "{\n  \"httpStatus\": \"BAD_REQUEST\",\n  \"message\": \"암호가 일치하지 않습니다\"\n}")))
    })
    @GetMapping("/{provider}")
    public void OauthLogin(@PathVariable String provider, HttpServletResponse httpServletResponse) throws IOException {
        log.info("{} 소셜로그인으로 리다이렉트 합니다", provider);
        String redirectUrl = "/oauth2/authorization/" + provider;
        httpServletResponse.sendRedirect(redirectUrl);
    }
}
