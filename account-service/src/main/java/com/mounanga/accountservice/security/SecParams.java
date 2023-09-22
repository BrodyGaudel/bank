package com.mounanga.accountservice.security;

public class SecParams {
    public static final long EXP_TIME = 5*60*1000L;
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";
    public static final String ROLES = "roles";
    public static final String SECRET = "brodygaudel@spring.io";

    private SecParams(){
        super();
    }
}
