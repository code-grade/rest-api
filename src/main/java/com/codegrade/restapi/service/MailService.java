package com.codegrade.restapi.service;

import com.codegrade.restapi.exception.ApiException;
import com.codegrade.restapi.utils.JwtUtils;
import com.codegrade.restapi.utils.RBuilder;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
@Getter
@Setter
public class MailService {

    private final JavaMailSender javaMailSender;
    private final JwtUtils jwtUtils;
    private final SpringTemplateEngine templateEngine;

    public void sendSimpleMessage(String to, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("code.grade.dev@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }

    public void sendTemplateMessage(String to) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
        Context context = new Context();
        context.setVariable("username", "thilinatlm");
        context.setVariable("token", "thisistoken");
        context.setVariable("link", "https://code-grade.netlify.app/verify/asdasdasd");
        String html = templateEngine.process("account-verification", context);
        helper.setTo(to);
        helper.setText(html, true);
        helper.setSubject("Welcome");
        helper.setFrom("code.grade.dev@gmail.com");
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

    @Async
    public void verifyAccount(String username, String email) {
        String token = jwtUtils.signEmailJwt(username, email);
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()
            );

            Context context = new Context();
            context.setVariable("username", username);
            context.setVariable("token", token);
            context.setVariable("link", "https://code-grade.netlify.app/verify/" + token);

            String html = templateEngine.process("account-verification", context);
            helper.setTo(email);
            helper.setText(html, true);
            helper.setSubject("Welcome to Code Grade");
            helper.setFrom("code.grade.dev@gmail.com");
            javaMailSender.send(message);

        } catch (MessagingException ex) {

            throw new ApiException(RBuilder.error("email service error"));
        }
    }

}
