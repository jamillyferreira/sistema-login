    package com.ferreiradev.sistema_login.controller;

    import com.ferreiradev.sistema_login.docs.UserControllerDoc;
    import com.ferreiradev.sistema_login.dtos.response.UserResponse;
    import com.ferreiradev.sistema_login.mapper.AuthMapper;
    import com.ferreiradev.sistema_login.model.User;
    import com.ferreiradev.sistema_login.service.UserService;
    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.core.annotation.AuthenticationPrincipal;
    import org.springframework.web.bind.annotation.*;


    @Slf4j
    @RestController
    @RequiredArgsConstructor
    @RequestMapping("/api/users")
    public class UserController implements UserControllerDoc {

        private final UserService userService;
        private final AuthMapper authMapper;

        @Override
        @GetMapping("/me") // Acessivel somente para usuario logado
        public ResponseEntity<UserResponse> me(@AuthenticationPrincipal User user) {
            return ResponseEntity.ok(userService.getAuthenticatedUser(user));
        }

    }
