package com.mounanga.userservice.securities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class SecParams {

    private static final Logger log = LoggerFactory.getLogger(SecParams.class);
    public static final long EXP_TIME = 10*24*60*60*1000L;
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";
    public static final String ROLES = "roles";
    public static final String ALL = "*";
    public static final long MAX_AGE = 3600L;
    public static final String ORIGINS = "http://localhost:4200";

    private static final String FILE_NAME = "/secret";
    private SecParams(){
        super();
    }


    public static String getSecret(){
        try {
            ClassPathResource resource = new ClassPathResource(FILE_NAME);
            InputStream inputStream = resource.getInputStream();
            byte[] fileBytes = FileCopyUtils.copyToByteArray(inputStream);
            String fileContent = new String(fileBytes, StandardCharsets.UTF_8);
            inputStream.close();
            return fileContent;
        } catch (IOException e) {
            log.error(e.getMessage());
            return e.getMessage();
        }
    }
}
