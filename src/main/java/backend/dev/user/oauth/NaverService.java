package backend.dev.user.oauth;

import backend.dev.user.entity.User;
import backend.dev.user.repository.UserRepository;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class NaverService implements OAuth2Service {
    private final UserRepository userRepository;

    @Override
    public User join(OAuth2User oAuth2User, String provider) {

        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        String providerId = (String) response.get("id");

        return userRepository.findByProviderAndProviderId(provider, providerId)
                .map(user -> {
                    log.warn("이미 존재하는 사용자입니다 provider = {}, providerId = {}", provider, providerId);
                    return user;
                }).orElseGet(() -> {
                    String nickname = (String) response.get("nickname");
                    String email = (String) response.get("email");
                    String profile_image = (String) response.get("profile_image");

                    User findUser = userRepository.findByEmail(email).orElseGet(() -> {
                        User joinUser = makeUser(nickname, email, profile_image);
                        userRepository.save(joinUser);
                        return joinUser;
                    });

                    linkOAuth(provider, providerId, findUser);
                    return findUser;
                });
    }

    @Override
    public String getProvider() {
        return "naver";
    }
}
