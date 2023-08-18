package com.mounanga.accountservice.dtos;

import com.mounanga.accountservice.enums.Sex;

import java.util.Date;

public record CustomerDTO(String id,
                          String firstname,
                          String name,
                          String placeOfBirth,
                          Date dateOfBirth,
                          String nationality,
                          Sex sex,
                          String cin,
                          String email,
                          String phone,
                          Date creation,
                          Date lastUpdate) {
}
