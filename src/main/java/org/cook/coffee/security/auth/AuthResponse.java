package org.cook.coffee.security.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    private String token;
    private String type = "Bearer";
    private String email;
    private UUID uuid;
    private String role;

}
