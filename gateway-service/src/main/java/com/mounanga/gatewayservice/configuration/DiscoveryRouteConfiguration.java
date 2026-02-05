package com.mounanga.gatewayservice.configuration;

import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DiscoveryRouteConfiguration {

    /**
     *This code configures a bean in Spring Cloud Gateway that uses the discovery service to
     *  dynamically retrieve route information from registered service instances,
     *  providing automatic route discovery in Spring Cloud-based infrastructure.
     * @param client it is an interface provided by Spring Cloud Netflix
     *                                  to interact with the discovery service (e.g. Eureka, Consul, etc.)
     *                                  in a reactive way.
     * @param properties it is a Spring Cloud Netflix configuration class that contains
     *                                     properties related to service discovery.
     * @return DiscoveryClientRouteDefinitionLocator it is a component provided
     * by Spring Cloud Gateway that connects to the discovery service
     * (such as Eureka) and automatically retrieves route information from registered
     * service instances. It then generates the route definitions for
     * the Spring Cloud Gateway router using this information.
     */
    @Bean
    DiscoveryClientRouteDefinitionLocator discoveryClientRouteDefinitionLocator(ReactiveDiscoveryClient client,
                                                                                DiscoveryLocatorProperties properties){
        return new DiscoveryClientRouteDefinitionLocator(client, properties);
    }
}
