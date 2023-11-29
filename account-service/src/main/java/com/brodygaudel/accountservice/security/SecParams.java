package com.brodygaudel.accountservice.security;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
@NoArgsConstructor
@Getter
@Component
public class SecParams {

    @Value("${secret}")
    private String secret;

    @Value("${expired-time}")
    private Long expiredTime;
}
