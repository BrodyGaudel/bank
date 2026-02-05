package com.mounanga.accountcommandservice.command;


import com.mounanga.accountcommandservice.enums.AccountStatus;
import com.mounanga.accountcommandservice.enums.Currency;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
public class CreateAccountCommand extends BaseCommand<String> {

    private final AccountStatus status;
    private final BigDecimal balance;
    private final Currency currency;
    private final String customerId;

    public CreateAccountCommand(String id, LocalDateTime dateTime, String author, AccountStatus status, BigDecimal balance, Currency currency, String customerId) {
        super(id, dateTime, author);
        this.status = status;
        this.balance = balance;
        this.currency = currency;
        this.customerId = customerId;
    }

}
