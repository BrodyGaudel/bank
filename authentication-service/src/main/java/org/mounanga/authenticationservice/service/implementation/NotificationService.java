package org.mounanga.authenticationservice.service.implementation;

import lombok.extern.slf4j.Slf4j;
import org.mounanga.authenticationservice.dto.NotificationRequestDTO;
import org.mounanga.authenticationservice.web.NotificationRestClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    private final NotificationRestClient notificationRestClient;

    public NotificationService(NotificationRestClient notificationRestClient) {
        this.notificationRestClient = notificationRestClient;
    }


    @Async
    public void send(String to, String subject, String body) {
        NotificationRequestDTO dto = new NotificationRequestDTO(to, subject, body);
        notificationRestClient.sendNotification(dto);
    }
}
