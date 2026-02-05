package com.mounanga.customercommandservice.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mounanga.customercommandservice.enums.Gender;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class CustomerUpdatedEvent extends BaseEvent<String> {

    private final String firstname;
    private final String lastname;
    private final String placeOfBirth;
    private final LocalDate dateOfBirth;
    private final Gender gender;
    private final String nip;
    private final String email;
    private final String nationality;


    @JsonCreator
    public CustomerUpdatedEvent(
            @JsonProperty("id") String id,
            @JsonProperty("dateTime") LocalDateTime dateTime,
            @JsonProperty("author") String author,
            @JsonProperty("firstname") String firstname,
            @JsonProperty("lastname") String lastname,
            @JsonProperty("placeOfBirth") String placeOfBirth,
            @JsonProperty("dateOfBirth") LocalDate dateOfBirth,
            @JsonProperty("gender") Gender gender,
            @JsonProperty("nip") String nip,
            @JsonProperty("email") String email,
            @JsonProperty("nationality") String nationality) {
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
