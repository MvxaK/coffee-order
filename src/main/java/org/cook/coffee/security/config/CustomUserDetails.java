package org.cook.coffee.security.config;

import lombok.Data;
import org.cook.coffee.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Data
public class CustomUserDetails implements UserDetails {

    private UUID uuid;
    private String email;
    private String passwordHash;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(UserEntity userEntity) {
        this.uuid = userEntity.getUuid();
        this.email = userEntity.getEmail();
        this.passwordHash = userEntity.getPasswordHash();
        this.authorities = List.of(new SimpleGrantedAuthority(userEntity.getRole().getRole().name()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
