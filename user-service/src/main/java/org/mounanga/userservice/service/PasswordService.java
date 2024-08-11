package org.mounanga.userservice.service;

import org.mounanga.userservice.dto.UpdatePasswordRequest;

public interface PasswordService {

    void requestNewVerificationCode(String email);
    void updatePassword(UpdatePasswordRequest request);
}
