package com.mounanga.accountcommandservice.configuration;


import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Configuration class to enable asynchronous processing.
 * This allows methods annotated with @Async to run in separate threads.
 *
 * @author Brody Gaudel MOUNANGA BOUKA
 * @version 1.0
 * @since 1.0
 */
@Configuration
@EnableAsync
public class AsyncConfiguration {
    // Spring Boot provides default async executor
    // Can be customized here if needed
}
