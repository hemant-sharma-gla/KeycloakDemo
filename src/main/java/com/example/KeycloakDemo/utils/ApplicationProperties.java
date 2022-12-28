package com.example.KeycloakDemo.utils;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


@Component
@Data
@Configuration
public class ApplicationProperties {

    @Value("${keycloak.realm}")
    private String realm;
//    @Value("$(keycloak.authUrl}")
    private String authUrl="http://localhost:8080/auth";
    @Value("${keycloak.clientName}")
    private String clientId;
    @Value("${keycloak.clientSecretKey}")
    private String clientSecretKey;


}
