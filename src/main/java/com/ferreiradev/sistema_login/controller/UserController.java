package com.ferreiradev.sistema_login.controller;

import com.ferreiradev.sistema_login.dtos.request.LoginRequestDTO;
import com.ferreiradev.sistema_login.dtos.request.RegisterRequestDTO;
import com.ferreiradev.sistema_login.dtos.response.RegisterResponseDTO;
import com.ferreiradev.sistema_login.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody @Valid RegisterRequestDTO request) {
        RegisterResponseDTO response = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<RegisterResponseDTO> login(@RequestBody @Valid LoginRequestDTO request) {
        RegisterResponseDTO response = userService.loginUser(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
