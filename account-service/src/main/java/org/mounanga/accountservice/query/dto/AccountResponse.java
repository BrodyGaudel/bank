package org.mounanga.accountservice.query.dto;

import lombok.*;
import org.mounanga.accountservice.common.enums.Currency;
import org.mounanga.accountservice.common.enums.Status;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class AccountResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private Status status;
    private BigDecimal balance;
    private Currency currency;
    private String customerId;
    private LocalDateTime createdDate;
    private String createdBy;
    private LocalDateTime lastModifiedDate;
    private String lastModifiedBy;
}
