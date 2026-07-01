package org.cook.coffee.controller.api;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.cook.coffee.security.auth.AuthResponse;
import org.cook.coffee.security.auth.LoginRequest;
import org.cook.coffee.security.auth.RegisterRequest;
import org.cook.coffee.service.interfaces.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static org.cook.coffee.security.jwt.JwtAuthenticationEntryPoint.JWT_COOKIE_NAME;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthApiController {

    private final AuthService authService;

    @Value("${jwt.expiration}")
    private int jwtExpiration;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request, HttpServletResponse response){

        AuthResponse authResponse = authService.login(request);

        Cookie cookie = new Cookie(JWT_COOKIE_NAME, authResponse.getToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(jwtExpiration);
        response.addCookie(cookie);

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request, HttpServletResponse response){

        AuthResponse authResponse = authService.register(request);

        Cookie cookie = new Cookie(JWT_COOKIE_NAME, authResponse.getToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(jwtExpiration);
        response.addCookie(cookie);

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletResponse response){

        Cookie cookie = new Cookie(JWT_COOKIE_NAME, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.ok(Map.of("message", "Logout successful"));
    }

}
