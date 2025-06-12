package com.cineflex.api.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

@Service
public class EmailService {
    private final JavaMailSender javaMailSender;

    public EmailService (
        JavaMailSender javaMailSender
    ) {
        this.javaMailSender = javaMailSender;
    }

    public void sendActivationEmail(String token, String email, String host) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Activation email");

        message.setText(host + "/api/authentication/verify/" + token);

        javaMailSender.send(message);
    }
}
