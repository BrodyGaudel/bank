package com.mounanga.accountqueryservice.query;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class GetAccountByCustomerIdQuery {
    private String customerId;
}
