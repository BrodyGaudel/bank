# Customer Service

## Overview
The **Customer Service** is a microservice responsible for managing customer data within the **Bank Management Application**. It provides a RESTful API to perform CRUD operations (Create, Read, Update, Delete) on customer records and supports searching for customers based on specific criteria.

This service is built with **Java 21**, **Spring Boot**, **MySQL**, and **Maven** as the build tool. It follows best practices for microservice architecture, ensuring scalability, maintainability, and ease of deployment.

## Technologies Used
- **Java 21**
- **Spring Boot** 3.3.4
- **Maven**
- **MySQL** for database
- **Spring Data JPA** for persistence
- **Spring Web** for building the REST API

## API Endpoints
The **Customer Service** exposes the following RESTful API endpoints for managing customer data:

### 1. Get Customer by ID
- **URL**: `/customers/get/{id}`
- **Method**: `GET`
- **Description**: Retrieves a customer's details by their unique ID.
- **Response**: Returns a `CustomerResponseDTO` containing the customer’s details.

### 2. Get Customer by CIN
- **URL**: `/customers/find/{cin}`
- **Method**: `GET`
- **Description**: Retrieves a customer's details by their CIN (Customer Identification Number).
- **Response**: Returns a `CustomerResponseDTO` containing the customer’s details.

### 3. Get All Customers
- **URL**: `/customers/list`
- **Method**: `GET`
- **Description**: Retrieves a paginated list of all customers.
- **Parameters**:
    - `page` (optional, default: 0) - Specifies the page number for pagination.
    - `size` (optional, default: 9) - Specifies the number of customers per page.
- **Response**: Returns a `CustomerPageResponseDTO` containing a list of customers.

### 4. Search Customers by Keyword
- **URL**: `/customers/search`
- **Method**: `GET`
- **Description**: Searches for customers based on a keyword (e.g., name, CIN).
- **Parameters**:
    - `keyword` (optional, default: "") - The search term.
    - `page` (optional, default: 0) - Page number for pagination.
    - `size` (optional, default: 9) - Number of customers per page.
- **Response**: Returns a paginated `CustomerPageResponseDTO` with matching customers.

### 5. Create a New Customer
- **URL**: `/customers/create`
- **Method**: `POST`
- **Description**: Creates a new customer record.
- **Request Body**: Expects a `CustomerRequestDTO` with customer details.
- **Response**: Returns a `CustomerResponseDTO` containing the newly created customer’s details.

### 6. Update an Existing Customer
- **URL**: `/customers/update/{id}`
- **Method**: `PUT`
- **Description**: Updates an existing customer record by their ID.
- **Request Body**: Expects a `CustomerRequestDTO` with updated customer details.
- **Response**: Returns a `CustomerResponseDTO` containing the updated customer’s details.

### 7. Delete a Customer
- **URL**: `/customers/delete/{id}`
- **Method**: `DELETE`
- **Description**: Deletes a customer by their ID.
- **Response**: No content, the customer record is removed from the database.

## Data Transfer Objects (DTOs)

### CustomerRequestDTO
```java
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class CustomerRequestDTO {
    @NotBlank(message = "field 'firstname' is mandatory: it can not be blank")
    private String firstname;

    @NotBlank(message = "field 'lastname' is mandatory: it can not be blank")
    private String lastname;

    @NotBlank(message = "field 'placeOfBirth' is mandatory: it can not be blank")
    private String placeOfBirth;

    @NotNull(message = "field 'dateOfBirth' is mandatory: it can not be null")
    @AgeMinimum(min = 18, message = "Customer must be at least 18 years old")
    private LocalDate dateOfBirth;

    @NotBlank(message = "field 'nationality' is mandatory: it can not be blank")
    private String nationality;

    @NotNull(message = "field 'gender' is mandatory: it can not be null")
    private Gender gender;

    @NotBlank(message = "field 'cin' is mandatory: it can not be blank")
    private String cin;

    @NotBlank(message = "field 'email' is mandatory: it can not be blank")
    @Email(message = "field 'email' is not well formatted")
    private String email;
}
```
#### JSON EXAMPLE
```json
{
"firstname": "John",
"lastname": "Doe",
"placeOfBirth": "New York",
"dateOfBirth": "1990-01-01",
"nationality": "American",
"gender": "MALE",
"cin": "123456789",
"email": "john.doe@example.com"
}
```
### CustomerResponseDTO

```java
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class CustomerResponseDTO {
    private String id;
    private String firstname;
    private String lastname;
    private String placeOfBirth;
    private LocalDate dateOfBirth;
    private String nationality;
    private Gender gender;
    private String cin;
    private String email;
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
"firstname": "John",
"lastname": "Doe",
"placeOfBirth": "New York",
"dateOfBirth": "1990-01-01",
"nationality": "American",
"gender": "MALE",
"cin": "123456789",
"email": "john.doe@example.com",
"createdDate": "2023-01-01T12:00:00",
"createdBy": "admin",
"lastModifiedDate": "2023-01-01T12:00:00",
"lastModifiedBy": "admin"
}
```

### CustomerPageResponseDTO

```java
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class CustomerPageResponseDTO {
    private int totalPages;
    private int size;
    private long totalElements;
    private int numberOfElements;
    private int number;
    private boolean hasContent;
    private boolean hasNext;
    private boolean hasPrevious;
    private boolean isFirst;
    private boolean isLast;
    private List<CustomerResponseDTO> customers;
}
```

### JSON EXAMPLE

```json
{
  "totalPages": 5,
  "size": 9,
  "totalElements": 45,
  "numberOfElements": 9,
  "number": 0,
  "hasContent": true,
  "hasNext": true,
  "hasPrevious": false,
  "isFirst": true,
  "isLast": false,
  "customers": [
    {
      "id": "1",
      "firstname": "John",
      "lastname": "Doe",
      "placeOfBirth": "New York",
      "dateOfBirth": "1990-01-01",
      "nationality": "American",
      "gender": "MALE",
      "cin": "123456789",
      "email": "john.doe@example.com",
      "createdDate": "2023-01-01T12:00:00",
      "createdBy": "admin",
      "lastModifiedDate": "2023-01-01T12:00:00",
      "lastModifiedBy": "admin"
    },
    {
      "id": "2",
      "firstname": "Jane",
      "lastname": "Doe",
      "placeOfBirth": "Los Angeles",
      "dateOfBirth": "1992-02-02",
      "nationality": "American",
      "gender": "FEMALE",
      "cin": "987654321",
      "email": "jane.doe@example.com",
      "createdDate": "2023-01-01T12:00:00",
      "createdBy": "admin",
      "lastModifiedDate": "2023-01-01T12:00:00",
      "lastModifiedBy": "admin"
    }
  ]
}
```

