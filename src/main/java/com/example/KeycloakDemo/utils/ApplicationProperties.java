package com.example.KeycloakDemo.utils;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
@Data
public class ApplicationProperties {

    @Value("${keycloak.realm}")
    private String realm;
    @Value("$(keycloak.url}")
    private String url;
    @Value("${keycloak.clientId}")
    private String clientId;
    @Value("${keycloak.clientSecretKey}")
    private String clientSecretKey;


}
