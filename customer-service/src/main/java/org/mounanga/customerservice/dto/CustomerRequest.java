package org.mounanga.customerservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.mounanga.customerservice.enums.Sex;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class CustomerRequest {

    @NotBlank(message = "field 'firstname' is mandatory : it can not be blank")
    private String firstname;

    @NotBlank(message = "field 'lastname' is mandatory : it can not be blank")
    private String lastname;

    @NotBlank(message = "field 'place of birth' is mandatory : it can not be blank")
    private String placeOfBirth;

    @NotNull(message = "field 'date of birth' is mandatory : it can not be null")
    private LocalDate dateOfBirth;

    @NotBlank(message = "field 'nationality' is mandatory : it can not be blank")
    private String nationality;

    @NotNull(message = "field 'sex' is mandatory : it can not be null")
    private Sex sex;

    @NotBlank(message = "field 'cin' is mandatory : it can not be blank")
    @Size(min = 6, max = 64, message = "the length of the 'cin' field must be between a minimum of 6 and a maximum of 64")
    private String cin;

    @NotBlank(message = "field 'email' is mandatory : it can not be blank")
    @Email(message = "filed 'email' is not well formated")
    private String email;
}
