package backend.dev.user.utils;

import backend.dev.setting.exception.ErrorCode;
import backend.dev.setting.exception.PublicPlusCustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class EmailUtil {
    private final JavaMailSender javaMailSender;

    public void sendEmail(String email, String title, String text) {
        SimpleMailMessage emailForm = createEmailForm(email, title, text);
        try {
            javaMailSender.send(emailForm);
            log.info("이메일 발송 성공");
        } catch (MailException e) {
            log.error("{}",e.getMessage());
        }
    }


    // 발신할 이메일 데이터 세팅
    private SimpleMailMessage createEmailForm(String toEmail, String title, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(title);
        message.setText(text);

        return message;
    }
}
