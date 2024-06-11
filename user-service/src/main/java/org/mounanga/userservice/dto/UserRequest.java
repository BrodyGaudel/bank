package org.mounanga.userservice.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class UserRequest {

    @NotBlank(message = "field 'firstname' is mandatory : it can not be blank")
    private String firstname;

    @NotBlank(message = "field 'lastname' is mandatory : it can not be blank")
    private String lastname;

    @NotBlank(message = "field 'cin' is mandatory : it can not be blank")
    @Size(min = 6, max=32, message = "The ‘CIN’ field must have a minimum of 6 alphanumeric characters and a maximum of 32.")
    private String cin;

    @NotBlank(message = "field 'email' is mandatory : it can not be blank")
    @Email(message = "field 'email' is not well formatted : it must be like emple@mail.com")
    private String email;

    @NotBlank(message = "field 'username' is mandatory : it can not be blank")
    private String username;

    @NotBlank(message = "field 'password' is mandatory : it can not be blank")
    @Size(min = 8, message = "a password must have at least 8 alphanumeric characters")
    private String password;
}
