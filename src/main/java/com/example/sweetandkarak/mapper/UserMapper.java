package com.example.sweetandkarak.mapper;

import com.example.sweetandkarak.dto.response.UserResponse;
import com.example.sweetandkarak.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .profilePic(user.getProfilePic())
                .role(user.getRole())
                .isActive(user.getIsActive())
                .createdOn(user.getCreatedOn())
                .build();
    }
}
