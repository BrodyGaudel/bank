package com.mounanga.customerqueryservice.dto;

import com.mounanga.customerqueryservice.entity.Customer;
import com.mounanga.customerqueryservice.enums.Gender;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class CustomerResponseDTO {
    private String id;
    private String firstname;
    private String lastname;
    private String placeOfBirth;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String nip;
    private String email;
    private String nationality;

    public CustomerResponseDTO(@NotNull Customer customer) {
        this.id = customer.getId();
        this.firstname = customer.getFirstname();
        this.lastname = customer.getLastname();
        this.placeOfBirth = customer.getPlaceOfBirth();
        this.dateOfBirth = customer.getDateOfBirth();
        this.gender = customer.getGender();
        this.nip  = customer.getNip();
        this.email = customer.getEmail();
        this.nationality = customer.getNationality();
    }
}
