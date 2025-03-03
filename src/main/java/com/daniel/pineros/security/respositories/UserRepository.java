package com.daniel.pineros.security.respositories;

import java.util.Optional;

import com.daniel.pineros.security.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends
        JpaRepository<User, String> {
    Optional<User> findByUserName(String userName);
    boolean existsByUserName(String userName);
    
}
