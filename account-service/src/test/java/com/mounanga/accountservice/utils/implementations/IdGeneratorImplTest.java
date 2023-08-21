package com.mounanga.accountservice.utils.implementations;

import com.mounanga.accountservice.repositories.CompterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IdGeneratorImplTest {

    private IdGeneratorImpl idGenerator;

    @Autowired
    private CompterRepository repository;

    @BeforeEach
    void setUp() {
        idGenerator = new IdGeneratorImpl(repository);
        repository.deleteAll();
    }

    @Test
    void testAutoGenerate() {
        String id = idGenerator.autoGenerate();
        assertNotNull(id);
        int numberOfCharacters = id.length();
        assertEquals(16, numberOfCharacters);
    }
}