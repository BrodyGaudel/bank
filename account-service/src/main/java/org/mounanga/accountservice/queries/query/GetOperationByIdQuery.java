package org.mounanga.accountservice.queries.query;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class GetOperationByIdQuery {
    private String operationId;
}
