package com.grupo1.business;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendPasswordResetEmail(String toEmail, String token) {
        String frontendBaseUrl = "http://localhost:3000";
        String resetUrl = frontendBaseUrl + "/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("nao-responda@suaapp.com");
        message.setTo(toEmail);
        message.setSubject("Redefinição de Senha");
        message.setText("Para redefinir sua senha, clique no link abaixo:\n\n" 
                       + resetUrl + "\n\n"
                       + "Se você não solicitou isso, por favor ignore este e-mail.");
        
        mailSender.send(message);
    }
}