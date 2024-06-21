# BANK Application

The BANK application is a microservice-based banking management system developed using Java, Spring Boot, Spring Cloud, and Spring Security.

## Microservices

1. **Discovery-service**: Spring Eureka Server
2. **Gateway-service**: Spring Cloud Gateway
3. **Notification-service**: Management and sending of notifications via email
4. **User-service**: User management, roles, and authentication
5. **Customer-service**: Bank customers management
6. **Account-Service**: Management of customers' bank accounts, developed using CQRS and Event Sourcing Architecture
7. **Bank-ui**: User interface, developed with Angular, available [here](https://github.com/BrodyGaudel/bank-ui.git)
8. **Bank-admin-ui**: Super administrator user interface for creating other users and managing their roles, available [here](https://github.com/BrodyGaudel/bank-admin-ui.git), developed with Angular

## Technologies Used

- **Java**: JDK 21
- **Angular**: 17.3.3
- **Spring Boot**: v3.3.0
- **Axon Framework** V4.9.3
- **Axon Server**
- **MySQL**

## How to Run the Application

Start the microservices in the order listed above. When starting the `user-service` for the first time, the super administrator is created. Please check the startup logs to retrieve the password and authenticate using `SUPER_ADMIN` as the username. You must change the password thereafter.

### Order of Startup

1. Discovery-service
2. Gateway-service
3. Notification-service
4. User-service
5. Customer-service
6. Account-service
7. Bank-ui
8. Bank-admin-ui

## Repository Links

- **Bank-ui**: [GitHub Repository](https://github.com/BrodyGaudel/bank-ui.git)
- **Bank-admin-ui**: [GitHub Repository](https://github.com/BrodyGaudel/bank-admin-ui.git)

## Author

Brody Gaudel MOUNANGA BOUKA

## Contributing

Contributions are welcome! If you would like to contribute to this project, please fork the repository and use a feature branch. Pull requests are warmly welcome.

### Steps to Contribute

1. Fork the repository
2. Create a feature branch (`git checkout -b feature-branch`)
3. Commit your changes (`git commit -am 'Add new feature'`)
4. Push to the branch (`git push origin feature-branch`)
5. Create a new Pull Request

Feel free to contribute, raise issues, or suggest enhancements. Your feedback is highly appreciated!

---