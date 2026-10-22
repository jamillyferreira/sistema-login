package com.ferreiradev.sistema_login.controller;

import com.ferreiradev.sistema_login.docs.AuthControllerDoc;
import com.ferreiradev.sistema_login.dtos.request.*;
import com.ferreiradev.sistema_login.dtos.response.MessageResponse;
import com.ferreiradev.sistema_login.dtos.response.UserResponse;
import com.ferreiradev.sistema_login.dtos.response.AuthResponse;
import com.ferreiradev.sistema_login.mapper.AuthMapper;
import com.ferreiradev.sistema_login.security.CookieService;
import com.ferreiradev.sistema_login.service.AuthService;
import com.ferreiradev.sistema_login.service.PasswordResetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController implements AuthControllerDoc {

   private final AuthService authService;
   private final PasswordResetService passwordResetService;
   private final CookieService cookieService;


   @PostMapping("/register")
   public ResponseEntity<AuthResponse> register(@RequestBody @Valid RegisterRequest request) {
      AuthResponse response = authService.register(request);
      return ResponseEntity.status(HttpStatus.CREATED)
              .header(HttpHeaders.SET_COOKIE, cookieService.createRefreshCookie(response.refreshToken()).toString())
              .body(response);
   }

   @PostMapping("/login")
   public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
      AuthResponse response = authService.login(request);

      return ResponseEntity.ok()
              .header(HttpHeaders.SET_COOKIE, cookieService.createRefreshCookie(response.refreshToken()).toString())
              .body(response);
   }

   @PostMapping("/refresh")
   public ResponseEntity<AuthResponse> refresh(
           @CookieValue(name = CookieService.REFRESH_TOKEN_NAME, required = false) String refreshToken) {
      AuthResponse response = authService.refresh(refreshToken);

      return ResponseEntity.ok()
              .header(HttpHeaders.SET_COOKIE, cookieService.createRefreshCookie(response.refreshToken()).toString())
              .body(response);
   }

   @PostMapping("/logout")
   public ResponseEntity<Void> logout(
           @CookieValue(name = CookieService.REFRESH_TOKEN_NAME, required = false) String refreshToken) {
      authService.logout(refreshToken);

      return ResponseEntity.noContent()
              .header(HttpHeaders.SET_COOKIE, cookieService.clearRefreshCookie().toString())
              .build();
   }

   @PostMapping("/forgot-password")
   public ResponseEntity<MessageResponse> forgotPassword(@RequestBody @Valid ForgotPasswordRequest request) {
      passwordResetService.requestForgotPassword(request.email());

      MessageResponse response = new MessageResponse(
              "Se o e-mail existir, um código de redefinição será enviado.");
      return ResponseEntity.ok(response);
   }

   @PostMapping("/reset-password")
   public ResponseEntity<MessageResponse> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
      passwordResetService.resetPassword(request.code(), request.newPassword());
      MessageResponse response = new MessageResponse("Senha redefinida com sucesso");
      return ResponseEntity.ok(response);

   }


}
