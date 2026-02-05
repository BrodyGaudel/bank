package com.mounanga.customercommandservice.configuration;

import com.mounanga.customercommandservice.validation.AggregateCommandDispatchInterceptor;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.common.Registration;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Axon Framework components within the Customer Command Service.
 * This class sets up and configures Axon's command handling infrastructure, including
 * command bus configuration and interceptor registration.
 * <p>
 * The configuration implements {@link DisposableBean} to ensure proper cleanup of
 * resources when the Spring application context is destroyed.
 * </p>
 *
 * @see CommandBus
 * @see SimpleCommandBus
 * @see AggregateCommandDispatchInterceptor
 * @see DisposableBean
 * @author Brody Gaudel MOUNANGA BOUKA
 * @since 1.0
 * @version 1.0
 */
@Configuration
public class AxonConfiguration implements DisposableBean {

    /**
     * Holds the registration of the dispatch interceptor with the command bus.
     * Used for proper resource cleanup during application shutdown.
     */
    private Registration registration;

    /**
     * Creates and configures an Axon {@link CommandBus} with a command dispatch interceptor.
     * <p>
     * The command bus is built using Axon's {@link SimpleCommandBus} and is configured
     * with an {@link AggregateCommandDispatchInterceptor} that performs validation
     * and preprocessing of commands before they are dispatched to command handlers.
     * </p>
     *
     * @param interceptor the command dispatch interceptor that validates and processes
     *                    commands before they reach the command handlers
     * @return a configured {@link CommandBus} instance ready for use
     * @throws IllegalArgumentException if the interceptor is null
     * @see SimpleCommandBus
     * @see AggregateCommandDispatchInterceptor
     */
    @Bean
    public CommandBus commandBus(AggregateCommandDispatchInterceptor interceptor) {
        CommandBus commandBus = SimpleCommandBus.builder().build();
        registration = commandBus.registerDispatchInterceptor(interceptor);
        return commandBus;
    }

    /**
     * Cleans up resources when the Spring application context is destroyed.
     * <p>
     * This method is part of the {@link DisposableBean} interface and ensures that
     * the registration of the dispatch interceptor is properly closed to prevent
     * memory leaks and ensure clean shutdown of Axon components.
     * </p>
     *
     * @see Registration#close()
     */
    @Override
    public void destroy() {
        if (registration != null) {
            registration.close();
        }
    }
}
