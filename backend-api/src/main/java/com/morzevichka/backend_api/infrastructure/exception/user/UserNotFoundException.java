package com.morzevichka.backend_api.infrastructure.exception.user;

import com.morzevichka.backend_api.infrastructure.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
