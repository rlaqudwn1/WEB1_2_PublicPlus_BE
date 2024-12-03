package backend.dev.googlecalendar.controller;

import backend.dev.googlecalendar.setting.GoogleCalendarService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GoogleCalendarController {

    private final GoogleCalendarService googleCalendarService;

    @Value("${google.redirect.uri}")
    private String redirectUri;

    public GoogleCalendarController(GoogleCalendarService googleCalendarService) {
        this.googleCalendarService = googleCalendarService;
    }

    // 인증 URL을 클라이언트에게 전달
//    @GetMapping("/auth-url")
//    public String getAuthorizationUrl() throws Exception {
//        return googleCalendarService.getAuthorizationUrl(redirectUri);
//    }

    // 인증 후 Google에서 리디렉션된 URL을 처리하고, 인증 코드를 사용해 액세스 토큰을 얻음
//    @GetMapping("/callback")
//    public String handleGoogleCallback(@RequestParam("code") String code) throws Exception {
//        googleCalendarService.getCalendarService(code, redirectUri);
//        return "redirect:/dashboard"; // 인증 후 대시보드 페이지로 리디렉션
//    }
}
