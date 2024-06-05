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
public class OperationPageResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private PaginationInfo pagination;
    private PageStatus status;
    private List<OperationResponse> accounts;
}
