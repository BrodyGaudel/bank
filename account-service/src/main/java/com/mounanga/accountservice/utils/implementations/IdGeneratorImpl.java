package com.mounanga.accountservice.utils.implementations;

import com.mounanga.accountservice.entities.Compter;
import com.mounanga.accountservice.repositories.CompterRepository;
import com.mounanga.accountservice.utils.IdGenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional
public class IdGeneratorImpl implements IdGenerator {

    private static final Logger log = LoggerFactory.getLogger(IdGeneratorImpl.class);

    private final CompterRepository compterRepository;

    public IdGeneratorImpl(CompterRepository compterRepository) {
        this.compterRepository = compterRepository;
    }


    /**
     * auto generate an id as a String
     *
     * @return id generated
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
            compter = compters.get(0);
            compterRepository.deleteById(compter.getId());
        }

        String header = LocalDate.now().format(
                DateTimeFormatter.ofPattern("yyyyMMdd")
        );

        Long increment = compter.getId()+1;
        compterRepository.save(new Compter(increment));
        log.info("id generated");
        return header+increment;
    }
}
