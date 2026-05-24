package com.example.sweetandkarak.service;

import com.example.sweetandkarak.config.JwtUtil;
import com.example.sweetandkarak.dto.request.ForgotPasswordRequest;
import com.example.sweetandkarak.dto.request.LoginRequest;
import com.example.sweetandkarak.dto.request.ResetPasswordRequest;
import com.example.sweetandkarak.dto.request.UserSignupRequest;
import com.example.sweetandkarak.dto.response.AuthResponse;
import com.example.sweetandkarak.enums.RoleEnum;
import com.example.sweetandkarak.exception.DuplicateResourceException;
import com.example.sweetandkarak.exception.ResourceNotFoundException;
import com.example.sweetandkarak.model.User;
import com.example.sweetandkarak.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

    @Transactional
    public String signup(UserSignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already registered: " + request.getEmail());
        }

        String verificationToken = UUID.randomUUID().toString();

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .role(RoleEnum.CUSTOMER)
                .emailVerified(false)
                .verificationToken(verificationToken)
                .build();

        userRepository.save(user);
        log.info("User registered, pending verification: {}", user.getEmail());

        emailService.sendVerificationEmail(user.getEmail(), user.getFullName(), verificationToken);

        return "Registration successful. Please check your email to verify your account before logging in.";
    }

    @Transactional
    public String verifyEmail(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid or expired verification token."));

        user.setEmailVerified(true);
        user.setVerificationToken(null);
        userRepository.save(user);

        emailService.sendWelcomeEmail(user.getEmail(), user.getFullName());
        log.info("Email verified for user: {}", user.getEmail());

        return "Email verified successfully. You can now log in.";
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("No account found with this email."));

        if (!user.getEmailVerified()) {
            throw new RuntimeException("Please verify your email before logging in. Check your inbox.");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtUtil.generateToken(userDetails, user.getRole().name());

        log.info("User logged in: {}", user.getEmail());

        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole().name())
                .build();
    }

    @Transactional
    public String forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("No account found with this email."));

        String resetToken = UUID.randomUUID().toString();
        user.setResetPasswordToken(resetToken);
        userRepository.save(user);

        emailService.sendResetPasswordEmail(user.getEmail(), user.getFullName(), resetToken);
        log.info("Reset password email sent to: {}", user.getEmail());

        return "Password reset email sent. Please check your inbox.";
    }

    @Transactional
    public String resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByResetPasswordToken(request.getToken())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid or expired reset token."));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setResetPasswordToken(null);
        userRepository.save(user);

        log.info("Password reset successfully for: {}", user.getEmail());

        return "Password reset successfully. You can now log in with your new password.";
    }
}