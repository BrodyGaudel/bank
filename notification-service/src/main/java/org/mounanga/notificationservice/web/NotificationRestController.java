package org.mounanga.notificationservice.web;

import jakarta.validation.Valid;
import org.mounanga.notificationservice.dto.NotificationRequestDTO;
import org.mounanga.notificationservice.service.NotificationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
public class NotificationRestController {

    private final NotificationService notificationService;

    public NotificationRestController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/send")
    public void send(@RequestBody @Valid NotificationRequestDTO dto){
        notificationService.send(dto);
    }
}
