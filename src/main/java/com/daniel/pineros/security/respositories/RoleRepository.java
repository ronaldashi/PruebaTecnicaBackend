package com.daniel.pineros.security.respositories;

import java.util.Optional;

import com.daniel.pineros.security.entities.Role;
import com.daniel.pineros.security.enums.RoleList;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository <Role, Integer> {
    Optional<Role> findByRoleName(RoleList roleName);
    
}
