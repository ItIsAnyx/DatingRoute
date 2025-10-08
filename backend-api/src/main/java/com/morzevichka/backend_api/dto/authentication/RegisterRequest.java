package com.morzevichka.backend_api.dto.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @Email(message = "Некорректный формат email")
        @NotBlank(message = "Email не должен быть пустым")
        String email,

        @NotBlank(message = "Логин не может быть пустым")
        String login,

        @NotBlank(message = "Пароль не может быть пустым")
        @Size(min = 8, message = "Пароль должен быть не менее 8 символов")
        String password
) {}
