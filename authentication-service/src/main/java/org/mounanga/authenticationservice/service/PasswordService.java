package org.mounanga.authenticationservice.service;

import org.mounanga.authenticationservice.dto.ChangePasswordRequestDTO;

public interface PasswordService {

    void requestCodeToResetPassword(String email);

    void resetPassword(ChangePasswordRequestDTO dto);

}
