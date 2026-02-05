package com.mounanga.accountcommandservice.web;

import com.mounanga.accountcommandservice.bank.RIBGenerator;
import com.mounanga.accountcommandservice.command.*;
import com.mounanga.accountcommandservice.dto.AccountRequestDTO;
import com.mounanga.accountcommandservice.dto.OperationDTO;
import com.mounanga.accountcommandservice.enums.AccountStatus;
import com.mounanga.accountcommandservice.util.CommandFactory;
import jakarta.validation.Valid;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.enums.ParameterIn;


@RestController
@RequestMapping("/api/v1/accounts")
@Tag(
        name = "Account Management",
        description = "APIs for managing bank accounts, including creation, activation, and transaction operations"
)
public class AccountRestController {

    private final CommandGateway commandGateway;
    private final RIBGenerator ribGenerator;

    public AccountRestController(CommandGateway commandGateway, RIBGenerator ribGenerator) {
        this.commandGateway = commandGateway;
        this.ribGenerator = ribGenerator;
    }

    @Operation(
            summary = "Create a new bank account",
            description = "Creates a new bank account with a unique RIB (Relevé d'Identité Bancaire) generated automatically"
    )
    @PostMapping
    public CompletableFuture<String> create(@RequestBody @Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Account creation request details",
            required = true,
            content = @Content(schema = @Schema(implementation = AccountRequestDTO.class))
    ) AccountRequestDTO dto) {
        String rib = ribGenerator.generate();
        CreateAccountCommand command = CommandFactory.buildCreate(rib, dto);
        return commandGateway.send(command);
    }

    @Operation(
            summary = "Activate or deactivate an account",
            description = "Changes the status of an existing account to either ACTIVATED or DEACTIVATED"
    )
    @PatchMapping("/{rib}")
    public CompletableFuture<String> activate(
            @Parameter(
                    name = "rib",
                    description = "Account RIB (Bank Account Identifier)",
                    required = true,
                    example = "FR7630001007941234567890185",
                    in = ParameterIn.PATH,
                    schema = @Schema(type = "string", pattern = "^[A-Z]{2}[0-9]{2}[A-Z0-9]{1,30}$")
            )
            @PathVariable String rib,

            @Parameter(
                    name = "status",
                    description = "Desired account status",
                    required = true,
                    example = "ACTIVATED",
                    in = ParameterIn.QUERY,
                    schema = @Schema(implementation = AccountStatus.class, allowableValues = {"ACTIVATED", "DEACTIVATED"})
            )
            @RequestParam(name = "status") AccountStatus status) {
        switch (status) {
            case ACTIVATED -> {
                ActivateAccountCommand command = CommandFactory.buildActivate(rib);
                return commandGateway.send(command);
            }
            case DEACTIVATED -> {
                DeactivateAccountCommand command = CommandFactory.buildDeactivate(rib);
                return commandGateway.send(command);
            }
            default -> throw new IllegalArgumentException("Invalid status: " + status);
        }
    }

    @Operation(
            summary = "Credit an account",
            description = "Adds funds to a bank account with the specified amount"
    )
    @PutMapping("/credit")
    public CompletableFuture<String> credit(@RequestBody @Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Credit operation details including RIB and amount",
            required = true,
            content = @Content(schema = @Schema(implementation = OperationDTO.class))
    ) OperationDTO dto) {
        CreditAccountCommand command = CommandFactory.buildCredit(dto);
        return commandGateway.send(command);
    }

    @Operation(
            summary = "Debit an account",
            description = "Withdraws funds from a bank account with the specified amount"
    )
    @PutMapping("/debit")
    public CompletableFuture<String> debit(@RequestBody @Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Debit operation details including RIB and amount",
            required = true,
            content = @Content(schema = @Schema(implementation = OperationDTO.class))
    ) OperationDTO dto) {
        DebitAccountCommand command = CommandFactory.buildDebit(dto);
        return commandGateway.send(command);
    }
}
