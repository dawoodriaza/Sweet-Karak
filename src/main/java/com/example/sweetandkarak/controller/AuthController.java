package com.example.sweetandkarak.controller;

import com.example.sweetandkarak.dto.request.LoginRequest;
import com.example.sweetandkarak.dto.request.UserSignupRequest;
import com.example.sweetandkarak.dto.response.ApiResponse;
import com.example.sweetandkarak.dto.response.AuthResponse;
import com.example.sweetandkarak.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<AuthResponse>> signup(@Valid @RequestBody UserSignupRequest request) {
        AuthResponse response = authService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("User registered successfully", response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }
}
