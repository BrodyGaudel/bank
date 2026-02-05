package com.mounanga.accountcommandservice.bank;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "ribs", indexes = {
        @Index(name = "idx_rib_number", columnList = "rib_number", unique = true)
})
@Getter
@Setter
@ToString
public class RIB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rib_number", nullable = false, unique = true, length = 23)
    private String ribNumber;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;


    public RIB() {
        this.createdAt = LocalDateTime.now();
    }

    public RIB(String ribNumber) {
        this.ribNumber = ribNumber;
        this.createdAt = LocalDateTime.now();
    }

}
