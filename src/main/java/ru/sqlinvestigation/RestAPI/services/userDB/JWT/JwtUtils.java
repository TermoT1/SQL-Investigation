package ru.sqlinvestigation.RestAPI.services.userDB.JWT;

import io.jsonwebtoken.Claims;
import ru.sqlinvestigation.RestAPI.models.userDB.JWT.JwtAuthentication;
import ru.sqlinvestigation.RestAPI.models.userDB.Role;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class JwtUtils {

    public JwtUtils() {
    }

    public static JwtAuthentication generate(Claims claims) {
        final JwtAuthentication jwtInfoToken = new JwtAuthentication();
        jwtInfoToken.setRole(String.valueOf(claims.get("role")));
        jwtInfoToken.setUsername(claims.getSubject());
        jwtInfoToken.setUserId(Long.parseLong(String.valueOf(claims.get("userId"))));
        return jwtInfoToken;
    }

    private static Set<Role> getRoles(Claims claims) {
        final List<String> roles = claims.get("role", List.class);
        return roles.stream()
                .map(Role::valueOf)
                .collect(Collectors.toSet());
    }

}
