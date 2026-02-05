package com.mounanga.accountcommandservice.bank;

import com.mounanga.accountcommandservice.exception.RibException;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;

/**
 * Service for generating secure and unique French RIB (Relevé d'Identité Bancaire) numbers.
 * This implementation follows the French banking standard for RIB generation and ensures
 * cryptographic security and uniqueness of generated RIBs.
 *
 * <p>The RIB format consists of:
 * <ul>
 *   <li>Bank code (5 digits)</li>
 *   <li>Branch code (5 digits)</li>
 *   <li>Account number (11 digits)</li>
 *   <li>RIB key (2 digits calculated with a specific algorithm)</li>
 * </ul>
 *
 * <p>This service uses cryptographically secure random number generation and includes
 * collision detection mechanisms to guarantee uniqueness of generated RIBs.</p>
 *
 * <p>The generation algorithm follows the standard French RIB calculation:
 * Key = 97 - ((89 × bank_code + 15 × branch_code + 3 × account_number) modulo 97)</p>
 *
 * @author Brody Gaudel MOUNANGA BOUKA
 * @version 1.0
 * @since 1.0
 */
@Service
public class SecureRIBGenerator implements RIBGenerator {

    /**
     * Bank code (5 digits) - Fixed for the institution.
     */
    private static final String BANK_CODE = "30004";

    /**
     * Branch code (5 digits) - Fixed for the institution.
     */
    private static final String BRANCH_CODE = "00001";

    /**
     * Maximum number of generation attempts to prevent infinite loops in case of collisions.
     */
    private static final int MAX_ATTEMPTS = 10;

    /**
     * Repository for checking RIB uniqueness and persistence.
     */
    private final RIBRepository ribRepository;

    /**
     * Cryptographically secure random number generator.
     */
    private final SecureRandom secureRandom;

    /**
     * Constructs a new SecureRIBGenerator with the specified repository.
     *
     * @param ribRepository the repository used for checking RIB uniqueness and persistence
     */
    public SecureRIBGenerator(RIBRepository ribRepository) {
        this.ribRepository = ribRepository;
        this.secureRandom = new SecureRandom();
    }

    /**
     * Generates a unique and valid French RIB (Relevé d'Identité Bancaire).
     * This method ensures the generated RIB is unique by checking against existing RIBs
     * in the repository and retrying if a collision occurs.
     *
     * <p>The generation process:
     * <ol>
     *   <li>Generates a secure random account number (11 digits)</li>
     *   <li>Calculates the RIB key using the standard French algorithm</li>
     *   <li>Assembles the full RIB from bank code, branch code, account number, and key</li>
     *   <li>Checks for uniqueness in the repository</li>
     *   <li>Retries up to {@link #MAX_ATTEMPTS} times if a collision occurs</li>
     * </ol></p>
     *
     * @return a unique and valid French RIB string
     * @throws RuntimeException if a unique RIB cannot be generated after the maximum number of attempts
     */
    @Override
    @Transactional
    public String generate() {
        int attempts = 0;

        while (attempts < MAX_ATTEMPTS) {
            String accountNumber = generateAccountNumber();
            String ribKey = calculateRIBKey(accountNumber);
            String fullRIB = BANK_CODE + BRANCH_CODE + accountNumber + ribKey;

            // Quick uniqueness check
            if (!ribRepository.checkExistsFast(fullRIB)) {
                try {
                    RIB rib = new RIB(fullRIB);
                    ribRepository.save(rib);
                    return fullRIB;
                } catch (DataIntegrityViolationException e) {
                    // Collision detected, retry
                    attempts++;
                    continue;
                }
            }
            attempts++;
        }

        throw new RibException("Unable to generate a unique RIB after " + MAX_ATTEMPTS + " attempts");
    }

    /**
     * Generates an 11-digit account number using cryptographically secure random generation.
     * The generated number is formatted to ensure exactly 11 digits with leading zeros if needed.
     *
     * @return an 11-digit account number as a string
     */
    private @NotNull String generateAccountNumber() {
        // Use SecureRandom for cryptographically secure generation
        long accountNum = Math.abs(secureRandom.nextLong() % 100000000000L);
        return String.format("%011d", accountNum);
    }

    /**
     * Calculates the RIB key (2 digits) according to the standard French algorithm.
     * The key is calculated as: 97 - ((89 × bank_code + 15 × branch_code + 3 × account_number) modulo 97)
     *
     * @param accountNumber the 11-digit account number
     * @return the 2-digit RIB key as a string
     */
    private @NotNull String calculateRIBKey(String accountNumber) {
        long bank = Long.parseLong(BANK_CODE);
        long branch = Long.parseLong(BRANCH_CODE);
        long account = Long.parseLong(accountNumber);

        long calculation = (89 * bank + 15 * branch + 3 * account) % 97;
        long key = 97 - calculation;

        return String.format("%02d", key);
    }
}