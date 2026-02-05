package com.mounanga.customercommandservice.dto;

import com.mounanga.customercommandservice.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CustomerRequestDTO(
        @Size(max = 200, message = "the size of lastname must be under 200 characters")
        String firstname,

        @NotBlank(message = "lastname is mandatory: it can not be blank, null or empty")
        @Size(max = 200, message = "the size of lastname must be under 200 characters")
        String lastname,

        @NotBlank(message = "firstname is mandatory: it can not be blank, null or empty")
        @Size(max = 200, message = "the size of place of birth must be under 200 characters")
        String placeOfBirth,

        @NotNull(message = "date of birth is mandatory: it can not be null")
        LocalDate dateOfBirth,

        @NotBlank(message = "nationality is mandatory: it can not be blank, null or empty")
        @Size(max = 200, message = "the size of nationality of birth must be under 200 characters")
        String nationality,

        @NotNull(message = "gender is mandatory: it can not be null")
        Gender gender,

        @NotBlank(message = "nip is mandatory: it can not be blank, empty or null")
        @Size(min = 8, max = 16, message = "nip's size must be between 8 and 16")
        String nip,

        @NotBlank(message = "email is mandatory: it can not be blank, empty or null")
        @Email(message = "email must be well formated like john.doe@gmail.com")
        String email) {

}