package org.mounanga.userservice.web.restclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "NOTIFICATION-SERVICE")
public interface NotificationRestClient {

    @PostMapping("/bank/notifications/login")
    void sendNotification(@RequestBody Notification notification);
}
