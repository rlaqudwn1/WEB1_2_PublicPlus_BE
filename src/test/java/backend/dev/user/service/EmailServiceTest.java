package backend.dev.user.service;

import backend.dev.setting.exception.PublicPlusCustomException;
import backend.dev.setting.redis.Redis;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.store.FolderException;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.*;

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
        Mockito.when(redis.isHasValues("aaa@aaa.com")).thenThrow(PublicPlusCustomException.class);
        Mockito.when(redis.getValues("bbb@bbb.com")).thenReturn("123123");
        greenMail.purgeEmailFromAllMailboxes();
    }

    @Test
    void sendCodeToEmail() {
        //given
        String toMail = "test@test.com";
        String subject = "test subject";
        String body = "hello world";
        //when
        emailService.sendCodeToEmail(toMail);
        //then
//        assertThatThrownBy(()->emailService.sendCodeToEmail(failToMail)).isInstanceOf(PublicPlusCustomException.class);

        String body1 = GreenMailUtil.getBody(greenMail.getReceivedMessages()[0]);
        System.out.println(body1);
    }

    @Test
    void verifyCode() {
        //given
        //when
        //then
    }
}
