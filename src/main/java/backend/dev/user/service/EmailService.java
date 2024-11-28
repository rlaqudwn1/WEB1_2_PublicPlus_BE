package backend.dev.user.service;

import backend.dev.setting.exception.ErrorCode;
import backend.dev.setting.exception.PublicPlusCustomException;
import backend.dev.setting.redis.Redis;
import backend.dev.user.repository.UserRepository;
import backend.dev.user.utils.EmailUtil;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EmailService {

    private final UserRepository userRepository;

    private final Redis redis;

    private final EmailUtil emailUtil;

    private static final String AUTH_CODE_PREFIX = "AuthCode_";

    @Value("${auth-code-expiration-millis}") // 시간제한 3분
    private long authCodeExpirationMillis;

    public void sendCodeToEmail(String toEmail) {
        //이메일 중복 검사
        if(userRepository.findByEmail(toEmail).isPresent()) throw new PublicPlusCustomException(ErrorCode.DUPLICATE_EMAIL);

        //인증코드 생성, 저장 및 이메일 전송
        String title = "공공 플러스 이메일 인증 번호";
        String authCode = this.createCode();
        String article = "공공 플러스 이메일 인증 번호입니다\n" + "인증번호 : " + authCode;
        // 이메일 인증 요청 시 인증 번호 Redis에 저장
        log.info(AUTH_CODE_PREFIX + "{}", toEmail);
        if (redis.checkExistsValue(AUTH_CODE_PREFIX + toEmail)) {
            throw new PublicPlusCustomException(ErrorCode.ALWAYS_SEND_EMAIL);
        }
        redis.setValues(AUTH_CODE_PREFIX + toEmail,
                authCode, Duration.ofMillis(authCodeExpirationMillis));
        emailUtil.sendEmail(toEmail, title, article);
    }


    public boolean verifyCode(String email, String authCode) {
        String redisAuthCode = redis.getValues(AUTH_CODE_PREFIX + email);

        return redis.checkExistsValue(redisAuthCode) && redisAuthCode.equals(authCode);
    }

    private String createCode() {
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < 6; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new PublicPlusCustomException(ErrorCode.SERVER_ERROR);
        }
    }
}
