package org.mounanga.accountservice.commands.command;


import lombok.Getter;
import org.mounanga.accountservice.common.enums.AccountStatus;
import org.mounanga.accountservice.common.enums.Currency;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class CreateAccountCommand extends BaseCommand<String>{
    private final AccountStatus status;
    private final BigDecimal balance;
    private final Currency currency;
    private final String customerId;
    private final String email;

    public CreateAccountCommand(String id, LocalDateTime commandDate, String commandBy, AccountStatus status, BigDecimal balance, Currency currency, String customerId, String email) {
        super(id, commandDate, commandBy);
        this.status = status;
        this.balance = balance;
        this.currency = currency;
        this.customerId = customerId;
        this.email = email;
    }
}
