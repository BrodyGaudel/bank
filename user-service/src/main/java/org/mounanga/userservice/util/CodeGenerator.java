package org.mounanga.userservice.util;

import org.jetbrains.annotations.NotNull;

import java.security.SecureRandom;

public class CodeGenerator {

    private CodeGenerator() {
        super();
    }

    public static @NotNull String generateVerificationCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            int digit = random.nextInt(10);
            code.append(digit);
        }
        return code.toString();
    }
}
