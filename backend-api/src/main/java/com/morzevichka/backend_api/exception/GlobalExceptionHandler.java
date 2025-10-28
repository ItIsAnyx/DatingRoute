package com.morzevichka.backend_api.exception;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final DefaultErrorAttributes defaultErrorAttributes;

    private Map<String, Object> buildDefaultError(HttpServletRequest servletRequest, Throwable ex, HttpStatus status) {
        final Map<String, Object> errorAttributes = defaultErrorAttributes.getErrorAttributes(
                new ServletWebRequest(servletRequest),
                ErrorAttributeOptions.defaults().excluding(ErrorAttributeOptions.Include.STACK_TRACE)
        );

        errorAttributes.put("status", status.value());
        errorAttributes.put("error", status.getReasonPhrase());
        errorAttributes.put("message", ex.getMessage());
        errorAttributes.put("path", servletRequest.getRequestURI());
        return errorAttributes;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> methodArgumentNotValidError(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        final HttpStatus status = HttpStatus.BAD_REQUEST;

        Map<String, String> details = ex
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                                FieldError::getField,
                                value -> Objects.isNull(value.getDefaultMessage()) ? "Invalid Field" : value.getDefaultMessage()
                        )
                );

        Map<String, Object> errorAttributes = buildDefaultError(request, ex, status);
        errorAttributes.put("message", "Validation Failed");
        errorAttributes.put("details", details);

        return ResponseEntity.status(status.value()).body(errorAttributes);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> illegalArgumentError(
            IllegalArgumentException ex,
            HttpServletRequest request
    ) {
        final HttpStatus status = HttpStatus.BAD_REQUEST;

        Map<String, Object> errorAttributes = buildDefaultError(request, ex, status);

        return ResponseEntity.status(status.value()).body(errorAttributes);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> badCredentialsError(
            BadCredentialsException ex,
            HttpServletRequest request
    ) {
        final HttpStatus status = HttpStatus.UNAUTHORIZED;

        Map<String, Object> errorAttributes = buildDefaultError(request, ex, status);

        return ResponseEntity.status(status.value()).body(errorAttributes);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<String> expiredJwtError() {
        return ResponseEntity.status(401).body("JWT");
    }
}
