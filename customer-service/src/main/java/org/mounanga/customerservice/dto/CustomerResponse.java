package org.mounanga.customerservice.dto;

import lombok.*;
import org.mounanga.customerservice.enums.Sex;

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
    private Sex sex;
    private String cin;
    private LocalDateTime createdDate;
    private String creator;
    private LocalDateTime lastModifiedDate;
    private String lastModifier;
}
