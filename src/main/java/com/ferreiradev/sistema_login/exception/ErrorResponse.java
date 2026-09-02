package com.ferreiradev.sistema_login.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        String type,
        String title,
        Integer status,
        String detail,
        List<String> errors,
        String path,
        Instant timestamp
) {
    public ErrorResponse(String type, String title, Integer status, String detail, String path, Instant timestamp) {
        this(type, title, status, detail, null, path, timestamp);
    }
}
