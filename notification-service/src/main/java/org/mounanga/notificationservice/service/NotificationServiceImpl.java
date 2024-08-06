package org.mounanga.notificationservice.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.mounanga.notificationservice.configuration.ApplicationProperties;
import org.mounanga.notificationservice.dto.NotificationRequestDTO;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;

import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {

    private static final String NOTIFICATION_TEMPLATE = "notification.html";

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final ApplicationProperties applicationProperties;

    public NotificationServiceImpl(JavaMailSender mailSender, SpringTemplateEngine templateEngine, ApplicationProperties applicationProperties) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.applicationProperties = applicationProperties;
    }


    @Async
    @Override
    public void sendNotification(NotificationRequestDTO notification) {
        log.info("In sendNotification()");
        try{
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MULTIPART_MODE_MIXED, UTF_8.name());
            Map<String, Object> properties = new HashMap<>();
            properties.put("data", notification.body());
            send(notification.to(), notification.subject(),mimeMessage, helper, properties);
            log.info("Successfully sent notification");
        }catch (Exception e) {
            log.warn("Failed to send notification");
            log.error(e.getMessage());
            log.error(e.getLocalizedMessage());
        }
    }

    private void send(String to, String subject, MimeMessage mimeMessage, @NotNull MimeMessageHelper helper, Map<String, Object> properties) throws MessagingException {
        Context context = new Context();
        context.setVariables(properties);

        helper.setFrom(applicationProperties.getSystemEmail());
        helper.setTo(to);
        helper.setSubject(subject);

        String template = templateEngine.process(NOTIFICATION_TEMPLATE, context);
        helper.setText(template, true);
        mailSender.send(mimeMessage);
    }
}
