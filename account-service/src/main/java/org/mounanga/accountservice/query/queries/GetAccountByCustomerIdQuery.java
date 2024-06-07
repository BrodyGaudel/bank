package org.mounanga.accountservice.query.queries;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class GetAccountByCustomerIdQuery {
    private String customerId;
}
