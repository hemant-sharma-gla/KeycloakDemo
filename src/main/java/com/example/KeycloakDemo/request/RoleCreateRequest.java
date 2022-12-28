package com.example.KeycloakDemo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleCreateRequest {

    private String roleName;
    private String roleDescription;
}
