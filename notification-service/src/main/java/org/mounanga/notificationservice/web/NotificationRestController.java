package org.mounanga.notificationservice.web;

import org.mounanga.notificationservice.dto.Notification;
import org.mounanga.notificationservice.service.MailService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
public class NotificationRestController {

    private final MailService mailService;

    public NotificationRestController(MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping("/send")
    public void sendNotification(@RequestBody Notification notification) {
        mailService.send(notification);
    }
}
