package com.example.sweetandkarak.controller;

import com.example.sweetandkarak.dto.request.ForgotPasswordRequest;
import com.example.sweetandkarak.dto.request.LoginRequest;
import com.example.sweetandkarak.dto.request.ResetPasswordRequest;
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
    public ResponseEntity<ApiResponse<String>> signup(@Valid @RequestBody UserSignupRequest request) {
        String message = authService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(message, null));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<ApiResponse<String>> verifyEmail(@RequestParam String token) {
        String message = authService.verifyEmail(token);
        return ResponseEntity.ok(ApiResponse.success(message, null));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        String message = authService.forgotPassword(request);
        return ResponseEntity.ok(ApiResponse.success(message, null));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        String message = authService.resetPassword(request);
        return ResponseEntity.ok(ApiResponse.success(message, null));
    }
}