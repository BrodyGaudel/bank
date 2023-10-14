package com.brodygaudel.customerservice.dtos;

import java.util.List;

public record CustomerPageDTO(
        int page,
        int size,
        int totalPage,
        List<CustomerDTO> customerDTOList
) {
}
