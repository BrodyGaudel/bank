package com.mounanga.accountqueryservice.entity;

import com.mounanga.accountqueryservice.enums.AccountStatus;
import com.mounanga.accountqueryservice.enums.Currency;
import com.mounanga.accountqueryservice.event.AccountCreatedEvent;
import jakarta.persistence.*;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Account implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Column(nullable = false)
    private AccountStatus status;

    @Column(nullable = false)
    private BigDecimal balance;

    @Column(nullable = false)
    private Currency currency;

    @Column(nullable = false)
    private String customerId;

    @Column(updatable = false)
    private LocalDateTime createdDate;

    @Column(updatable = false)
    private String createBy;

    private LocalDateTime lastModifiedDate;

    private String lastModifiedBy;

    @OneToMany(
            mappedBy = "account",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Operation> operations = new ArrayList<>();


    public Account(@NotNull AccountCreatedEvent event) {
        this.id = event.getId();
        this.status = event.getStatus();
        this.balance = event.getBalance();
        this.currency = event.getCurrency();
        this.customerId = event.getCustomerId();
        this.createdDate = event.getDateTime();
        this.createBy = event.getAuthor();
        this.lastModifiedDate = event.getDateTime();
        this.lastModifiedBy = event.getAuthor();
    }


}

