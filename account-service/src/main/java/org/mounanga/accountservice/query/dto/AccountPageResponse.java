package org.mounanga.accountservice.query.dto;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class AccountPageResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private PaginationInfo pagination;
    private PageStatus status;
    private List<AccountResponse> accounts;
}
