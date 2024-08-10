package org.mounanga.accountservice.commands.util.validation;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountEmailCustomerIdRepository extends JpaRepository<AccountEmailCustomerId, String> {

    @Override
    boolean existsById(@NotNull String s);
    boolean existsByEmail(String email);
    boolean existsByCustomerId(String customerId);
    AccountEmailCustomerId findByEmail(String email);
    AccountEmailCustomerId findByCustomerId(String customerId);
    AccountEmailCustomerId findByEmailAndCustomerId(String email, String customerId);
}
