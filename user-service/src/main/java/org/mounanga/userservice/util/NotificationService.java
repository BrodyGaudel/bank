package org.mounanga.userservice.util;

import org.mounanga.userservice.dto.NotificationRequest;

public interface NotificationService {
    void send(NotificationRequest request);
}
