package com.mounanga.customercommandservice.command;

import com.mounanga.customercommandservice.enums.Gender;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class CreateCustomerCommand extends BaseCommand<String> {

    private final String firstname;
    private final String lastname;
    private final String placeOfBirth;
    private final LocalDate dateOfBirth;
    private final Gender gender;
    private final String nip;
    private final String email;
    private final String nationality;


    public CreateCustomerCommand(String id, LocalDateTime dateTime, String author, String firstname, String lastname, String placeOfBirth, LocalDate dateOfBirth, Gender gender, String nip, String email, String nationality) {
        super(id, dateTime, author);
        this.firstname = firstname;
        this.lastname = lastname;
        this.placeOfBirth = placeOfBirth;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.nip = nip;
        this.email = email;
        this.nationality = nationality;
    }
}
