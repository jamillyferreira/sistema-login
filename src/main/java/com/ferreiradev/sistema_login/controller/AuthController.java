package com.ferreiradev.sistema_login.controller;

import com.ferreiradev.sistema_login.dtos.request.LoginRequestDTO;
import com.ferreiradev.sistema_login.dtos.request.RegisterRequestDTO;
import com.ferreiradev.sistema_login.dtos.response.LoginResponseDTO;
import com.ferreiradev.sistema_login.dtos.response.RegisterResponseDTO;
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
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody @Valid RegisterRequestDTO request) {
        RegisterResponseDTO response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO request) {
        LoginResponseDTO response = authService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }



}
