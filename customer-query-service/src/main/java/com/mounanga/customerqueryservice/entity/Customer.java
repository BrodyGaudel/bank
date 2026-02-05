package com.mounanga.customerqueryservice.entity;

import com.mounanga.customerqueryservice.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;



@Entity
@Table(name = "customers")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Customer implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(nullable = false, unique = true, length = 36)
    private String id;

    @Column(nullable = false, length = 200)
    private String firstname;

    @Column(nullable = false, length = 200)
    private String lastname;

    @Column(nullable = false, length = 200)
    private String placeOfBirth;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(unique = true, length = 50, nullable = false)
    private String nip;

    @Column(unique = true, length = 150, nullable = false)
    private String email;

    @Column(length = 200, nullable = false)
    private String nationality;


    @Column(nullable = false)
    private LocalDateTime auditDateTime;


    @Column(nullable = false, length = 100)
    private String auditAuthor;

    @Column(nullable = false)
    private Boolean isDeleted = false;
}
