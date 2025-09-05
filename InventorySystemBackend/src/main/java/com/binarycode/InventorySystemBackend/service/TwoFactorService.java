package com.binarycode.InventorySystemBackend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class TwoFactorService {

    private final EmailService emailService;
    private final Map<String, String> twoFactorCodes = new HashMap<>();
    private final Map<String, Long> codeExpiration = new HashMap<>();


    public String generateAndSend2FACode(String email) {
        try {
            log.info("=== INICIANDO GENERACIÓN 2FA ===");
            log.info("Email destino: {}", email);
            
            String code = generateRandomCode(6);
            log.info("Código generado: {}", code);
            
            
            twoFactorCodes.put(email, code);
            codeExpiration.put(email, System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10));
            log.info("Código almacenado para: {}", email);
            
         
            emailService.send2FACode(email, code);
            log.info("✅ Solicitud de envío de email completada");
            
            System.out.println("\n========================================");
            System.out.println("🔐 CÓDIGO 2FA PARA LOGIN:");
            System.out.println("Email: " + email);
            System.out.println("Código: " + code);
            System.out.println("========================================\n");
            
            return code;
            
        } catch (Exception e) {
            log.error("❌ ERROR en generateAndSend2FACode: {}", e.getMessage());
            
            String code = generateRandomCode(6);
            twoFactorCodes.put(email, code);
            codeExpiration.put(email, System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10));
            
            System.out.println("\n========================================");
            System.out.println("⚠️  Email falló, pero código generado:");
            System.out.println("Email: " + email);
            System.out.println("Código: " + code);
            System.out.println("========================================\n");
            
            return code;
        }
    }

    public boolean verify2FACode(String email, String code) {
        try {
            log.info("=== INICIANDO VERIFICACIÓN 2FA ===");
            log.info("Email: {}", email);
            log.info("Código recibido: {}", code);
            
            String storedCode = twoFactorCodes.get(email);
            Long expirationTime = codeExpiration.get(email);
            
            log.info("Código almacenado: {}", storedCode);
            log.info("Tiempo expiración: {}", expirationTime);
            log.info("Tiempo actual: {}", System.currentTimeMillis());
            
            if (storedCode == null) {
                log.warn("❌ No hay código almacenado para este email");
                return false;
            }
            
            if (expirationTime == null) {
                log.warn("❌ No hay tiempo de expiración para este código");
                return false;
            }
            
     
            if (System.currentTimeMillis() > expirationTime) {
                log.warn("❌ Código expirado");
                twoFactorCodes.remove(email);
                codeExpiration.remove(email);
                return false;
            }
            
       
            if (storedCode.equals(code)) {
                log.info("✅ Código verificado exitosamente");
                twoFactorCodes.remove(email);
                codeExpiration.remove(email);
                return true;
            }
            
            log.warn("❌ Código incorrecto");
            return false;
            
        } catch (Exception e) {
            log.error("❌ ERROR en verify2FACode: {}", e.getMessage(), e);
            return false;
        }
    }

    private String generateRandomCode(int length) {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        
        for (int i = 0; i < length; i++) {
            code.append(random.nextInt(10));
        }
        
        return code.toString();
    }


    public boolean hasPending2FA(String email) {
        boolean hasPending = twoFactorCodes.containsKey(email);
        log.info("Verificando 2FA pendiente para {}: {}", email, hasPending);
        return hasPending;
    }


    public String getCurrentCode(String email) {
        String code = twoFactorCodes.get(email);
        log.info("Código actual para {}: {}", email, code);
        return code;
    }


    public void cleanupExpiredCodes() {
        log.info("🔍 Limpiando códigos 2FA expirados");
        long currentTime = System.currentTimeMillis();
        int removedCount = 0;
        
        for (Map.Entry<String, Long> entry : codeExpiration.entrySet()) {
            if (currentTime > entry.getValue()) {
                twoFactorCodes.remove(entry.getKey());
                codeExpiration.remove(entry.getKey());
                removedCount++;
            }
        }
        
        log.info("🧹 Códigos expirados removidos: {}", removedCount);
    }


    public String generateTestCode(String email) {
        String code = generateRandomCode(6);
        twoFactorCodes.put(email, code);
        codeExpiration.put(email, System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10));
        
        System.out.println("\n========================================");
        System.out.println("🧪 CÓDIGO 2FA DE PRUEBA:");
        System.out.println("Email: " + email);
        System.out.println("Código: " + code);
        System.out.println("========================================\n");
        
        return code;
    }
}