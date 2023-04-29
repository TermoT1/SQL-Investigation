package ru.sqlinvestigation.RestAPI.services.userDB.JWT;

import io.jsonwebtoken.Claims;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.sqlinvestigation.RestAPI.models.userDB.*;
import ru.sqlinvestigation.RestAPI.models.userDB.JWT.JwtAuthentication;
import ru.sqlinvestigation.RestAPI.models.userDB.JWT.JwtRequest;
import ru.sqlinvestigation.RestAPI.models.userDB.JWT.JwtResponse;
import ru.sqlinvestigation.RestAPI.models.userDB.RefreshToken;
import ru.sqlinvestigation.RestAPI.services.userDB.UserDetailsServiceImpl;
import ru.sqlinvestigation.RestAPI.services.userDB.UserService;

@Service
public class AuthService {

    private final UserDetailsServiceImpl userDetailsService;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final JwtProvider jwtProvider;

    private final PasswordEncoder passwordEncoder;

    public AuthService(UserDetailsServiceImpl userDetailsService, UserService userService,
                       RefreshTokenService refreshTokenService, JwtProvider jwtProvider,
                       PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;

        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
    }

    public JwtResponse login(@NonNull JwtRequest authRequest) {
        final User user = userService.getByUsername(authRequest.getUsername())
                .orElseThrow(() -> new AuthException("Пользователь не найден"));
        if (passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            final String accessToken = jwtProvider.generateAccessToken(user);
            final String refreshToken = jwtProvider.generateRefreshToken(user);
            refreshTokenService.update(new RefreshToken(user.getId(), refreshToken));
//            refreshStorage.put(user.getUsername(), refreshToken);
            return new JwtResponse(accessToken, refreshToken);
        } else {
            throw new AuthException("Неправильный пароль");
        }
    }

    public JwtResponse getAccessToken(@NonNull String refreshToken) throws Exception {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String userId = claims.getSubject();
            final String saveRefreshToken = refreshTokenService.getRefreshTokenByUserId(Long.valueOf(userId));
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userService.getById(Long.valueOf(userId))
                        .orElseThrow(() -> new AuthException("Пользователь не найден"));
                final String accessToken = jwtProvider.generateAccessToken(user);
                return new JwtResponse(accessToken, null);
            }
        }
        return new JwtResponse(null, null);
    }

    public JwtResponse refresh(@NonNull String refreshToken) throws Exception {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String userId = claims.getSubject();
            final String saveRefreshToken = refreshTokenService.getRefreshTokenByUserId(Long.valueOf(userId));
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userService.getById(Long.valueOf(userId))
                        .orElseThrow(() -> new AuthException("Пользователь не найден"));
                final String accessToken = jwtProvider.generateAccessToken(user);
                final String newRefreshToken = jwtProvider.generateRefreshToken(user);
                refreshTokenService.update(new RefreshToken(user.getId(), newRefreshToken));
                return new JwtResponse(accessToken, newRefreshToken);
            }
        }
        throw new AuthException("Невалидный JWT токен");
    }

    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }

}
