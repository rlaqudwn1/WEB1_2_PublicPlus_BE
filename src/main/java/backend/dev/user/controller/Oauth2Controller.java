package backend.dev.user.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Oauth2Controller {
    @GetMapping("/login/oauth2/code/kakao")
    public String test1() {
        return "카카오로그인";
    }

}
