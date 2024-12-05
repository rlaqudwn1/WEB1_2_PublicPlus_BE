package backend.dev.user.oauth;

import backend.dev.user.entity.User;
import backend.dev.user.repository.UserRepository;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class GoogleService implements OAuth2Service {
    private final UserRepository userRepository;

    @Override
    public User join(OAuth2User oAuth2User, String provider) {
        if (oAuth2User == null) {
            throw new RuntimeException();
        }
        if (!StringUtils.hasText(provider)) {
            throw new RuntimeException();
        }
        String providerId = oAuth2User.getName();
        return userRepository.findByProviderAndProviderId(provider, providerId).map(user -> {
            log.warn("이미 존재하는 사용자입니다 provider = {}, providerId = {}", provider, providerId);
            return user;
        }).orElseGet(() -> {
            Map<String, Object> attributes = oAuth2User.getAttributes();

            String nickname = (String) attributes.get("name");
            String email = (String) attributes.get("email");
            String profile_image = (String) attributes.get("picture");

            User findUser = userRepository.findByEmail("email").orElseGet(() -> {
                        User joinUser = makeUser(nickname, email, profile_image);
                        userRepository.save(joinUser);
                        return joinUser;
                    }
            );

            linkOAuth(provider, providerId, findUser);
            return findUser;
        });
    }

    @Override
    public String getProvider() {
        return "google";
    }

}
