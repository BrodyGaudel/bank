package org.mounanga.authenticationservice.util.generator;

import org.jetbrains.annotations.NotNull;

import java.security.SecureRandom;

public class CodeGenerator {

    private CodeGenerator() {
        super();
    }

    public static @NotNull String generate(int size) {
        if(size < 1) {
            throw new IllegalArgumentException("Size must be greater than 0");
        }
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            int digit = random.nextInt(10);
            code.append(digit);
        }
        return code.toString();
    }

}
