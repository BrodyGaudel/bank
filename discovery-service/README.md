
---

# Discovery Service

## Overview
The **Discovery Service** is a service registry that uses **Eureka** to enable other microservices to register and discover available services within the application. It is essential in a microservices architecture for managing communication between services, thereby facilitating the scalability and resilience of applications.

This service is built with **Java 21**, **Spring Boot**, and **Spring Cloud Netflix Eureka**.

## Technologies Used
- **Java 21**
- **Spring Boot** 3.3.4
- **Spring Cloud Netflix Eureka** for service discovery
- **Maven**

## Configuration
### Application Properties
The application configuration is primarily handled in the `application.properties` file. Below are the properties defined for the discovery service:

```properties
server.port=8761
spring.application.name=DISCOVERY-SERVICE

# Do not register the server itself as a client
eureka.client.fetch-registry=false

# Does not register itself in the service registry
eureka.client.register-with-eureka=false
```

### Explanation of Properties
- `server.port`: Sets the port on which the discovery service will be accessible (default is 8761).
- `spring.application.name`: Specifies the application name used to identify the service in the registry.
- `eureka.client.fetch-registry`: Determines whether the Eureka server should fetch the registry of services. In this case, it is disabled since the server should not behave as a client.
- `eureka.client.register-with-eureka`: Indicates that the server should not register itself in the service registry.

## API Endpoints
The **Discovery Service** does not provide a direct REST API for interacting with services, but you can access the **Eureka** dashboard to view registered services.

### Eureka Dashboard
- **URL**: `http://localhost:8761`
- **Description**: Access the Eureka dashboard to see all registered services, their statuses, and other relevant information.

## Main Application Class
Here is the main application class that starts the Eureka server:

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class DiscoveryServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(DiscoveryServiceApplication.class, args);
    }
}
```

### Explanation of the Code
- `@EnableEurekaServer`: Annotation that enables the Eureka server.
- `@SpringBootApplication`: Indicates that this class is a primary Spring Boot configuration and activates the application startup.
- The `main` method starts the Spring application.

## Conclusion
The **Discovery Service** plays a crucial role in managing communication between microservices by enabling dynamic discovery. It provides a simple interface for registering and discovering services, which is essential for the scalability and resilience of microservices applications.

---