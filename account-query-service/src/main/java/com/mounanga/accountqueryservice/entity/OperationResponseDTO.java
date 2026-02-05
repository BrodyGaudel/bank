package com.mounanga.accountqueryservice.entity;

import com.mounanga.accountqueryservice.enums.OperationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class OperationResponseDTO {
    private String id;
    private OperationType type;
    private BigDecimal amount;
    private String remark;
    private String author;
    private LocalDateTime dateTime;
    private String accountId;

    public OperationResponseDTO(@NotNull Operation operation) {
        this.id = operation.getId();
        this.type = operation.getType();
        this.amount = operation.getAmount();
        this.remark = operation.getRemark();
        this.author = operation.getAuthor();
        this.dateTime = operation.getDateTime();
        if (operation.getAccount() != null){
            this.accountId = operation.getAccount().getId();
        }
    }
}
