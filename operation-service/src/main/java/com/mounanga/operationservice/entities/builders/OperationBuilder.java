package com.mounanga.operationservice.entities.builders;

import com.mounanga.operationservice.entities.Operation;
import com.mounanga.operationservice.enums.Currency;
import com.mounanga.operationservice.enums.Type;

import java.math.BigDecimal;
import java.util.Date;

public class OperationBuilder {

    private final Operation operation = new Operation();

    public OperationBuilder id(String id){
        operation.setId(id);
        return this;
    }

    public OperationBuilder date(Date date){
        operation.setDate(date);
        return this;
    }

    public OperationBuilder currency(Currency currency){
        operation.setCurrency(currency);
        return this;
    }

    public OperationBuilder description(String description){
        operation.setDescription(description);
        return this;
    }

    public OperationBuilder amount(BigDecimal amount){
        operation.setAmount(amount);
        return this;
    }

    public OperationBuilder type(Type type){
        operation.setType(type);
        return this;
    }

    public OperationBuilder accountId(String accountId){
        operation.setAccountId(accountId);
        return this;
    }

    public Operation build(){
        return operation;
    }


}
