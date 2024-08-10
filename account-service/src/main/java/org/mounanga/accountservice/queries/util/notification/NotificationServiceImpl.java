package org.mounanga.accountservice.queries.util.notification;

import org.jetbrains.annotations.NotNull;
import org.mounanga.accountservice.queries.dto.NotificationRequestDTO;
import org.mounanga.accountservice.queries.web.NotificationRestClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy 'at' HH:mm");
    private static final String BANK_ACCOUNT_CREATION_TITLE = "Bank account creation.";
    private static final String BANK_ACCOUNT_ACTIVATION_TITLE = "Bank account activation.";
    private static final String BANK_ACCOUNT_SUSPENSION_TITLE = "Bank account suspension.";
    private static final String BANK_ACCOUNT_CREDITED_TITLE = "Bank account credited.";
    private static final String BANK_ACCOUNT_DEBITED_TITLE = "Bank account debited.";
    private static final String BANK_ACCOUNT_DELETED_TITLE = "Bank account deleted.";

    private final NotificationRestClient notificationRestClient;

    public NotificationServiceImpl(NotificationRestClient notificationRestClient) {
        this.notificationRestClient = notificationRestClient;
    }

    @Async
    @Override
    public void sentAccountCreationNotification(String accountId, String email, LocalDateTime dateTime) {
        String body = "Hello! Your bank account has been successfully created on " + formatDateTime(dateTime) +
                ". Your bank account number is " + accountId + ".";
        sendNotification(email, BANK_ACCOUNT_CREATION_TITLE, body);
    }

    @Async
    @Override
    public void sendAccountDeletedNotification(String accountId, String email, LocalDateTime dateTime) {
        String body = "Hello! Your account with id"+accountId+"has just been deleted on "+formatDateTime(dateTime)+
                ". For further information, please contact your nearest branch";
        sendNotification(email, BANK_ACCOUNT_DELETED_TITLE, body);
    }

    @Async
    @Override
    public void sendAccountActivationNotification(String email, LocalDateTime dateTime) {
        String body = "Hello! Your bank account has just been activated on " + formatDateTime(dateTime) +
                ". For further information, please contact your nearest branch.";
        sendNotification(email, BANK_ACCOUNT_ACTIVATION_TITLE, body);
    }

    @Async
    @Override
    public void sendAccountSuspensionNotification(String email, LocalDateTime dateTime) {
        String body = "Hello! Your bank account has just been suspended on " + formatDateTime(dateTime) +
                ". For further information, please contact your nearest branch.";
        sendNotification(email, BANK_ACCOUNT_SUSPENSION_TITLE, body);
    }

    @Async
    @Override
    public void sendAccountCreditedNotification(String email, BigDecimal amountCredited, BigDecimal balance, LocalDateTime dateTime) {
        String body = "Hello! Your bank account was credited with " + amountCredited +
                " on " + formatDateTime(dateTime) + ". Your new balance is: " + balance + ".";
        sendNotification(email, BANK_ACCOUNT_CREDITED_TITLE, body);
    }

    @Async
    @Override
    public void sendAccountDebitedNotification(String email, BigDecimal amountDebited, BigDecimal balance, LocalDateTime dateTime) {
        String body = "Hello! Your bank account was debited with " + amountDebited +
                " on " + formatDateTime(dateTime) + ". Your new balance is: " + balance + ".";
        sendNotification(email, BANK_ACCOUNT_DEBITED_TITLE, body);
    }

    private void sendNotification(String email, String title, String body) {
        NotificationRequestDTO notification = new NotificationRequestDTO(email, title, body);
        notificationRestClient.sendNotification(notification);
    }

    @NotNull
    private String formatDateTime(@NotNull LocalDateTime dateTime) {
        return dateTime.format(FORMATTER);
    }
}

