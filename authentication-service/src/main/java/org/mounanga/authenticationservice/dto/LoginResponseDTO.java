package org.mounanga.authenticationservice.dto;

public record LoginResponseDTO(String jwt, boolean passwordNeedToBeUpdate) {
}
