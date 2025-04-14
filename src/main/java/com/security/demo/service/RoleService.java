package com.security.demo.service;

import com.security.demo.models.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    public Optional<Role> findByRole(String role);
    public Role save(Role role);
    public List<Role> getAllRoles();
    public List<Role> findById(List<Long> rolesId);
}
