package backend.dev.user.controller;

import backend.dev.user.oauth.GoogleService;
import backend.dev.user.oauth.KakaoService;
import backend.dev.user.oauth.NaverService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/oauth2/")
@RequiredArgsConstructor
public class Oauth2Controller {

    @GetMapping("/kakao")
    public String kakaoOauth() {
        return "카카오 로그인";
    }
    @GetMapping("/naver")
    public String naverOauth() {

        return "네이버 로그인";
    }
    @GetMapping("/google")
    public String googleOauth() {
        return "구글 로그인";
    }
}
