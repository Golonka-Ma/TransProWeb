package com.example.transpro.service;

import com.example.transpro.model.Role;
import com.example.transpro.model.User;
import com.example.transpro.repository.RoleRepository;
import com.example.transpro.repository.UserRepository;
import com.example.transpro.dto.UserRegistrationDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Dodaj brakującą metodę
    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User save(UserRegistrationDto registration) {
        // Pobierz istniejącą rolę z bazy danych
        Role userRole = roleRepository.findByName("ROLE_USER");
        if (userRole == null) {
            // Jeśli rola nie istnieje, utwórz ją i zapisz
            userRole = Role.builder()
                    .name("ROLE_USER")
                    .build();
            roleRepository.save(userRole);
        }

        // Twórz nowego użytkownika i przypisz mu istniejącą rolę
        User user = User.builder()
                .username(registration.getUsername())
                .password(passwordEncoder.encode(registration.getPassword()))
                .email(registration.getEmail())
                .roles(Collections.singleton(userRole))
                .build();

        return userRepository.save(user);
    }

    // Implementacja metody z UserDetailsService
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Nie znaleziono użytkownika: " + username);
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(role ->
                authorities.add(new SimpleGrantedAuthority(role.getName())));
        return authorities;
    }
}
