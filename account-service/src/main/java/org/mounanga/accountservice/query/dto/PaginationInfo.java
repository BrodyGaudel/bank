package org.mounanga.accountservice.query.dto;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class PaginationInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private int page;
    private int totalPages;
    private int size;
    private long totalElements;
    private int numberOfElements;
    private int number;
}
