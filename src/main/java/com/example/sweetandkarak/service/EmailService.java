package com.example.sweetandkarak.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.admin.email}")
    private String systemAdminEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(String toEmail, String fullName, String token) {
        String subject = "Verify Your Email - Sweet Karak";
        String body = "Hi " + fullName + ",\n\n"
                + "Thank you for registering at Sweet Karak.\n\n"
                + "Please verify your email by using this token:\n\n"
                + token + "\n\n"
                + "Send a POST request to:\n"
                + "POST /api/auth/verify-email?token=" + token + "\n\n"
                + "This token is valid for 24 hours.\n\n"
                + "Best regards,\nSweet Karak Team";
        sendEmail(toEmail, subject, body);
        log.info("Verification email sent to {}", toEmail);
    }

    public void sendWelcomeEmail(String toEmail, String fullName) {
        String subject = "Welcome to Sweet Karak!";
        String body = "Hi " + fullName + ",\n\n"
                + "Your email has been verified successfully.\n"
                + "Welcome to Sweet Karak! You can now log in and start ordering.\n\n"
                + "Best regards,\nSweet Karak Team";
        sendEmail(toEmail, subject, body);
        log.info("Welcome email sent to {}", toEmail);
    }

    public void sendResetPasswordEmail(String toEmail, String fullName, String token) {
        String subject = "Reset Your Password - Sweet Karak";
        String body = "Hi " + fullName + ",\n\n"
                + "You requested to reset your password.\n\n"
                + "Use this token to reset your password:\n\n"
                + token + "\n\n"
                + "Send a POST request to:\n"
                + "POST /api/auth/reset-password\n"
                + "Body: { \"token\": \"" + token + "\", \"newPassword\": \"your_new_password\" }\n\n"
                + "This token is valid for 1 hour.\n"
                + "If you did not request this, ignore this email.\n\n"
                + "Best regards,\nSweet Karak Team";
        sendEmail(toEmail, subject, body);
        log.info("Reset password email sent to {}", toEmail);
    }

    public void sendCafeRequestSubmittedEmail(String cafeAdminEmail, String cafeName) {
        String adminSubject = "New Cafe Request: " + cafeName;
        String adminBody = "A new cafe registration request has been submitted.\n\n"
                + "Cafe Name: " + cafeName + "\n"
                + "Submitted by: " + cafeAdminEmail + "\n\n"
                + "Please log in to the admin panel to review.";
        sendEmail(systemAdminEmail, adminSubject, adminBody);

        String cafeAdminSubject = "Your Cafe Request Has Been Submitted";
        String cafeAdminBody = "Hi,\n\n"
                + "Your request to register \"" + cafeName + "\" has been submitted.\n"
                + "Our team will review it and notify you by email.\n\n"
                + "Best regards,\nSweet Karak Team";
        sendEmail(cafeAdminEmail, cafeAdminSubject, cafeAdminBody);
        log.info("Cafe request submitted emails sent for: {}", cafeName);
    }

    public void sendCafeApprovedEmail(String cafeAdminEmail, String cafeName) {
        String subject = "Your Cafe Has Been Approved!";
        String body = "Congratulations!\n\n"
                + "Your cafe \"" + cafeName + "\" has been approved.\n"
                + "You can now start adding items to your menu.\n\n"
                + "Best regards,\nSweet Karak Team";
        sendEmail(cafeAdminEmail, subject, body);
        log.info("Cafe approved email sent to {}", cafeAdminEmail);
    }

    public void sendCafeRejectedEmail(String cafeAdminEmail, String cafeName) {
        String subject = "Cafe Registration Update";
        String body = "Hi,\n\n"
                + "We regret to inform you that your cafe \"" + cafeName + "\" has not been approved.\n"
                + "Please contact support for more information.\n\n"
                + "Best regards,\nSweet Karak Team";
        sendEmail(cafeAdminEmail, subject, body);
        log.info("Cafe rejected email sent to {}", cafeAdminEmail);
    }

    public void sendOrderConfirmationEmail(String toEmail, String fullName, Long orderId, String itemName) {
        String subject = "Order Confirmation #" + orderId;
        String body = "Hi " + fullName + ",\n\n"
                + "Your order has been placed successfully!\n\n"
                + "Order ID: " + orderId + "\n"
                + "Item: " + itemName + "\n\n"
                + "Best regards,\nSweet Karak Team";
        sendEmail(toEmail, subject, body);
        log.info("Order confirmation email sent to {} for order #{}", toEmail, orderId);
    }

    private void sendEmail(String toEmail, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", toEmail, e.getMessage());
        }
    }
}