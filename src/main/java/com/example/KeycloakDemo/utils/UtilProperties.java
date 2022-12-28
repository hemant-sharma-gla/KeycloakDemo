package com.example.KeycloakDemo.utils;

import com.example.KeycloakDemo.exceptionhandler.ValidationException;
import com.example.KeycloakDemo.request.UserCreateRequest;
import com.example.KeycloakDemo.service.KeycloakInstanceBuilder;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UtilProperties {

    @Autowired
    private KeycloakInstanceBuilder keycloakInstanceBuilder;

    public void validateUserName(String userName) {
        var userList = keycloakInstanceBuilder.getInstanceWithRealm().users().list();
        if (!userList.isEmpty()) {
            userList.forEach(ele -> {
                if (ele.getUsername().equalsIgnoreCase(userName)) {
                    throw new ValidationException(HttpStatus.BAD_REQUEST.value(), "User already exists with this username...");
                }
            });
        }
    }

    public String saveUser(UserCreateRequest request) {
        validateUserName(request.getUsername());
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setFirstName(request.getFirstName());
        userRepresentation.setLastName(request.getLastName());
        userRepresentation.setEmail(request.getEmail());
        userRepresentation.setUsername(request.getUsername());
        userRepresentation.setEnabled(true);
        userRepresentation.singleAttribute("phoneNumber", request.getPhoneNumber());

        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(request.getPassword());
        userRepresentation.setCredentials(List.of(passwordCredentials));

        userRepresentation.setRequiredActions(List.of(ApplicationConstants.VERIFY_EMAIL));

        try {
            return CreatedResponseUtil.getCreatedId(keycloakInstanceBuilder.getInstanceWithRealm().users().create(userRepresentation));
        } catch (Exception e) {
            throw new ValidationException(HttpStatus.BAD_REQUEST.value(), "Unable to create user ,please try again after some time...");
        }
    }
}
