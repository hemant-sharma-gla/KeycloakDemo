package com.example.KeycloakDemo.service;

import com.example.KeycloakDemo.exceptionhandler.ValidationException;
import com.example.KeycloakDemo.request.RoleCreateRequest;
import com.example.KeycloakDemo.response.RoleResponse;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {

    @Autowired
    private KeycloakInstanceBuilder keycloakInstanceBuilder;
    @Autowired
    private UserService userService;

    public List<RoleResponse> getAllRoles() {
        return keycloakInstanceBuilder.getInstanceWithRealm().roles().list().stream().map(role -> new RoleResponse(role.getName(), role.getDescription())).collect(Collectors.toList());
    }

    public String createRole(RoleCreateRequest request) {
        RoleRepresentation roleRepresentation = new RoleRepresentation();
        roleRepresentation.setName(request.getRoleName());
        roleRepresentation.setDescription(request.getRoleDescription());
        try {
            keycloakInstanceBuilder.getInstanceWithRealm().roles().create(roleRepresentation);
        } catch (Exception e) {
            throw new ValidationException(HttpStatus.BAD_REQUEST.value(), "Unable to create role, please try again some time...");
        }
        return "Role Created Successfully.";
    }

    public RoleResponse getRoleByRoleName(String roleName) {
        try {
            RoleRepresentation role = keycloakInstanceBuilder.getInstanceWithRealm().roles().get(roleName).toRepresentation();
            return new RoleResponse(role.getName(), role.getDescription());
        } catch (Exception e) {
            throw new ValidationException(HttpStatus.BAD_REQUEST.value(), "Role Not Found...");
        }
    }

    public void assignRoleToUser(String role, String id) {
        RoleRepresentation roleRepresentation;
        try {
            roleRepresentation = keycloakInstanceBuilder.getInstanceWithRealm().roles().get(role).toRepresentation();
            keycloakInstanceBuilder.getInstanceWithRealm().users().get(id).roles().realmLevel().add(Collections.singletonList(roleRepresentation));
        } catch (NotFoundException exception) {
            throw new ValidationException(HttpStatus.BAD_REQUEST.value(), "Invalid Role Provided");
        } catch (Exception e) {
            throw new ValidationException(HttpStatus.BAD_REQUEST.value(), "Unable to assign role to user ,please try again after some time");
        }
    }

    public List<RoleResponse> getUserRoles(String userName) {
        try {

          return   keycloakInstanceBuilder
                    .getInstanceWithRealm()
                    .users()
                    .get(keycloakInstanceBuilder.getInstanceWithRealm()
                            .users().search(userName)
                            .get(0).getId()).roles()
                    .realmLevel().listAll().stream()
                    .map(role -> new RoleResponse(role.getName(), role.getDescription())).collect(Collectors.toList());

        } catch (Exception e) {
            throw new ValidationException(HttpStatus.BAD_REQUEST.value(), "User Not Found");
        }
    }
}
