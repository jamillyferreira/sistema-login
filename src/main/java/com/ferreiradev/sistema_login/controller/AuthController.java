package com.ferreiradev.sistema_login.controller;

import com.ferreiradev.sistema_login.dtos.request.LoginRequest;
import com.ferreiradev.sistema_login.dtos.request.RefreshRequest;
import com.ferreiradev.sistema_login.dtos.request.RegisterRequest;
import com.ferreiradev.sistema_login.dtos.response.UserResponse;
import com.ferreiradev.sistema_login.dtos.response.TokenResponse;
import com.ferreiradev.sistema_login.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody @Valid RegisterRequest request) {
        UserResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest request) {
        TokenResponse response = authService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestBody @Valid RefreshRequest request) {
        TokenResponse response = authService.refresh(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout (@RequestBody @Valid RefreshRequest request) {
        authService.logout(request);
        return ResponseEntity.noContent().build();
    }



}
