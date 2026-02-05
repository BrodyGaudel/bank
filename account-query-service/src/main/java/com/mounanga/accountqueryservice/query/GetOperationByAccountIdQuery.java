package com.mounanga.accountqueryservice.query;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class GetOperationByAccountIdQuery {

    private String accountId;
    private int page;
    private int pageSize;
}
