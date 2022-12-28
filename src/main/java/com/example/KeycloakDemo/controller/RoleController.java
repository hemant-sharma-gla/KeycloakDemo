package com.example.KeycloakDemo.controller;

import com.example.KeycloakDemo.request.RoleCreateRequest;
import com.example.KeycloakDemo.response.RoleResponse;
import com.example.KeycloakDemo.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping
    public String createRole(@RequestBody RoleCreateRequest request) {
        return roleService.createRole(request);
    }

    @GetMapping
    public List<RoleResponse> getAllRoles() {
        return roleService.getAllRoles();
    }

    @GetMapping("/{roleName}")
    public RoleResponse getRoleDetailsByRoleName(@PathVariable String roleName) {
        return roleService.getRoleByRoleName(roleName);
    }

    @GetMapping("/user/{userName}")
    public List<RoleResponse> getUserRoles(@PathVariable String userName) {
        return roleService.getUserRoles(userName);
    }

}
