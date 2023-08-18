package com.mounanga.accountservice.dtos;

import com.mounanga.accountservice.enums.Status;

public record UpdateStatusForm(String accountId, Status status) {
}
