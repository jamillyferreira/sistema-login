package com.ferreiradev.sistema_login.docs;

import com.ferreiradev.sistema_login.dtos.response.MessageResponse;
import com.ferreiradev.sistema_login.dtos.response.UserResponse;
import com.ferreiradev.sistema_login.exception.ErrorResponse;
import com.ferreiradev.sistema_login.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Tag(name = "Perfil do usuário", description = "Endpoints para consulta do perfil do usuário autenticado")
public interface UserControllerDoc {

    @Operation(
            summary = "Consultar perfil do usuário autenticado",
            description = "Retorna os dados do usuário logado com base no token JWT enviado no header Authorization",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Dados do usuário retornados com sucesso",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))
            ),
            @ApiResponse(responseCode = "401", description = "Não autenticado / token inválido")
    })
    ResponseEntity<UserResponse> me(@Parameter(hidden = true) @AuthenticationPrincipal User user);

}
