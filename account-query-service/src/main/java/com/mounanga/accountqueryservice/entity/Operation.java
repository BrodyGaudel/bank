package com.mounanga.accountqueryservice.entity;

import com.mounanga.accountqueryservice.enums.OperationType;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Operation implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, updatable = false)
    private OperationType type;

    @Column(nullable = false, updatable = false)
    private BigDecimal amount;

    @Column(nullable = false, updatable = false)
    private String remark;

    @Column(nullable = false, updatable = false)
    private String author;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateTime;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Override
    public String toString() {
        return "Operation{" +
                "id='" + id + '\'' +
                ", type=" + type +
                ", amount=" + amount +
                ", remark='" + remark + '\'' +
                ", author='" + author + '\'' +
                ", dateTime=" + dateTime +
                ", account=" + account +
                '}';
    }


}

