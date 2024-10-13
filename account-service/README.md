
---

# Account Service

## Overview
The **Account Service** is a microservice responsible for managing bank account data within the **Bank Management Application**. It implements the CQRS (Command Query Responsibility Segregation) and Event Sourcing patterns using the **Axon Framework** and **Axon Server**. The service provides a RESTful API to perform account operations such as creation, updating, and querying account details and transactions.

This service is built with **Java 21**, **Spring Boot**, **Axon Framework**, and **Maven** as the build tool, ensuring scalability, maintainability, and a clear separation between command and query responsibilities.

## Technologies Used
- **Java 21**
- **Spring Boot** 3.3.4
- **Axon Framework** for CQRS and Event Sourcing
- **Axon Server** for event storage and messaging
- **Maven**
- **Spring Web** for building the REST API

## API Endpoints
The **Account Service** exposes the following RESTful API endpoints for managing account data:

### 1. Create Account
- **URL**: `/accounts/commands/create`
- **Method**: `POST`
- **Description**: Creates a new account associated with a customer.
- **Request Body**: Expects an `AccountRequestDTO` with account details.
- **Response**: Returns the ID of the newly created account.

### 2. Update Account Status
- **URL**: `/accounts/commands/update`
- **Method**: `PUT`
- **Description**: Updates the status of an existing account.
- **Request Body**: Expects an `UpdateStatusRequestDTO` with the account ID and new status.
- **Response**: Returns the ID of the updated account.

### 3. Credit Account
- **URL**: `/accounts/commands/credit`
- **Method**: `POST`
- **Description**: Credits a specified amount to an account.
- **Request Body**: Expects an `OperationRequestDTO` with account ID and amount.
- **Response**: Returns the ID of the credit operation.

### 4. Debit Account
- **URL**: `/accounts/commands/debit`
- **Method**: `POST`
- **Description**: Debits a specified amount from an account.
- **Request Body**: Expects an `OperationRequestDTO` with account ID and amount.
- **Response**: Returns the ID of the debit operation.

### 5. Transfer Between Accounts
- **URL**: `/accounts/commands/transfer`
- **Method**: `POST`
- **Description**: Transfers an amount from one account to another.
- **Request Body**: Expects a `TransferRequestDTO` with transfer details.
- **Response**: Returns a list of IDs for the transfer operations.

### 6. Delete Account
- **URL**: `/accounts/commands/delete/{id}`
- **Method**: `DELETE`
- **Description**: Deletes an account by its ID.
- **Response**: Returns the ID of the deleted account.

### 7. Get Account by ID
- **URL**: `/accounts/queries/get-account/{id}`
- **Method**: `GET`
- **Description**: Retrieves account details by its ID.
- **Response**: Returns an `AccountResponseDTO` with account details.

### 8. Get Account by Customer ID
- **URL**: `/accounts/queries/find-account/{customerId}`
- **Method**: `GET`
- **Description**: Retrieves account details by customer ID.
- **Response**: Returns an `AccountResponseDTO` with account details.

### 9. Get Operation by ID
- **URL**: `/accounts/queries/get-operation/{id}`
- **Method**: `GET`
- **Description**: Retrieves operation details by its ID.
- **Response**: Returns an `OperationResponseDTO` with operation details.

### 10. Get All Operations for an Account
- **URL**: `/accounts/queries/all-operations`
- **Method**: `GET`
- **Description**: Retrieves a paginated list of operations for a specific account.
- **Parameters**:
    - `accountId` (required) - The ID of the account.
    - `page` (optional, default: 0) - Specifies the page number for pagination.
    - `size` (optional, default: 9) - Specifies the number of operations per page.
- **Response**: Returns a list of `OperationResponseDTO` with operations.

## Data Transfer Objects (DTOs)

### AccountResponseDTO
```java
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class AccountResponseDTO {
    private String id;
    private AccountStatus status;
    private BigDecimal balance;
    private Currency currency;
    private String customerId;
    private LocalDateTime createdDate;
    private String createdBy;
    private LocalDateTime lastModifiedDate;
    private String lastModifiedBy;
}
```
### JSON EXAMPLE
```json
{
    "id": "1",
    "status": "ACTIVATED",
    "balance": 1000.00,
    "currency": "USD",
    "customerId": "123456789",
    "createdDate": "2023-01-01T12:00:00",
    "createdBy": "admin",
    "lastModifiedDate": "2023-01-01T12:00:00",
    "lastModifiedBy": "admin"
}
```

### OperationResponseDTO
```java
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class OperationResponseDTO {
    private String id;
    private LocalDateTime dateTime;
    private BigDecimal amount;
    private OperationType type;
    private String description;
    private String createdBy;
    private String accountId;
}
```
### JSON EXAMPLE
```json
{
    "id": "1",
    "dateTime": "2023-01-01T12:00:00",
    "amount": 100.00,
    "type": "CREDIT",
    "description": "Deposit",
    "createdBy": "admin",
    "accountId": "1"
}
```

### AccountRequestDTO
```java
public record AccountRequestDTO(
        @NotBlank(message="field 'customerId' is mandatory: it can not be blank") String customerId,
        @NotNull(message="field 'currency' is mandatory: it can not be null") Currency currency) {}
```
### JSON EXAMPLE
```json
{
    "customerId": "123456789",
    "currency": "USD"
}
```

### OperationRequestDTO
```java
public record OperationRequestDTO(
        @NotBlank(message="field 'accountId' is mandatory: it can not be blank") String accountId,
        @NotNull(message ="field 'amount' is mandatory: it can not be null") @Positive(message = "amount must be positive") BigDecimal amount,
        @NotBlank(message="field 'description' is mandatory: it can not be blank") String description) {}
```
### JSON EXAMPLE
```json
{
    "accountId": "1",
    "amount": 100.00,
    "description": "Deposit"
}
```

### UpdateStatusRequestDTO
```java
public record UpdateStatusRequestDTO(
        @NotBlank(message="field 'accountId' is mandatory: it can not be blank") String accountId,
        @NotNull(message = "field 'status' is mandatory: it can not be null") AccountStatus status) {}
```
### JSON EXAMPLE
```json
{
    "accountId": "1",
    "status": "SUSPENDED"
}
```

---