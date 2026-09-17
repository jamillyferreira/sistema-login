    package com.ferreiradev.sistema_login.controller;

    import com.ferreiradev.sistema_login.dtos.response.UserResponse;
    import com.ferreiradev.sistema_login.mapper.AuthMapper;
    import com.ferreiradev.sistema_login.model.User;
    import lombok.RequiredArgsConstructor;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.core.annotation.AuthenticationPrincipal;
    import org.springframework.web.bind.annotation.*;


    @RestController
    @RequiredArgsConstructor
    @RequestMapping("/users")
    public class UserController {

        private final AuthMapper authMapper;

        @GetMapping("/me") // Acessivel somente para usuario logado
        public ResponseEntity<UserResponse> me(@AuthenticationPrincipal User user) {
            return ResponseEntity.ok(authMapper.toRegisterResponseDTO(user));
        }

    }
