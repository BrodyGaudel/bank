# Bank Management Application

![Bank Application](https://raw.githubusercontent.com/BrodyGaudel/bank/refs/heads/main/illustration.jpg)

## Overview
The **Bank Management Application** is a microservices-based system designed to handle various banking operations, such as customer management, account transactions (credit, debit, transfer), authentication, and notifications. The project implements **CQRS** (Command Query Responsibility Segregation) and **Event Sourcing** patterns using the **Axon Framework** for the **Account Service**, ensuring scalability and performance while handling large amounts of banking data.

The frontend of the application is developed using **Angular**, while the backend services are built with **Spring Boot**. All services are containerized with **Docker** and integrated into a CI/CD pipeline using **Jenkins**. The project also features code quality analysis using **SonarQube** and artifact management with **Nexus**.

## Architecture
The system follows a **microservices architecture** and utilizes various Spring Cloud technologies, including **Spring Cloud Eureka Server** for service discovery and **Spring Cloud Reactive Gateway** for routing.

### Microservices
1. **Discovery Service**: A Eureka Server for service discovery and registration.
2. **Gateway Service**: A reactive API gateway to route requests to respective microservices.
3. **Customer Service**: Manages customer information within the system.
4. **Account Service**: Manages bank accounts, including operations like credit, debit, and transfers. Implements **CQRS** and **Event Sourcing** using the **Axon Framework**.
5. **Authentication Service**: Handles user authentication, role-based access control, and authorization for bank personnel.
6. **Notification Service**: Manages email notifications for various banking operations.

## Technologies
The project uses a range of technologies to deliver a reliable, scalable, and efficient banking management solution:

- **Java 21**
- **Spring Boot 3.3.4**
- **Axon Framework** with **Axon Server** for CQRS and Event Sourcing.
- **Spring Cloud OpenFeign** for inter-service communication.
- **MySQL** as the primary database.
- **Angular** for the frontend UI.
- **Docker** for containerization.
- **SonarQube** for code quality analysis.
- **Nexus** for artifact management.
- **Jenkins** for CI/CD pipeline automation.

## Service Start-up Order
To ensure proper initialization of services, they need to be started in the following order:

1. **Axon Server**
2. **Discovery Service**
3. **Gateway Service**
4. **Notification Service**
5. **Authentication Service**
6. **Customer Service**
7. **Account Service**

> **Note**: The **Axon Server** should be started first as it handles event storage and routing for the **Account Service**. For more information about Axon Server, please refer to its [official documentation](https://www.axoniq.io/products/axon-server).

## Axon Framework and Axon Server
This application leverages the **Axon Framework** for implementing **CQRS** and **Event Sourcing** in the **Account Service**. Axon Framework provides the tools necessary to build scalable and maintainable event-driven systems. It uses **Axon Server** for event storage and routing.

- Axon Framework Documentation: [Axon Framework](https://www.axoniq.io/products/axon-framework)
- Axon Server Documentation: [Axon Server](https://www.axoniq.io/products/axon-server)

## Getting Started

### Prerequisites
- **Java 21**
- **Maven**
- **Node.js** (for the frontend)
- **MySQL** (configured with the necessary schema)
- **Axon Server** (start Axon Server before running the microservices)

### Backend Setup

1. Clone the backend repository:
    ```bash
    git clone https://github.com/BrodyGaudel/bank
    cd bank
    ```

2. Package the microservices with Maven:
    ```bash
    mvn clean install
    ```

3. Start each service individually using Maven:
    - Start the **Axon Server** (make sure it's running before starting any other services):
      ```bash
      java -jar axonserver.jar
      ```

    - Start the **Discovery Service**:
      ```bash
      cd discovery-service
      mvn spring-boot:run
      ```

    - Start the **Gateway Service**:
      ```bash
      cd gateway-service
      mvn spring-boot:run
      ```

    - Follow the same steps for the other services in the order mentioned above.

### Frontend Setup

1. Clone the frontend repository:
    ```bash
    git clone https://github.com/BrodyGaudel/bank-ui
    cd bank-ui
    ```

2. Install dependencies and run the application:
    ```bash
    npm install
    npm start
    ```

3. Access the frontend via `http://localhost:4200`.

### Jenkins and CI/CD Pipeline
The project includes a **Jenkinsfile** that defines the CI/CD pipeline. Ensure Jenkins is configured with the necessary plugins to support Docker and Maven builds.

## Features

- **Customer Management**: CRUD operations for bank customers.
- **Account Operations**: Handles bank account creation, credits, debits, and transfers.
- **CQRS and Event Sourcing**: The **Account Service** uses Axon Framework for scalable event-driven architecture.
- **Authentication & Authorization**: Role-based access control for bank employees.
- **Email Notifications**: Automated email notifications for customer and account operations.

## Microservices Communication
Inter-service communication is achieved using **Spring Cloud OpenFeign**, enabling synchronous calls between microservices.

## Monitoring & Quality
- **SonarQube** is integrated for code quality analysis.
- **Nexus** is used for artifact management, ensuring efficient dependency management across services.
- **Jenkins** is used for CI/CD automation, handling the build, test, and deployment pipelines.

## Project Structure

bank/ 
    ├── discovery-service/
    ├── gateway-service/
    ├── customer-service/
    ├── account-service/
    ├── authentication-service/
    ├── notification-service/


## Contributions
Contributions are welcome! Please feel free to submit a pull request or open an issue for any bugs or feature requests.

## License
This project is licensed under the MIT License.

## Contact
For any inquiries, please contact:
- **Brody Gaudel** - brodymounanga@gmail.com

## Repositories
- **Backend**: [Bank Backend Repository](https://github.com/BrodyGaudel/bank)
- **Frontend**: [Bank Frontend Repository](https://github.com/BrodyGaudel/bank-ui)