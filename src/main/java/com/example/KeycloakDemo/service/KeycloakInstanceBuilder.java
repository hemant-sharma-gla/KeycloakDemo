package com.example.KeycloakDemo.service;


import com.example.KeycloakDemo.utils.ApplicationProperties;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static com.example.KeycloakDemo.utils.ApplicationConstants.CLIENT_KEY;

@Component
public class KeycloakInstanceBuilder {

    @Autowired
    ApplicationProperties keycloakConfigs;
    private static final ConcurrentHashMap<String, Keycloak> keycloakClients = new ConcurrentHashMap<>();

    public Keycloak keycloakClient() {
        return KeycloakBuilder.builder().
                serverUrl(keycloakConfigs.getAuthUrl()).
                realm(keycloakConfigs.getRealm()).grantType(OAuth2Constants.CLIENT_CREDENTIALS).
                clientId(keycloakConfigs.getClientId()).
                clientSecret(keycloakConfigs.getClientSecretKey()).
                resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build()).build();
    }

    public Keycloak getInstance() {
        Keycloak keycloak = keycloakClients.get(CLIENT_KEY);

        if(Objects.isNull(keycloak)) {
            keycloak = keycloakClient();
            keycloakClients.put(CLIENT_KEY, keycloak);
        }
        return keycloak;
    }


    public RealmResource getInstanceWithRealm() {
        return getInstance().realm(keycloakConfigs.getRealm());
    }
}
