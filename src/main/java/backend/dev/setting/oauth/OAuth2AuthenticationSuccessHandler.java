package backend.dev.setting.oauth;

import backend.dev.setting.jwt.JwtAuthenticationProvider;
import backend.dev.setting.jwt.JwtToken;
import backend.dev.user.entity.User;
import backend.dev.user.oauth.OAuth2Service;
import backend.dev.user.oauth.OAuth2ServiceRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
@Slf4j
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final OAuth2ServiceRegistry oAuth2ServiceRegistry;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        log.info("성공 : {}",authentication.getPrincipal());
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            String provider = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();

            OAuth2Service service = oAuth2ServiceRegistry.getService(provider);
            User join = service.join(oAuth2User, provider);

            JwtToken jwtToken = jwtAuthenticationProvider.makeToken(join.getId());
            String tokenString = objectMapper.writeValueAsString(jwtToken);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(tokenString);
        }else {
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }
}
