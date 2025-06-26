package com.cineflex.api.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender javaMailSender;

    public EmailService (
        JavaMailSender javaMailSender
    ) {
        this.javaMailSender = javaMailSender;
    }

    public void sendActivationEmail(String token, String email, String host) {
        sendEmail(host + "/api/authentication/verify/" + token, email, "Activation email");
    }

    public void sendEmail(String content, String email, String subject) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Activation email");

        message.setText(content);

        javaMailSender.send(message);
    }
}
