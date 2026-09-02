package com.ferreiradev.sistema_login.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExistsException(
            EmailAlreadyExistsException ex, WebRequest request) {

        String path = ((ServletWebRequest) request).getRequest().getRequestURI();

        ErrorResponse response = new ErrorResponse(
                "about:blank",
                HttpStatus.CONFLICT.getReasonPhrase(),
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                path,
                Instant.now()
        );

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidCredentialException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentialException(
            InvalidCredentialException ex, WebRequest request) {

        String path = ((ServletWebRequest) request).getRequest().getRequestURI();

        ErrorResponse response = new ErrorResponse(
                "about: blank",
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                HttpStatus.UNAUTHORIZED.value(),
                ex.getMessage(),
                path,
                Instant.now()
        );

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage()).toList();

        String path = ((ServletWebRequest) request).getRequest().getRequestURI();

        ErrorResponse response = new ErrorResponse(
                "about:blank",
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                HttpStatus.BAD_REQUEST.value(),
                "Erro de validação nos campos enviados",
                errors,
                path,
                Instant.now()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
