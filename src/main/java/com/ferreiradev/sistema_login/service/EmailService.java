package com.ferreiradev.sistema_login.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;

    @Async
    public void sendPasswordResetEmail(String toEmail, String resetToken) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Recuperação de senha");
            message.setText("Use o código para redefinir sua senha: " + resetToken
                    + "\nEsse código expira em 15 minutos.");
            mailSender.send(message);
            log.info("E-mail de redefinição de senha enviado");
        } catch (MailException e) {
            log.error("Falha ao enviar e-mail de redefinição de senha", e);
        }
    }
}
