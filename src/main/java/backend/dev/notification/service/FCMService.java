package backend.dev.notification.service;

import backend.dev.notification.entity.FCMToken;
import backend.dev.notification.entity.Topic;
import backend.dev.notification.repository.TokenRepository;
import backend.dev.setting.exception.ErrorCode;
import backend.dev.setting.exception.PublicPlusCustomException;
import backend.dev.user.entity.User;
import backend.dev.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FCMService {
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;


    public void subscribeTopic(String topicName) {
        String userId = "";// 유저 아이디는 세션에서 가져온다고 가정하자
        User user = userRepository.findById(userId).orElseThrow(
                () -> new PublicPlusCustomException(ErrorCode.NOT_FOUND_USER));
        // User 정보를 통해 토큰을 가져온다
        FCMToken byUser = tokenRepository.findByUser(user);

//        Topic topic = tokenRepository.findById(topic)

    }
}
