package org.cook.coffee.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.cook.coffee.entity.RoleEntity;
import org.cook.coffee.entity.UserEntity;
import org.cook.coffee.model.Role;
import org.cook.coffee.repository.RoleRepository;
import org.cook.coffee.repository.UserRepository;
import org.cook.coffee.security.auth.AuthResponse;
import org.cook.coffee.security.auth.LoginRequest;
import org.cook.coffee.security.auth.RegisterRequest;
import org.cook.coffee.security.jwt.JwtService;
import org.cook.coffee.service.interfaces.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {

        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new IllegalArgumentException("Email already exists");
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(request.getEmail());
        userEntity.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        RoleEntity roleEntity = roleRepository.findByRole(Role.ROLE_USER);
        userEntity.setRole(roleEntity);

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String jwt = jwtService.generateToken(userDetails);
        String role = userEntity.getRole().getRole().name();

        return new AuthResponse(jwt, "Bearer", userEntity.getEmail(), userEntity.getId(), role);
    }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String jwt = jwtService.generateToken(userDetails);

        UserEntity userEntity = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("There is no user with email -> " + request.getEmail()));

        String role = userEntity.getRole().getRole().name();

        return new AuthResponse(jwt, "Bearer", userEntity.getEmail(), userEntity.getId(), role);
    }
}
