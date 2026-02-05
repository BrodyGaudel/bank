package com.mounanga.customerqueryservice.web;

import com.mounanga.customerqueryservice.dto.CustomerResponseDTO;
import com.mounanga.customerqueryservice.query.GetCustomerByEmailQuery;
import com.mounanga.customerqueryservice.query.GetCustomerByIdQuery;
import com.mounanga.customerqueryservice.query.GetCustomerByNipQuery;
import com.mounanga.customerqueryservice.query.GetCustomersQuery;
import org.axonframework.messaging.responsetypes.ResponseType;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.enums.ParameterIn;


@RestController
@RequestMapping("/api/v1/customers")
@Tag(name = "Customer Management", description = "APIs for managing customer data")
public class CustomerRestController {

    private final QueryGateway queryGateway;

    public CustomerRestController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @Operation(
            summary = "Get customers with pagination and filtering",
            description = "Retrieves a paginated list of customers with optional keyword search and deleted status filter",
            parameters = {
                    @Parameter(
                            name = "keyword",
                            description = "Search keyword for customer name, email, or NIP",
                            example = "John",
                            in = ParameterIn.QUERY,
                            schema = @Schema(type = "string", defaultValue = " ")
                    ),
                    @Parameter(
                            name = "page",
                            description = "Page number (zero-based)",
                            example = "0",
                            in = ParameterIn.QUERY,
                            schema = @Schema(type = "integer", minimum = "0", defaultValue = "0")
                    ),
                    @Parameter(
                            name = "size",
                            description = "Number of items per page",
                            example = "10",
                            in = ParameterIn.QUERY,
                            schema = @Schema(type = "integer", minimum = "1", maximum = "100", defaultValue = "10")
                    ),
                    @Parameter(
                            name = "deleted",
                            description = "Include deleted customers",
                            example = "false",
                            in = ParameterIn.QUERY,
                            schema = @Schema(type = "boolean", defaultValue = "false")
                    )
            }
    )
    @GetMapping
    public List<CustomerResponseDTO> getCustomers(@RequestParam(name = "keyword", defaultValue = " ") String keyword,
                                                  @RequestParam(name = "page", defaultValue = "0") int page,
                                                  @RequestParam(name = "size", defaultValue = "10") int size,
                                                  @RequestParam(name = "deleted", defaultValue = "false") boolean deleted) {
        GetCustomersQuery query = new GetCustomersQuery(keyword, page, size, deleted);
        ResponseType<List<CustomerResponseDTO>> responseType = ResponseTypes.multipleInstancesOf(CustomerResponseDTO.class);
        return queryGateway.query(query, responseType).join();
    }

    @Operation(
            summary = "Get customer by ID",
            description = "Retrieves a single customer by their unique identifier"
    )
    @GetMapping("/{id}")
    public CustomerResponseDTO getCustomerById(
            @Parameter(
                    description = "Customer UUID",
                    required = true,
                    example = "123e4567-e89b-12d3-a456-426614174000"
            )
            @PathVariable UUID id) {
        GetCustomerByIdQuery query = new GetCustomerByIdQuery(id.toString());
        ResponseType<CustomerResponseDTO> responseType = ResponseTypes.instanceOf(CustomerResponseDTO.class);
        return queryGateway.query(query, responseType).join();
    }

    @Operation(
            summary = "Get customer by email",
            description = "Retrieves a single customer by their email address (case-insensitive)"
    )
    @GetMapping("/email/{email}")
    public CustomerResponseDTO getCustomerByEmail(
            @Parameter(
                    description = "Customer email address",
                    required = true,
                    example = "customer@example.com"
            )
            @PathVariable String email) {
        GetCustomerByEmailQuery query = new GetCustomerByEmailQuery(email);
        ResponseType<CustomerResponseDTO> responseType = ResponseTypes.instanceOf(CustomerResponseDTO.class);
        return queryGateway.query(query, responseType).join();
    }

    @Operation(
            summary = "Get customer by NIP",
            description = "Retrieves a single customer by their NIP (Tax Identification Number)"
    )
    @GetMapping("/nip/{nip}")
    public CustomerResponseDTO getCustomerByNip(
            @Parameter(
                    description = "Customer NIP (Tax Identification Number)",
                    required = true,
                    example = "1234567890"
            )
            @PathVariable String nip) {
        GetCustomerByNipQuery query = new GetCustomerByNipQuery(nip);
        ResponseType<CustomerResponseDTO> responseType = ResponseTypes.instanceOf(CustomerResponseDTO.class);
        return queryGateway.query(query, responseType).join();
    }
}