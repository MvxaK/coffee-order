package org.cook.coffee.service.interfaces;

import org.cook.coffee.security.auth.AuthResponse;
import org.cook.coffee.security.auth.LoginRequest;
import org.cook.coffee.security.auth.RegisterRequest;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);

}
