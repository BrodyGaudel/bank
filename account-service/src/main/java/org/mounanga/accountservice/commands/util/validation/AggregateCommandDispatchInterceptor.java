package org.mounanga.accountservice.commands.util.validation;

import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.jetbrains.annotations.NotNull;
import org.mounanga.accountservice.commands.command.CreateAccountCommand;
import org.mounanga.accountservice.commands.command.DeleteAccountCommand;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.BiFunction;

@Component
public class AggregateCommandDispatchInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {

    private final AccountEmailCustomerIdRepository repository;

    public AggregateCommandDispatchInterceptor(AccountEmailCustomerIdRepository repository) {
        this.repository = repository;
    }

    @NotNull
    @Override
    public CommandMessage<?> handle(@NotNull CommandMessage<?> message) {
        return MessageDispatchInterceptor.super.handle(message);
    }

    @NotNull
    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(@NotNull List<? extends CommandMessage<?>> messages) {
        return (i,m) -> {
            if(CreateAccountCommand.class.equals(m.getPayloadType())){
                validateCreateAccountCommand(m);
            }
            else if(DeleteAccountCommand.class.equals(m.getPayloadType())){
                validateDeleteAccountCommand(m);
            }
            return m;
        };
    }

    private void validateCreateAccountCommand(@NotNull CommandMessage<?> m){
        final CreateAccountCommand command = (CreateAccountCommand) m.getPayload();
        if(repository.findByEmail(command.getEmail()) != null){
            throw new IllegalArgumentException(String.format("Account with customer's email %s already exists", command.getEmail()));
        }
        if(repository.findByCustomerId(command.getCustomerId()) != null){
            throw new IllegalArgumentException(String.format("Account with customer's id %s already exists", command.getCustomerId()));
        }
    }

    private void validateDeleteAccountCommand(@NotNull CommandMessage<?> m){
        final DeleteAccountCommand command = (DeleteAccountCommand) m.getPayload();
        if(!repository.existsById(command.getId())){
            throw new IllegalArgumentException(String.format("Account with id %s does not exist", command.getId()));
        }
    }

}
