package com.mounanga.customerqueryservice.util;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * ApplicationProperties holds application-level configuration values.
 * <p>
 * This class is responsible for retrieving and exposing configuration properties
 * defined in the application's external configuration files (e.g., application.yml,
 * application.yml).
 * </p>
 *
 * <p>
 * In this case, it provides access to the Keycloak client ID used for authentication
 * and authorization purposes.
 * </p>
 *
 * @author Brody Gaudel MOUNANGA BOUKA
 */
@Component
@Getter
public class ApplicationProperties {


    @Value("${keycloak.client-id}")
    private String keycloakClientId;

    @Value("${keycloak.client-secret}")
    private String keycloakClientSecret;

    @Value("${kafka.bootstrap-servers:localhost:9092}")
    private String kafkaBootstrapServers;

    @Value("${kafka.consumer.group-id:customer-query-group}")
    private String kafkaGroupId;
}
