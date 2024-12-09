package com.example.transpro.service;

import com.example.transpro.model.User;
import com.example.transpro.dto.UserRegistrationDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<User> findAll();
    User findByUsername(String username);
    User save(UserRegistrationDto registration);
}
