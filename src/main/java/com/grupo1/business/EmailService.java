package com.grupo1.business;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendPasswordResetEmail(String email, String token) {
        String frontendBaseUrl = "http://localhost:8080";
        String resetUrl = frontendBaseUrl + "/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("nao-responde@gmail.com");
        message.setTo(email);
        message.setSubject("Redefinição de senha: ");
        message.setText("Redefinir sua senha, clique no link abaixo:\n\n"
                + resetUrl + "\n\n"
                + "Se você não solicitou isso, por favor ignore este e-mail.");
        mailSender.send(message);
    }
}
