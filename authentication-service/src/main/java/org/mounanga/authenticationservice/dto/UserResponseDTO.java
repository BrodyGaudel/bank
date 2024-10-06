package org.mounanga.authenticationservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.mounanga.authenticationservice.enums.Gender;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserResponseDTO {
    private String id;
    private String firstname;
    private String lastname;
    private String placeOfBirth;
    private LocalDate dateOfBirth;
    private String nationality;
    private Gender gender;
    private String cin;
    private String email;
    private String username;
    private Boolean enabled;
    private Boolean passwordNeedToBeModified;
    private LocalDateTime lastLogin;
    private String createBy;
    private LocalDateTime createdDate;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;
    private List<String> roles;
}
