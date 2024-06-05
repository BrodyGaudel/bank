package org.mounanga.accountservice.query.queries;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class GetAllOperationByAccountIdQuery {
    private String accountId;
    private int page;
    private int size;
}
