package org.mounanga.accountservice.query.web.restclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "NOTIFICATION-SERVICE")
public interface NotificationRestClient {

    @PostMapping("/bank/notifications/send")
    void postNotification(@RequestBody Notification notification);
}
