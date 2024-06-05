package org.mounanga.notificationservice.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class CustomerResponse {
    private String id;
    private String firstname;
    private String lastname;
    private String placeOfBirth;
    private LocalDate dateOfBirth;
    private String nationality;
    private String sex;
    private String cin;
    private LocalDateTime createdDate;
    private String creator;
    private LocalDateTime lastModifiedDate;
    private String lastModifier;

    public String getFullName(){
        return firstname + " " + lastname;
    }
}
