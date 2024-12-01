package backend.dev.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import backend.dev.setting.exception.PublicPlusCustomException;
import backend.dev.setting.redis.Redis;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.store.FolderException;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = {"classpath:/application-test.properties"})
class EmailServiceTest {

    @RegisterExtension //테스트 클래스 추가 에노테이션
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("springboot", "springboot"))
            .withPerMethodLifecycle(false);
    @Autowired
    EmailService emailService;
    @MockBean
    Redis redis;


    @BeforeEach
    void init() throws FolderException {
        greenMail.purgeEmailFromAllMailboxes();
    }

    @Test
    @DisplayName("이미 인증을 요청한 메일에서는 예외를 발생시키고, 그렇지 않은 메일에 대해서는 메일을 전송시킨다")
    void sendCodeToEmail() {
        //given
        Mockito.when(redis.isHasValues("AuthCode_aaa@aaa.com")).thenThrow(PublicPlusCustomException.class);
        String toFailByAlwaysSend = "aaa@aaa.com";
        String toSuccess = "bbb@bbb.com";
        //when
        emailService.sendCodeToEmail(toSuccess);
        String mailText = GreenMailUtil.getBody(greenMail.getReceivedMessages()[0]);
        System.out.println(mailText);
        byte[] decodedBytes = Base64.getDecoder().decode(mailText.replaceAll("\\s+", ""));//모든 공백문자 제거
        String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);
        //then
        assertThatThrownBy(()->emailService.sendCodeToEmail(toFailByAlwaysSend)).isInstanceOf(PublicPlusCustomException.class);
        assertThat(decodedString).contains("공공 플러스 이메일 인증 번호입니다");
    }

    @Test
    @DisplayName("레디스에 저장된 메일과 올바른 인증번호를 검증하면 true, 아니면 예외를 발생시킨다")
    void verifyCode() {
        //given
        Mockito.when(redis.getValues("AuthCode_bbb@bbb.com")).thenReturn("123123");
        Mockito.when(redis.getValues("AuthCode_ccc@ccc.com")).thenReturn(null);
        //when
        boolean isTrue = emailService.verifyCode("bbb@bbb.com", "123123");
        //then
        assertThatThrownBy(()->emailService.verifyCode("ccc@ccc.com", "435345")).isInstanceOf(
                PublicPlusCustomException.class);
        assertThat(isTrue).isTrue();
    }
}
