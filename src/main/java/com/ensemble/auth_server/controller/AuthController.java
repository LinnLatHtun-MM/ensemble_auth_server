package com.ensemble.auth_server.controller;

import com.ensemble.auth_server.dto.*;
import com.ensemble.auth_server.entity.UserAccount;
import com.ensemble.auth_server.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/portal/register")
    public UserAccount portalRegister(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/portal/login")
    public TokenResponse portalLogin(@RequestBody LoginRequest req) {
        String token = authService.login(req.email(), req.password(), "PORTAL");
        return new TokenResponse(token, "Bearer", 3600);
    }

    @PostMapping("/client/login")
    public ResponseEntity<?> clientLogin(@RequestBody LoginRequest req) {
        try {
            String token = authService.login(req.email(), req.password(), "CLIENT");
            return ResponseEntity.ok(new TokenResponse(token, "Bearer", 3600));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).body(new ErrorResponse("Invalid credentials"));
        }
    }
}

