package backend.dev.user.oauth;

import backend.dev.user.entity.User;
import backend.dev.user.repository.UserRepository;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
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
public class GoogleService implements OAuth2Service{
    private final UserRepository userRepository;
    @Override
    public User join(OAuth2User oAuth2User, String provider) {
        if(oAuth2User ==null) throw new RuntimeException();
        if(!StringUtils.hasText(provider)) throw new RuntimeException();
        String providerId = oAuth2User.getName(); // provider ID
        return userRepository.findByProviderAndProviderId(provider,providerId).map(user->{
            log.warn("이미 존재하는 사용자입니다 provider = {}, providerId = {}",provider,providerId);
            return user;
        }).orElseGet(()->{
            Map<String, Object> attributes = oAuth2User.getAttributes();

            String name = (String) attributes.get("name");
            String email = (String) attributes.get("email");
            String picture = (String) attributes.get("picture");
            Optional<User> byEmail = userRepository.findByEmail("email");
            if (byEmail.isPresent()) {
                User user = byEmail.get();
                linkOAuth(provider, providerId, user);
                return user;
            }
            String userId = UUID.randomUUID().toString();

            User user = User.builder()
                    .userId(userId)
                    .email(email)
                    .nickname(name)
                    .profile(picture).build();
            linkOAuth(provider,providerId,user);
            userRepository.save(user);
            return user;
        });
    }

    @Override
    public String getProvider() {
        return "google";
    }

}
