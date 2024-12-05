package backend.dev.user.service;

import backend.dev.setting.exception.ErrorCode;
import backend.dev.setting.exception.PublicPlusCustomException;
import backend.dev.setting.redis.Redis;
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

    private final Redis redis;

    private final EmailUtil emailUtil;

    private static final String AUTH_CODE_PREFIX = "AuthCode_";

    @Value("${auth-code-expiration-millis}")
    private long authCodeExpirationMillis;

    public void sendCodeToEmail(String toEmail) {
        String title = "공공 플러스 이메일 인증 번호";
        String authCode = this.createCode();
        String article = "공공 플러스 이메일 인증 번호입니다\n" + "인증번호 : " + authCode;


        if (redis.isHasValues(AUTH_CODE_PREFIX + toEmail)) {
            throw new PublicPlusCustomException(ErrorCode.ALWAYS_SEND_EMAIL);
        }
        redis.setValues(AUTH_CODE_PREFIX + toEmail, authCode, Duration.ofMillis(authCodeExpirationMillis));

        emailUtil.sendEmail(toEmail, title, article);
    }


    public boolean verifyCode(String email, String authCode) {
        String redisAuthCode = redis.getValues(AUTH_CODE_PREFIX + email);
        if (redisAuthCode == null) {
            throw new PublicPlusCustomException(ErrorCode.CERTIFICATION_TIME_OVER);
        }

        if (redisAuthCode.equals(authCode)) {
            redis.removeValues(AUTH_CODE_PREFIX + email);
        }

        return redisAuthCode.equals(authCode);
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
            log.error("인증코드 생성 오류 : {}", e.getMessage());
            throw new PublicPlusCustomException(ErrorCode.SERVER_ERROR);
        }
    }
}
