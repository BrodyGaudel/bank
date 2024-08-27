package org.mounanga.userservice.web;

import org.mounanga.userservice.dto.NotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "NOTIFICATION-SERVICE")
public interface NotificationRestClient {

    @PostMapping("/bank/mailing/send")
    void sendNotification(@RequestBody NotificationRequest request);
}
