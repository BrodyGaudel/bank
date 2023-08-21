package com.mounanga.accountservice.utils.implementations;

import com.mounanga.accountservice.entities.Compter;
import com.mounanga.accountservice.repositories.CompterRepository;
import com.mounanga.accountservice.utils.IdGenerator;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Transactional
@Service
public class IdGeneratorImpl implements IdGenerator {

    private static final Logger log = LoggerFactory.getLogger(IdGeneratorImpl.class);

    private final CompterRepository compterRepository;

    public IdGeneratorImpl(CompterRepository compterRepository) {
        this.compterRepository = compterRepository;
    }

    /**
     * automatically generate a unique bank account number
     *
     * @return id a unique bank account number
     */
    @Override
    public String autoGenerate() {
        log.info("In autoGenerate()");
        List<Compter> compters = compterRepository.findAll();
        Compter compter;
        if(compters.isEmpty()) {
            compter = new Compter((long) 999999);
        }
        else {
            compter = compters.get(compters.size() - 1);
            compterRepository.deleteById(compter.getId());
        }
        Long increment = compter.getId()+1;
        compterRepository.save(new Compter(increment));
        log.info("id generated");
        return getHeader()+increment;
    }

    private @NotNull String getHeader(){
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        return currentDate.format(formatter);
    }
}
