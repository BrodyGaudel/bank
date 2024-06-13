package org.mounanga.notificationservice.dto;

import java.time.LocalDateTime;

public record LoginNotification(LocalDateTime dateTime, String where, String fullName, String email) {
}
