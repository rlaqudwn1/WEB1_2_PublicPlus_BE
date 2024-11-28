package backend.dev.user.oauth;

import backend.dev.user.entity.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OAuth2Service {
     User join(OAuth2User oAuth2User, String provider);

//     void linkOauth();
}
