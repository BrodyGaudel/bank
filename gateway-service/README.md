
---

# Gateway Service

## Overview
The **Gateway Service** acts as a single entry point for routing requests to various microservices in a cloud-based architecture. It uses **Spring Cloud Gateway** and integrates with a discovery service (like **Eureka**) to dynamically route requests to registered services. This setup allows for flexible routing, load balancing, and the application of cross-cutting concerns such as authentication and logging.

This service is built with **Java 21**, **Spring Boot**, and **Spring Cloud Reactive Gateway**.

## Technologies Used
- **Java 21**
- **Spring Boot** 3.3.4
- **Spring Cloud Reactive Gateway** for routing and filtering
- **Spring Cloud Netflix Eureka** for service discovery
- **Maven**

## Configuration
### Application YAML
The application configuration is primarily handled in the `application.yml` file. Below are the configurations defined for the gateway service:

```yaml
server:
  port: 8888

spring:
  application:
    name: GATEWAY-SERVICE

  cloud:
    discovery:
      enabled: true

    gateway:
      globalcors:
        corsConfigurations:
          '[/**]':
            allowedOrigins: ${ALLOWED_ORIGINS:http://localhost:4200,http://localhost:8761,http://localhost:8887,http://localhost:8886}
            allowedHeaders:
              - Content-Type
              - Authorization
            exposedHeaders:
              - Content-Type
              - Authorization
            allowedMethods:
              - GET
              - POST
              - PUT
              - DELETE
              - PATCH

eureka:
  instance:
    preferIpAddress: true
  client:
    service-url:
      defaultZone: ${DISCOVERY_SERVICE:http://localhost:8761/eureka}

management:
  endpoints:
    web:
      exposure:
        include: "*"
```

### Explanation of Properties
- `server.port`: Sets the port on which the gateway service will be accessible (default is 8888).
- `spring.application.name`: Specifies the application name used to identify the gateway service in the application.
- `spring.cloud.discovery.enabled`: Enables service discovery capabilities for the gateway.
- `spring.cloud.gateway.globalcors`: Configures global CORS (Cross-Origin Resource Sharing) settings for all routes.
- `eureka.client.service-url.defaultZone`: Configures the Eureka server URL for service discovery.
- `management.endpoints.web.exposure.include`: Specifies which management endpoints should be exposed.

## API Endpoints
The **Gateway Service** does not directly expose its own REST API. Instead, it acts as a proxy for routing requests to other registered services. The routing logic can be defined in the Spring Cloud Gateway configuration, allowing it to dynamically forward requests to the appropriate service based on the defined routes.

### CORS Configuration
CORS settings allow specified origins to interact with the gateway service, providing flexibility for frontend applications hosted on different ports.

## Main Application Class
Here is the main application class that starts the gateway service:

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayServiceApplication.class, args);
    }

    /**
     * This method configures a bean in Spring Cloud Gateway that uses the discovery service to
     * dynamically retrieve route information from registered service instances,
     * providing automatic route discovery in Spring Cloud-based infrastructure.
     * 
     * @param reactiveDiscoveryClient Interface for interacting with the discovery service (e.g. Eureka).
     * @param discoveryLocatorProperties Configuration class for service discovery properties.
     * @return DiscoveryClientRouteDefinitionLocator that retrieves route definitions from registered services.
     */
    @Bean
    DiscoveryClientRouteDefinitionLocator discoveryClientRouteDefinitionLocator(ReactiveDiscoveryClient reactiveDiscoveryClient,
                                                                                DiscoveryLocatorProperties discoveryLocatorProperties){
        return new DiscoveryClientRouteDefinitionLocator(reactiveDiscoveryClient, discoveryLocatorProperties);
    }
}
```

### Explanation of the Code
- `@SpringBootApplication`: Indicates that this class is a primary Spring Boot configuration and activates the application startup.
- `discoveryClientRouteDefinitionLocator()`: This method creates a bean that enables the gateway to automatically retrieve and define routes based on the services registered with the discovery service (Eureka). It uses:
    - `ReactiveDiscoveryClient`: An interface for reactive interaction with service registries.
    - `DiscoveryLocatorProperties`: A configuration class containing properties related to service discovery.

## Conclusion
The **Gateway Service** simplifies and centralizes routing within microservices architectures. By leveraging **Spring Cloud Gateway** and service discovery, it facilitates seamless communication between services, while also providing essential cross-cutting features like CORS support.

---