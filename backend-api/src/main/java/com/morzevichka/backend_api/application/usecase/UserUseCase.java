package com.morzevichka.backend_api.application.usecase;

import com.morzevichka.backend_api.api.dto.user.UserResponse;
import com.morzevichka.backend_api.application.mapper.UserMapper;
import com.morzevichka.backend_api.application.service.UserApplicationService;
import com.morzevichka.backend_api.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserUseCase {

    private final UserApplicationService userApplicationService;
    private final UserMapper userMapper;

    public UserResponse getCurrentUser() {
        User user = userApplicationService.getCurrentUser();

        return userMapper.toResponse(user);
    }
}
