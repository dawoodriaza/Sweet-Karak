package com.example.sweetandkarak.dto.response;

import com.example.sweetandkarak.enums.RoleEnum;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String profilePic;
    private RoleEnum role;
    private Integer isActive;
    private LocalDateTime createdOn;
}
