package chat.flirtbackend.service;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.util.Date;

@Service
public class EmailService {

    @Value("${app.email.sender}")
    private String email;
    @Value("${app.email.enabled}")
    private Boolean enabled;

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    public EmailService(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    public void sendMessage(String to, String subject, String text) {
        if(enabled) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(email);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            javaMailSender.send(message);
        }
    }

    @SneakyThrows
    public void sendMessage(String to, String subject, String templateName, Context context) {
        if(enabled) {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
            message.setFrom(email);
            message.setTo(to);
            message.setSentDate(new Date());
            message.setSubject(subject);
            String htmlContent = templateEngine.process(templateName, context);
            message.setText(htmlContent, true);
            javaMailSender.send(mimeMessage);
        }
    }

}
