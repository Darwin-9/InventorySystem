package com.binarycode.InventorySystemBackend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j 
public class EmailService {

    private final JavaMailSender mailSender;

    public void send2FACode(String toEmail, String code) {
        try {
            log.info("📧 Intentando enviar código 2FA a: {}", toEmail);
            
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Código de verificación - Sistema de Inventario");
            message.setText("Su código de verificación es: " + code + "\n\n" +
                           "Este código expira en 10 minutos.");
            
            mailSender.send(message);
            log.info("✅ Email 2FA enviado exitosamente a: {}", toEmail);
            log.info("🔐 Código enviado: {}", code);
            
        } catch (Exception e) {
            log.error("❌ Error enviando email 2FA a {}: {}", toEmail, e.getMessage());
            log.error("🔐 Código que se intentó enviar: {}", code);
            
        }
    }

    public void sendPasswordResetEmail(String toEmail, String token) {
        try {
            log.info("📧 Intentando enviar email de reset a: {}", toEmail);
            
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Recuperación de contraseña");
            message.setText("Para resetear su contraseña, use el siguiente token: " + token);
            
            mailSender.send(message);
            log.info("✅ Email de reset enviado exitosamente a: {}", toEmail);
            
        } catch (Exception e) {
            log.error("❌ Error enviando email de reset a {}: {}", toEmail, e.getMessage());
        }
    }
}