package com.example.KeycloakDemo.response;

import lombok.Data;

import java.util.List;

@Data
public class UserResponse {

    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private boolean enabled;
    List<RoleResponse> roles;


}
