package org.mounanga.userservice.util;

import lombok.extern.slf4j.Slf4j;
import org.mounanga.userservice.dto.NotificationRequest;
import org.mounanga.userservice.web.NotificationRestClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRestClient restClient;

    public NotificationServiceImpl(NotificationRestClient restClient) {
        this.restClient = restClient;
    }

    @Async
    @Override
    public void send(NotificationRequest request) {
        log.info("In send()");
        try{
            restClient.sendNotification(request);
            log.info("Successfully sent notification");
        }catch(Exception e){
            log.warn("Failed to send notification");
            log.error(e.getMessage());
        }
    }
}
