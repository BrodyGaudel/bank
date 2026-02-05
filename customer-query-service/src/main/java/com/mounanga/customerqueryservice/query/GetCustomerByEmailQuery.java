package com.mounanga.customerqueryservice.query;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GetCustomerByEmailQuery {
    private String email;
}
