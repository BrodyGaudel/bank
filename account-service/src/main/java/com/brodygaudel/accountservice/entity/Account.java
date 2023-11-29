package com.brodygaudel.accountservice.entity;

import com.brodygaudel.accountservice.enums.AccountStatus;
import com.brodygaudel.accountservice.enums.Currency;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Account {

    @Id
    private String id;

    @Column(unique = true, updatable = false, nullable = false)
    private String customerId;

    @Column(nullable = false)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus status;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    private List<Operation> operations;

    @Column(nullable = false, updatable = false)
    private LocalDateTime creation;

    private LocalDateTime lastUpdate;
}
