package org.mounanga.accountservice.commands.configuration;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.common.Registration;
import org.mounanga.accountservice.commands.util.validation.AggregateCommandDispatchInterceptor;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonConfiguration implements DisposableBean {

    private Registration registration;

    @Bean
    public CommandBus commandBus(AggregateCommandDispatchInterceptor interceptor) {
        CommandBus commandBus = SimpleCommandBus.builder().build();
        registration = commandBus.registerDispatchInterceptor(interceptor);
        return commandBus;
    }

    @Override
    public void destroy() {
        if (registration != null) {
            registration.close();
        }
    }
}
