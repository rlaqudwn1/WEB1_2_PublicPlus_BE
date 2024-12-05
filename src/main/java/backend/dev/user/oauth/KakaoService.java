package backend.dev.user.oauth;

import backend.dev.user.entity.User;
import backend.dev.user.repository.UserRepository;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class KakaoService implements OAuth2Service {
    private final UserRepository userRepository;

    @Override
    public User join(OAuth2User oAuth2User, String provider) {

        String providerId = oAuth2User.getName();

        return findUserByProviderAndProviderId(provider, providerId).map(user -> {
            log.warn("이미 존재하는 사용자입니다 provider = {}, providerId = {}", provider, providerId);
            return user;

        }).orElseGet(() -> {
            Map<String, Object> attributes = oAuth2User.getAttributes();

            Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

            String nickname = (String) properties.get("nickname");
            String profileImage = (String) properties.get("profile_image");
            String email = (String) kakaoAccount.get("email");

            User findUser = userRepository.findByEmail(email).orElseGet(() -> {
                User joinUser = makeUser(nickname, email, profileImage);
                userRepository.save(joinUser);
                return joinUser;
            });
            linkOAuth(provider, providerId, findUser);
            return findUser;
        });
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Transactional(readOnly = true)
    public Optional<User> findUserByProviderAndProviderId(String provider, String providerId) {
        return userRepository.findByProviderAndProviderId(provider, providerId);
    }
}
