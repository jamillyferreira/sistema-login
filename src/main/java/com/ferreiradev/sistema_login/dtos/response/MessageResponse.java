package com.ferreiradev.sistema_login.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta genérica de operações de redefinição de senha")
public record MessageResponse(
        @Schema(description = "Mensagem informativa sobre o resultado da operação")
        String message
) {
}
