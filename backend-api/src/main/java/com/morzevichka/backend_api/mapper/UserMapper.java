package com.morzevichka.backend_api.mapper;

import com.morzevichka.backend_api.dto.user.UserInfoResponse;
import com.morzevichka.backend_api.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    public UserInfoResponse toDto(User user) {
        return new UserInfoResponse(user.getId(), user.getLogin(), user.getEmail(), user.getUserRole());
    }

    public List<UserInfoResponse> toDto(List<User> users) {
        return users
                .stream()
                .map(this::toDto)
                .toList();
    }
}
