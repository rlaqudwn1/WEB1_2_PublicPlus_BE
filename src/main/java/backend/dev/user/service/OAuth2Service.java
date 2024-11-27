package backend.dev.user.service;

import backend.dev.user.entity.Oauth;
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
public class OAuth2Service {
    private final UserRepository userRepository;

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
            Map<String, Object> kakao_acount =(Map<String, Object>) attributes.get("kakao_account");
            for (String s : properties.keySet()) {
                log.info("properties 내용물 = {}",s);
            }
            for (String s : kakao_acount.keySet()) {
                log.info("kakao_acount 내용물 = {}",s);
            }
            String nickname = (String) properties.get("nickname");
            String profileImage = (String) properties.get("profile_image");
            String email = (String) kakao_acount.get("email");
            Optional<User> byEmail = userRepository.findByEmail(email);
            if (byEmail.isPresent()) {
                User user = byEmail.get();
                Oauth oauth = Oauth.builder().provider(provider).providerId(providerId).build();
                user.addOauthList(oauth);
                return user;
            }
            String userId = UUID.randomUUID().toString();
            Oauth oauth = Oauth.builder().provider(provider).providerId(providerId).build();
            User user = User.builder()
                    .userId(userId)
                    .email(email)
                    .nickname(nickname)
                    .profile(profileImage).build();
            user.addOauthList(oauth);
            userRepository.save(user);
            return user;
        });
    }
    @Transactional(readOnly = true)
    public Optional<User> findUserByProviderAndProviderId(String provider, String providerId) {
        return userRepository.findByProviderAndProviderId(provider, providerId);
    }
}
