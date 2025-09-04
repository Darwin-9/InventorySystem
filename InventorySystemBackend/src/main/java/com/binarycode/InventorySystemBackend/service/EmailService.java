package com.binarycode.InventorySystemBackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void send2FACode(String toEmail, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Código de verificación - Sistema de Inventario");
        message.setText("Su código de verificación es: " + code + "\n\n" +
                       "Este código expira en 10 minutos.");
        
        mailSender.send(message);
    }

    public void sendPasswordResetEmail(String toEmail, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Recuperación de contraseña");
        message.setText("Para resetear su contraseña, use el siguiente token: " + token);
        
        mailSender.send(message);
    }
}