package com.example.KeycloakDemo.service;

import com.example.KeycloakDemo.exceptionhandler.ValidationException;
import com.example.KeycloakDemo.request.UserCreateRequest;
import com.example.KeycloakDemo.response.UserResponse;
import com.example.KeycloakDemo.utils.ApplicationProperties;
import com.example.KeycloakDemo.utils.UtilProperties;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private ApplicationProperties keycloakConfigs;
    @Autowired
    private KeycloakInstanceBuilder keycloakInstanceBuilder;
    @Autowired
    private UtilProperties utilProperties;
    @Autowired
    private RoleService roleService;

    public String createUser(UserCreateRequest request) {
        String id = utilProperties.saveUser(request);
        roleService.assignRoleToUser(request.getRole(), id);
        return "User created Successfully.";
    }

    public List<UserResponse> getAllUsers() {
        List<UserRepresentation> users = keycloakInstanceBuilder.getInstance().realm(keycloakConfigs.getRealm()).users().list();
        List<UserResponse> userResponsesList = new ArrayList<>();
        users.forEach(user ->  {
            UserResponse  userResponse = new UserResponse();
            BeanUtils.copyProperties(user, userResponse);
            userResponse.setRoles(roleService.getUserRoles(user.getUsername()));
            userResponsesList.add(userResponse);
        });
        return userResponsesList;
    }

    public UserResponse getUserByUserName(String username) {
        try {
            UserRepresentation user = keycloakInstanceBuilder.getInstance().realm(keycloakConfigs.getRealm()).users().search(username).get(0);
            UserResponse userResponse = new UserResponse();
            BeanUtils.copyProperties(user, userResponse);
            userResponse.setRoles(roleService.getUserRoles(user.getUsername()));
            return userResponse;
        } catch (Exception e) {
            throw new ValidationException(HttpStatus.BAD_REQUEST.value(), "User Not Found.");
        }
    }

}
