package org.mounanga.userservice.web.restclient;

import java.time.LocalDateTime;

public record Notification(LocalDateTime dateTime, String where, String fullName, String email) {
}
