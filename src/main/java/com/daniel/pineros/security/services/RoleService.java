package com.daniel.pineros.security.services;

import java.util.Optional;

import javax.transaction.Transactional;

import com.daniel.pineros.security.entities.Role;
import com.daniel.pineros.security.enums.RoleList;
import com.daniel.pineros.security.respositories.RoleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    public Optional<Role> getByRoleName(RoleList roleName){
        return roleRepository.findByRoleName(roleName);
    }
    public Role getOrCreateRole(RoleList roleName) {return roleRepository.findByRoleName(roleName)
                .orElseGet(() -> {
                    Role newRole = new Role(roleName);
                    roleRepository.save(newRole);
                    return newRole;
                });
    }
    
    
}
