package org.mounanga.customerservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.mounanga.customerservice.enums.Gender;
import org.mounanga.customerservice.util.validation.AgeMinimum;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class CustomerRequestDTO {
    @NotBlank(message = "field 'firstname' is mandatory: it can not be blank")
    private String firstname;

    @NotBlank(message = "field 'lastname' is mandatory: it can not be blank")
    private String lastname;

    @NotBlank(message = "field 'placeOfBirth' is mandatory: it can not be blank")
    private String placeOfBirth;

    @NotNull(message = "field 'dateOfBirth' is mandatory: it can not be null")
    @AgeMinimum(min = 18, message = "Customer must be at least 18 years old")
    private LocalDate dateOfBirth;

    @NotBlank(message = "field 'nationality' is mandatory: it can not be blank")
    private String nationality;

    @NotNull(message = "field 'gender' is mandatory: it can not be null")
    private Gender gender;

    @NotBlank(message = "field 'cin' is mandatory: it can not be blank")
    private String cin;

    @NotBlank(message = "field 'email' is mandatory: it can not be blank")
    @Email(message = "field 'email' is not well formated")
    private String email;
}