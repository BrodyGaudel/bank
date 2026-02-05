package com.mounanga.accountqueryservice.entity;

import com.mounanga.accountqueryservice.enums.AccountStatus;
import com.mounanga.accountqueryservice.enums.Currency;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@NoArgsConstructor
@Getter
@Setter
@ToString
public class AccountResponseDTO {
    private String id;
    private AccountStatus status;
    private BigDecimal balance;
    private Currency currency;
    private String customerId;
    private LocalDateTime createdDate;
    private String createBy;
    private LocalDateTime lastModifiedDate;
    private String lastModifiedBy;

    public AccountResponseDTO(@NotNull Account account) {
        this.id = account.getId();
        this.status = account.getStatus();
        this.balance = account.getBalance();
        this.currency = account.getCurrency();
        this.customerId = account.getCustomerId();
        this.createdDate = account.getCreatedDate();
        this.lastModifiedDate = account.getLastModifiedDate();
        this.lastModifiedBy = account.getLastModifiedBy();
    }
}
