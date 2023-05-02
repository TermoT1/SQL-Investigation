package ru.sqlinvestigation.RestAPI.controllers.userDB;

import org.springdoc.api.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sqlinvestigation.RestAPI.models.userDB.JWT.JwtRequest;
import ru.sqlinvestigation.RestAPI.models.userDB.JWT.JwtResponse;
import ru.sqlinvestigation.RestAPI.models.userDB.JWT.JwtResponseAccessToken;
import ru.sqlinvestigation.RestAPI.models.userDB.JWT.RefreshJwtRequest;
import ru.sqlinvestigation.RestAPI.services.userDB.JWT.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest authRequest) {
        final JwtResponse token = authService.login(authRequest);
        System.out.println("login");
        return ResponseEntity.ok(token);
    }

    @PostMapping("/getNewAccessToken")
    public ResponseEntity<JwtResponseAccessToken> getNewAccessToken(@RequestBody RefreshJwtRequest request) throws Exception {
        final JwtResponseAccessToken token = authService.getAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/getNewRefreshToken")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) throws Exception {
        final JwtResponse token = authService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleException(Exception exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage(exception.getMessage()));
    }

}
