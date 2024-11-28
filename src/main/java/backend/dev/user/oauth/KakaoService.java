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
public class KakaoService implements OAuth2Service{
    private final UserRepository userRepository;

    @Override
    public User join(OAuth2User oAuth2User, String provider) {
        if(oAuth2User ==null) throw new RuntimeException();
        if(!StringUtils.hasText(provider)) throw new RuntimeException();
        String providerId = oAuth2User.getName(); // provider ID
        return findUserByProviderAndProviderId(provider,providerId).map(user->{
            log.warn("이미 존재하는 사용자입니다 provider = {}, providerId = {}",provider,providerId);
            return user;
        }).orElseGet(()->{
            Map<String, Object> attributes = oAuth2User.getAttributes();
            for (String s : attributes.keySet()) {
                log.info("attribute 내용물 = {}",s);
            }
            Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
            Map<String, Object> kakaoAccount =(Map<String, Object>) attributes.get("kakao_account");

            String nickname = (String) properties.get("nickname");
            String profileImage = (String) properties.get("profile_image");
            String email = (String) kakaoAccount.get("email");
            return userRepository.findByEmail(email).map(user->{
                linkOAuth(provider, providerId, user);
                return user;
            }).orElseGet(()->{
                String userId = UUID.randomUUID().toString();
                User user = User.builder()
                        .userId(userId)
                        .email(email)
                        .nickname(nickname)
                        .profile(profileImage).build();
                linkOAuth(provider,providerId,user);
                userRepository.save(user);
                return user;
            });
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
