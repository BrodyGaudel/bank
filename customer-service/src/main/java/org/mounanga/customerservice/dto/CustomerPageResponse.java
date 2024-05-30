package org.mounanga.customerservice.dto;

import java.util.List;

public record CustomerPageResponse(PaginationInfo pagination,
                                   PageStatus status,
                                   List<CustomerResponse> customers) {
}
