package com.brodygaudel.customerservice.securities;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class SecParams {

    @Value("${secret}")
    private String secret;

    @Value("${expired-time}")
    private Long expiredTime;

    public SecParams(){
        super();
    }
}
