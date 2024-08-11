package org.mounanga.userservice.dto;

public record NotificationRequest(String to, String subject, String body) {
}
