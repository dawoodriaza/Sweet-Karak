package com.example.sweetandkarak.service;

import com.example.sweetandkarak.dto.request.UserUpdateRequest;
import com.example.sweetandkarak.dto.response.UserResponse;
import com.example.sweetandkarak.exception.ResourceNotFoundException;
import com.example.sweetandkarak.mapper.UserMapper;
import com.example.sweetandkarak.model.User;
import com.example.sweetandkarak.repository.UserRepository;
import com.example.sweetandkarak.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final FileUploadUtil fileUploadUtil;

    public UserResponse getUserById(Long id) {
        return userMapper.toResponse(findById(id));
    }

    public UserResponse getUserByEmail(String email) {
        return userMapper.toResponse(findByEmail(email));
    }

    public Page<UserResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toResponse);
    }

    public Page<UserResponse> searchUsersByName(String name, Pageable pageable) {
        return userRepository.findByFullNameContainingIgnoreCase(name, pageable).map(userMapper::toResponse);
    }

    @Transactional
    public UserResponse updateUser(String email, UserUpdateRequest request) {
        User user = findByEmail(email);
        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }
        if (request.getPhoneNumber() != null) {
            user.setPhoneNumber(request.getPhoneNumber());
        }
        User updated = userRepository.save(user);
        log.info("User updated: {}", email);
        return userMapper.toResponse(updated);
    }

    @Transactional
    public UserResponse uploadProfilePic(String email, MultipartFile file) {
        User user = findByEmail(email);
        try {
            if (user.getProfilePic() != null) {
                fileUploadUtil.deleteFile(user.getProfilePic());
            }
            String filePath = fileUploadUtil.saveFile(file, "profile");
            user.setProfilePic(filePath);
            User updated = userRepository.save(user);
            log.info("Profile pic updated for: {}", email);
            return userMapper.toResponse(updated);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (IOException e) {
            log.error("Failed to upload profile pic: {}", e.getMessage());
            throw new RuntimeException("Failed to save profile picture to disk");
        }
    }

    @Transactional
    public void activateUser(Long id) {
        User user = findById(id);
        user.setIsActive(1);
        userRepository.save(user);
        log.info("User activated: {}", id);
    }

    @Transactional
    public void deactivateUser(Long id) {
        User user = findById(id);
        user.setIsActive(0);
        userRepository.save(user);
        log.info("User deactivated: {}", id);
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.delete(findById(id));
        log.info("User deleted: {}", id);
    }

    private User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
    }

    private User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
