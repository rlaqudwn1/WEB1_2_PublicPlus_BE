package backend.dev.user.oauth;

import backend.dev.user.entity.User;
import backend.dev.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class GoogleService implements OAuth2Service{
    private final UserRepository userRepository;
    @Override
    public User join(OAuth2User oAuth2User, String provider) {
        return null;
    }
}
