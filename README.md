# Bank Application

## Description
Bank Application is a banking management application developed with a microservices' architecture. It facilitates the management of customers, bank accounts, financial transactions, users, and notifications. The frontend is developed using Angular and Bootstrap to provide a modern and responsive user interface.

## Microservices Architecture
- **discovery-service**: Discovery service based on Eureka Server for dynamic service registration and discovery.
- **gateway-service**: Gateway service based on Spring Cloud Gateway for centralizing and securing access to the various microservices.
- **notification-service**: Notification service for sending emails.
- **customer-service**: Service for managing customers.
- **account-service**: Service for managing bank accounts and transactions (credit, debit, transfer), developed with CQRS and Event Sourcing architecture using Axon Framework.
- **user-service**: Service for managing users, roles, and authentication, using Spring Security and java-jwt.

## Frontend
- **admin-bank-ui**: Admin user interface developed with Angular and Bootstrap.

## Technologies Used
- **Backend**:
    - Java
    - Spring Boot (with Spring Security and Spring Cloud)
    - Axon Framework
    - Maven
    - Axon Server
    - MySQL
    - Docker
    - SonarQube
    - Jenkins
    - Nexus

- **Frontend**:
    - Angular
    - Typescript
    - HTML
    - CSS
    - Bootstrap

- **Libraries**:
    - java-jwt
    - auth-angular

- **Modeling**:
    - UML

## Deployment Instructions

### Prerequisites
- Docker
- Docker Compose
- Maven
- Node.js and npm (for the frontend)
- Java JDK 21+

### Deployment Steps

1. **Clone the repository**:
    ```sh
    git clone https://github.com/BrodyGaudel/bank.git
    cd bank
    ```

2. **Build Docker images**:
    ```sh
    docker-compose build
    ```

3. **Start the services**:
    ```sh
    docker-compose up
    ```

4. **Access the application**:
    - Frontend: `http://localhost:4200`
    - Eureka Dashboard: `http://localhost:8761`
    - Gateway: `http://localhost:8888`

## Usage

### Frontend
- Access the admin interface through the browser.
- Use the available features to manage customers, bank accounts, users, and notifications.

### Backend
- Microservices communicate with each other via Eureka Server for discovery and Spring Cloud Gateway for request management.
- Use Axon Server for managing events and commands in the `account-service`.

## Contribution

1. **Fork the project**
2. **Create a feature branch** (`git checkout -b feature/your-feature-name`)
3. **Commit your changes** (`git commit -m 'Add some feature'`)
4. **Push to the branch** (`git push origin feature/your-feature-name`)
5. **Open a Pull Request**

## License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Authors
- Brody Gaudel MOUNANGA BOUKA

## Contact
For any questions, please contact [brodymounanga@gmail.com](mailto:brodymounanga@gmail.com).
