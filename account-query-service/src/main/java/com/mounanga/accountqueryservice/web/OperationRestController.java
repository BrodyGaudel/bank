package com.mounanga.accountqueryservice.web;

import com.mounanga.accountqueryservice.entity.OperationResponseDTO;
import com.mounanga.accountqueryservice.query.GetOperationByAccountIdQuery;
import com.mounanga.accountqueryservice.query.GetOperationByIdQuery;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.axonframework.messaging.responsetypes.ResponseType;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/operations")
public class OperationRestController {

    private final QueryGateway queryGateway;

    public OperationRestController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping
    public List<OperationResponseDTO> getOperations(@RequestParam(name = "accountId") String accountId,
                                                    @RequestParam(name = "page") int page,
                                                    @RequestParam(name = "size") int size) {
        GetOperationByAccountIdQuery query = new GetOperationByAccountIdQuery(accountId, page, size);
        ResponseType<List<OperationResponseDTO>> responseType = ResponseTypes.multipleInstancesOf(OperationResponseDTO.class);
        return queryGateway.query(query, responseType).join();
    }

    @GetMapping("/{id}")
    public OperationResponseDTO getOperationById(@PathVariable UUID id) {
        GetOperationByIdQuery query = new GetOperationByIdQuery(id.toString());
        ResponseType<OperationResponseDTO> responseType = ResponseTypes.instanceOf(OperationResponseDTO.class);
        OperationResponseDTO dto = queryGateway.query(query, responseType).join();
        if (dto == null) {
            throw new ResourceNotFoundException("Operation with id "+id+"not found");
        }
        return dto;
    }
}
