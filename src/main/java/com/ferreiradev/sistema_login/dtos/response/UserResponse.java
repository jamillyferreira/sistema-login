package com.ferreiradev.sistema_login.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
@Schema(description = "Dados públicos do usuário cadastrado")
public record UserResponse(
        @Schema(description = "Identificador único do usuário")
        UUID id,

        @Schema(description = "Nome completo do usuário")
        String name,

        @Schema(description = "E-mail do usuário")
        String email,

        @Schema(description = "Data e hora de criação da conta")
        @JsonProperty("created_at")
        Instant createdAt
) {

}
