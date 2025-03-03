package com.daniel.pineros.security.services;

import java.util.Optional;

import javax.transaction.Transactional;

import com.daniel.pineros.security.entities.User;
import com.daniel.pineros.security.respositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getByUserName(String userName){
        return userRepository.findByUserName(userName);
    }
    public boolean existByUserName(String userName){
        return userRepository.existsByUserName(userName);
    }
    public void save(User user){
        userRepository.save(user);
    }
    public long countUsers() {return userRepository.count();
    }
    
}
