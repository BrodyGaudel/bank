package org.mounanga.userservice.dto;

public record LoginResponse(String jwt, boolean passwordNeedToBeModified) {
}
