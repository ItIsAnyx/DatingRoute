package com.morzevichka.backend_api.exception;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {
    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        final Throwable error = getError(webRequest);
        final Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options.excluding(ErrorAttributeOptions.Include.STACK_TRACE));

        errorAttributes.remove("errors");

        if (error instanceof MethodArgumentNotValidException ex) {
            Map<String, String> details = ex.getFieldErrors()
                    .stream()
                    .collect(Collectors.toMap(
                            FieldError::getField,
                            value -> Objects.isNull(value.getDefaultMessage()) ? "Invalid Field" : value.getDefaultMessage()));

            errorAttributes.put("details", details);
        }

        return errorAttributes;
    }
}
