package org.mounanga.accountservice.query.dto;

import lombok.*;
import org.mounanga.accountservice.query.enums.Type;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class OperationResponse {
    private String id;
    private LocalDateTime dateTime;
    private BigDecimal amount;
    private Type type;
    private String description;
    private String operator;
    private String accountId;
}
