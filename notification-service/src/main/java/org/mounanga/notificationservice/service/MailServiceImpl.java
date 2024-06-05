package org.mounanga.notificationservice.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mounanga.notificationservice.dto.CustomerResponse;
import org.mounanga.notificationservice.dto.Notification;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED;

@Slf4j
@Service
public class MailServiceImpl implements MailService {

    private static final String NOTIFICATION_TEMPLATE = "notification.html";
    private static final String NOTIFICATION = "NOTIFICATION";

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final CustomerRestClient customerRestClient;
    private final ApplicationProperties applicationProperties;

    public MailServiceImpl(JavaMailSender mailSender, SpringTemplateEngine templateEngine, CustomerRestClient customerRestClient, ApplicationProperties applicationProperties) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.customerRestClient = customerRestClient;
        this.applicationProperties = applicationProperties;
    }

    @Async
    @Override
    public void send(@NotNull Notification notification) {
        CustomerResponse customer = getCustomerById(notification.customerId());
        if(customer == null) {
            log.error("customer not found");
        }else{
            try{
                MimeMessage mimeMessage = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MULTIPART_MODE_MIXED, UTF_8.name());
                Map<String, Object> properties = new HashMap<>();
                properties.put("fullName", customer.getFullName());
                properties.put("when", formatLocalDateTime(notification.dateTime()));
                properties.put("amount", notification.amount());
                properties.put("description", notification.description());
                properties.put("type", notification.type().toString());
                String email = customer.getFirstname()+"."+customer.getLastname()+"@bank.ga";
                send(removeSpaces(email), mimeMessage, helper, properties, NOTIFICATION, NOTIFICATION_TEMPLATE);
            }catch (Exception e) {
                log.error(e.getMessage());
            }
        }

    }

    private @Nullable CustomerResponse getCustomerById(String customerId) {
        try{
            return customerRestClient.getCustomer(customerId);
        }catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    private void send(String to, MimeMessage mimeMessage, @NotNull MimeMessageHelper helper, Map<String, Object> properties, String subject, String templateGiven) throws MessagingException {
        Context context = new Context();
        context.setVariables(properties);

        helper.setFrom(applicationProperties.getSystemEmail());
        helper.setTo(to);
        helper.setSubject(subject);

        String template = templateEngine.process(templateGiven, context);
        helper.setText(template, true);
        mailSender.send(mimeMessage);
    }

    private static String removeSpaces(String input) {
        if (input == null) {
            return null;
        }
        return input.replace(" ", "");
    }

    private static @NotNull String formatLocalDateTime(LocalDateTime dateTime) {
        if(dateTime == null) {
            return " ";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss");
        return dateTime.format(formatter);
    }

}
