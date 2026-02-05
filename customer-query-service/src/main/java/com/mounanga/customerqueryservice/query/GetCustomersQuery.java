package com.mounanga.customerqueryservice.query;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GetCustomersQuery {
    private String keyword;
    private int page;
    private int pageSize;
    private boolean deleted;
}
