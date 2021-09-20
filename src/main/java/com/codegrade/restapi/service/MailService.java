package com.codegrade.restapi.service;

import com.codegrade.restapi.utils.JwtUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Getter @Setter
public class MailService {

    private final JavaMailSender javaMailSender;
    private final JwtUtils jwtUtils;

    public void sendSimpleMessage(String to, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("code.grade.dev@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }

    @Async
    public void verificationEmail(String username, String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("code.grade.dev@gmail.com");
        message.setTo(email);
        message.setSubject("Code Grade Verify Account");
        message.setText(
                "Use following link to verify your account, \n" +
                        jwtUtils.signEmailJwt(username, email)
        );
        javaMailSender.send(message);
    }

}
