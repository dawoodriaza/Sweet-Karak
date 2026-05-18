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

    public void sendWelcomeEmail(String toEmail, String fullName) {
        String subject = "Welcome to Sweet Karak!";
        String body = "Hi " + fullName + ",\n\nWelcome to Sweet Karak! Your account has been created successfully.\n\nBest regards,\nSweet Karak Team";
        sendEmail(toEmail, subject, body);
        log.info("Welcome email sent to {}", toEmail);
    }

    public void sendCafeRequestSubmittedEmail(String cafeAdminEmail, String cafeName) {
        String adminSubject = "New Cafe Request: " + cafeName;
        String adminBody = "A new cafe registration request has been submitted.\n\nCafe Name: " + cafeName + "\nSubmitted by: " + cafeAdminEmail + "\n\nPlease log in to the admin panel to review.";
        sendEmail(systemAdminEmail, adminSubject, adminBody);

        String cafeAdminSubject = "Your Cafe Request Has Been Submitted";
        String cafeAdminBody = "Hi,\n\nYour request to register \"" + cafeName + "\" has been submitted.\nOur team will review it and notify you.\n\nBest regards,\nSweet Karak Team";
        sendEmail(cafeAdminEmail, cafeAdminSubject, cafeAdminBody);

        log.info("Cafe request submitted emails sent for: {}", cafeName);
    }

    public void sendCafeApprovedEmail(String cafeAdminEmail, String cafeName) {
        String subject = "Your Cafe Has Been Approved!";
        String body = "Congratulations!\n\nYour cafe \"" + cafeName + "\" has been approved.\nYou can now start adding items.\n\nBest regards,\nSweet Karak Team";
        sendEmail(cafeAdminEmail, subject, body);
        log.info("Cafe approved email sent to {}", cafeAdminEmail);
    }

    public void sendCafeRejectedEmail(String cafeAdminEmail, String cafeName) {
        String subject = "Cafe Registration Update";
        String body = "Hi,\n\nWe regret to inform you that your cafe \"" + cafeName + "\" has not been approved.\nPlease contact support for more information.\n\nBest regards,\nSweet Karak Team";
        sendEmail(cafeAdminEmail, subject, body);
        log.info("Cafe rejected email sent to {}", cafeAdminEmail);
    }

    public void sendOrderConfirmationEmail(String toEmail, String fullName, Long orderId, String itemName) {
        String subject = "Order Confirmation #" + orderId;
        String body = "Hi " + fullName + ",\n\nYour order has been placed successfully!\n\nOrder ID: " + orderId + "\nItem: " + itemName + "\n\nBest regards,\nSweet Karak Team";
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
