package org.mounanga.accountservice.command.util.implementation;

import org.mounanga.accountservice.command.util.AccountIdGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class AccountIdGeneratorImpl implements AccountIdGenerator {

    private final CounterRepository counterRepository;

    public AccountIdGeneratorImpl(CounterRepository counterRepository) {
        this.counterRepository = counterRepository;
    }


    @Override
    public String generateAccountId() {
        Counter counter = counterRepository.save(new Counter());
        return String.format("%012d", counter.getId());
    }

}
