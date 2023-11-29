package com.brodygaudel.accountservice.entity;

import com.brodygaudel.accountservice.enums.Currency;
import com.brodygaudel.accountservice.enums.OperationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Operation {

    @Id
    private String id;

    @Column(updatable = false, nullable = false)
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    @Column(updatable = false, nullable = false)
    private OperationType type;

    @Column(updatable = false, nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(updatable = false, nullable = false)
    private Currency currency;

    @Column(updatable = false, nullable = false)
    private String description;

    @ManyToOne
    private Account account;
}
