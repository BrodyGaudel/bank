package org.mounanga.authenticationservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.mounanga.authenticationservice.enums.Gender;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @Column(nullable = false)
    private String placeOfBirth;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    private String nationality;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false, unique = true)
    private String cin;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean enabled;

    @Column(nullable = false)
    private Boolean passwordNeedToBeModified;

    private LocalDateTime lastLogin;

    @CreatedBy
    private String createBy;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedBy
    private String lastModifiedBy;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    @ManyToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name="user_role",joinColumns = @JoinColumn(name="user_id") , inverseJoinColumns = @JoinColumn(name="role_id"))
    private List<Role> roles;

    public String getFullName(){
        return firstname + " " + lastname;
    }

    public boolean isEnabled(){
        return enabled != null && !enabled.equals(Boolean.FALSE);
    }

    public boolean isPasswordNeedToBeModified(){
        return passwordNeedToBeModified != null && !passwordNeedToBeModified.equals(Boolean.FALSE);
    }
}
