package backend.dev.user.oauth;

import backend.dev.user.entity.User;
import backend.dev.user.repository.UserRepository;
import java.util.Map;
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
public class NaverService implements OAuth2Service{
    private final UserRepository userRepository;
    @Override
    public User join(OAuth2User oAuth2User, String provider) {
        if(oAuth2User ==null) throw new RuntimeException();
        if(!StringUtils.hasText(provider)) throw new RuntimeException();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String,Object> response = (Map<String, Object>)attributes.get("response");
        String providerId = (String)response.get("id");// provider ID
        return userRepository.findByProviderAndProviderId(provider, providerId)
                .map(user -> {
                    linkOAuth(provider, providerId,user);
                    log.warn("이미 존재하는 사용자입니다 provider = {}, providerId = {}", provider, providerId);

                    return user;
                })
                .orElseGet(()->
                        {

                            String nickname = (String) response.get("nickname");
                            String email = (String) response.get("email");
                            String profile_image = (String) response.get("profile_image");
                            String userId = UUID.randomUUID().toString();
                            User findUser = userRepository.findByEmail(email).orElseGet(() ->
                            {
                                User user = User.builder()
                                        .nickname(nickname)
                                        .userId(userId)
                                        .email(email)
                                        .profile(profile_image)
                                        .build();
                                userRepository.save(user);
                                return user;
                            });
                            linkOAuth(provider,providerId,findUser);
                            return findUser;
                        }
                );
    }

    @Override
    public String getProvider() {
        return "naver";
    }
}
