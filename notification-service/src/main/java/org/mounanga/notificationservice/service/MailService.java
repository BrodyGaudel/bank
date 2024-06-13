package org.mounanga.notificationservice.service;

import org.mounanga.notificationservice.dto.LoginNotification;
import org.mounanga.notificationservice.dto.Notification;

public interface MailService {

    void send(Notification notification);

    void sendLoginNotification(LoginNotification notification);
}
