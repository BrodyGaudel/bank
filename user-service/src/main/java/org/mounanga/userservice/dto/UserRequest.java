package org.mounanga.userservice.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.mounanga.userservice.enums.Gender;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class UserRequest {
    @NotBlank(message = "field 'firstname' is mandatory: it cannot be blank")
    private String firstname;

    @NotBlank(message = "field 'lastname' is mandatory: it cannot be blank")
    private String lastname;

    @NotNull(message = "field 'dateOfBirth' is mandatory: it cannot be null")
    @Past(message = "dateOfBirth must be in past")
    private LocalDate dateOfBirth;

    @NotBlank(message = "field 'placeOfBirth' is mandatory: it cannot be blank")
    private String placeOfBirth;

    @NotNull(message = "field 'gender' is mandatory: it cannot be null")
    private Gender gender;

    @NotBlank(message = "field 'nationality' is mandatory: it cannot be blank")
    private String nationality;

    @NotBlank(message = "field 'cin' is mandatory: it cannot be blank")
    private String cin;

    @NotBlank(message = "field 'e-mail' is mandatory: it cannot be blank")
    @Email(message = "e-mail is not well formed")
    private String email;

    @NotBlank(message = "field 'username' is mandatory: it cannot be blank")
    private String username;

    @NotBlank(message = "field 'password' is mandatory: it cannot be blank")
    @Size(min = 8, message = "the minimum password size is 8 alpha-numeric characters.")
    private String password;
}
