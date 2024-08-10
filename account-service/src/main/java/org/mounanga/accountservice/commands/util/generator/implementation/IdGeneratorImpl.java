package org.mounanga.accountservice.commands.util.generator.implementation;

import org.mounanga.accountservice.commands.util.generator.IdGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IdGeneratorImpl implements IdGenerator {

    private final CounterRepository counterRepository;

    public IdGeneratorImpl(CounterRepository counterRepository) {
        this.counterRepository = counterRepository;
    }

    @Transactional
    @Override
    public String autoGenerateId() {
        Long id = getNewCounterId();
        return String.format("%09d", id);
    }

    private Long getNewCounterId(){
        Counter savedCounter =  counterRepository.save(new Counter());
        return savedCounter.getId();
    }
}
