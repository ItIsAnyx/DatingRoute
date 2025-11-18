package com.morzevichka.backend_api.infrastructure.exception;

import com.morzevichka.backend_api.api.dto.error.DefaultErrorResponse;
import com.morzevichka.backend_api.api.dto.error.ValidationErrorResponse;
import com.morzevichka.backend_api.infrastructure.exception.chat.ChatNotFoundException;
import com.morzevichka.backend_api.infrastructure.mapper.ErrorMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ErrorMapper mapper;

    private DefaultErrorResponse buildDefaultError(
            HttpServletRequest servletRequest,
            Throwable ex,
            HttpStatus status) {
        return new DefaultErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage(),
                servletRequest.getRequestURI()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> methodArgumentNotValidError(
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

        DefaultErrorResponse defaultErrorResponse = buildDefaultError(request, ex, status);
        defaultErrorResponse.setMessage("Validation Failed");

        ValidationErrorResponse errorResponse = mapper.toValidationDto(defaultErrorResponse, details);

        return ResponseEntity.status(status.value()).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<DefaultErrorResponse> illegalArgumentError(
            IllegalArgumentException ex,
            HttpServletRequest request
    ) {
        final HttpStatus status = HttpStatus.BAD_REQUEST;

        return ResponseEntity
                .status(status.value())
                .body(buildDefaultError(request, ex, status));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<DefaultErrorResponse> notFoundError(
            NotFoundException ex,
            HttpServletRequest request
    ) {
        final HttpStatus status = HttpStatus.NOT_FOUND;

        return ResponseEntity
                .status(status.value())
                .body(buildDefaultError(request, ex, status));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<DefaultErrorResponse> generalError(
            Exception ex,
            HttpServletRequest request
    ) {
        final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        return ResponseEntity
                .status(status.value())
                .body(buildDefaultError(request, ex, status));
    }
}
