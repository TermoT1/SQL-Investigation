package ru.sqlinvestigation.RestAPI.models.userDB.JWT;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.sqlinvestigation.RestAPI.models.userDB.Role;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public class JwtAuthentication implements Authentication {
    private boolean authenticated;
    private long userId;
    private String username;
    private String role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role)); }

    @Override
    public Object getCredentials() { return null; }

    @Override
    public Object getDetails() { return null; }

    @Override
    public Object getPrincipal() { return username; }

    @Override
    public boolean isAuthenticated() { return authenticated; }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() { return username; }


    public void setUsername(String username) {
        this.username = username;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}