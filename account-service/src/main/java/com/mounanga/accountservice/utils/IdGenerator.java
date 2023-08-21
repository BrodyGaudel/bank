package com.mounanga.accountservice.utils;

/**
 * unique bank account number generation service
 */
public interface IdGenerator {

    /**
     * automatically generate a unique bank account number
     * @return id a unique bank account number
     */
    String autoGenerate();
}
