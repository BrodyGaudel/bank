# My Bank Admin

My Bank Admin is a web application for bank management. It is developed using Java (version 17), Spring Boot (version 3.1.2), Spring Cloud, Spring Security, Angular (version 16.2.2), MySQL, and Maven.

## Architecture

![Architecture](https://raw.githubusercontent.com/BrodyGaudel/bank/main/architecture.png)

## Microservices

The application is divided into three microservices:

### 1. Customer Service

The Customer Service handles the management of bank customers, including registration, modification, deletion, and more.

### 2. Account Service

The Account Service is responsible for managing bank accounts, including account creation, deletion, modification, as well as handling banking operations like credit and debit transactions.

### 3. User Service

The User Service is in charge of user management and authentication within the application.

## Additional Services

In addition to the core microservices, the application also includes the following services:

### Gateway Service

The Gateway Service serves as a gateway for the application, providing routing and security features using Spring Cloud Gateway.

### Discovery Service

The Discovery Service acts as a registration and discovery server for microservices using Eureka Discovery Server.

### Web UI

The Web UI is the user interface for administrative purposes, developed using Angular and Bootstrap.

## Prerequisites

Before running the application, ensure that you have the following prerequisites installed:

- Java 17
- Angular CLI 16.2.2
- MySQL
- Maven

## Getting Started

1. Clone this repository.
2. Configure the database connection in the application properties.
3. Build and run each microservice using Maven
4. You must start the services in the following order: discovery-service, gateway-service, customer-service, account-service, user-service, web-ui.
5. Start the Gateway Service and Discovery Service.
6. Launch the Web UI to access the admin interface.

## Usage

- Access the Web UI at [http://localhost:4200](http://localhost:4200) to manage the bank administration.
- Access the Discovery Service at [http://localhost:8761](http://localhost:8761) 

## Contributing

If you'd like to contribute to this project, please follow the [CONTRIBUTING.md](CONTRIBUTING.md) guidelines.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.

## Acknowledgments

- Special thanks to the Spring and Angular communities for their excellent frameworks and resources.
- Inspired by the world of banking and financial services.

## Author and Developer

- Brody Gaudel MOUNANGA BOUKA
