package com.brodygaudel.accountservice.util.implementation;

import com.brodygaudel.accountservice.util.IdGenerator;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class IdGeneratorImpl implements IdGenerator {

    private int counter = 0;
    private static final Lock lock = new ReentrantLock();

    @Override
    public String autoGenerate() {
        // Get the current date and time and Format the date and time according to the desired pattern
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        String formattedDateTime = dateFormat.format(now);

        //Use a lock to ensure exclusive access to subsequent operations.
        lock.lock();
        try {
            // Increments the counter safely
            counter++;
            // Generates the identifier by concatenating the date, formatted time and counter
            return formattedDateTime + String.format("%06d", counter);
        } finally {
            // Release the lock in all cases, even in exceptional cases
            lock.unlock();
        }
    }
}
