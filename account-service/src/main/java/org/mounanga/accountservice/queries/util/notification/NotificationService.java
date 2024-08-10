package org.mounanga.accountservice.queries.util.notification;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface NotificationService {
    void sentAccountCreationNotification(String accountId, String email, LocalDateTime dateTime);
    void sendAccountDeletedNotification(String accountId, String email, LocalDateTime dateTime);
    void sendAccountActivationNotification(String email, LocalDateTime dateTime);
    void sendAccountSuspensionNotification(String email, LocalDateTime dateTime);
    void sendAccountCreditedNotification(String email, BigDecimal amountCredited, BigDecimal balance, LocalDateTime dateTime);
    void sendAccountDebitedNotification(String email, BigDecimal amountDebited, BigDecimal balance, LocalDateTime dateTime);
}
