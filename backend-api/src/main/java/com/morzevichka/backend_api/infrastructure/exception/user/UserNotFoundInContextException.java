package com.morzevichka.backend_api.infrastructure.exception.user;

import com.morzevichka.backend_api.infrastructure.exception.NotFoundException;

public class UserNotFoundInContextException extends NotFoundException {
    public UserNotFoundInContextException() {
        super("Пользователь не аутентифицирован");
    }
}
