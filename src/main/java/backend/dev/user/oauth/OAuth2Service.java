package backend.dev.user.oauth;

import backend.dev.user.entity.Oauth;
import backend.dev.user.entity.User;
import java.util.UUID;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OAuth2Service {
    User join(OAuth2User oAuth2User, String provider);

    //     void linkOauth();
    String getProvider();

    default void linkOAuth(String provider, String providerId, User user) {
        Oauth oauth = Oauth.builder().provider(provider).providerId(providerId).build();
        user.addOauthList(oauth);
    }

    default User makeUser(String nickname, String email, String profile_image) {
        String userId = UUID.randomUUID().toString();
        return User.builder()
                .nickname(nickname)
                .userId(userId)
                .email(email)
                .profile(profile_image)
                .build();
    }


}
