package com.binarycode.InventorySystemBackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TwoFactorService {

    private final EmailService emailService;
    private final Map<String, String> twoFactorCodes = new HashMap<>();
    private final Map<String, Long> codeExpiration = new HashMap<>();

   
    public String generateAndSend2FACode(String email) {
       
        String code = generateRandomCode(6);
        
       
        twoFactorCodes.put(email, code);
        codeExpiration.put(email, System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10));
        
    
        emailService.send2FACode(email, code);
        
        return code;
    }

  
    public boolean verify2FACode(String email, String code) {
        String storedCode = twoFactorCodes.get(email);
        Long expirationTime = codeExpiration.get(email);
        
        if (storedCode == null || expirationTime == null) {
            return false;
        }
        
      
        if (System.currentTimeMillis() > expirationTime) {
            twoFactorCodes.remove(email);
            codeExpiration.remove(email);
            return false;
        }
        
  
        if (storedCode.equals(code)) {
           
            twoFactorCodes.remove(email);
            codeExpiration.remove(email);
            return true;
        }
        
        return false;
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
        return twoFactorCodes.containsKey(email);
    }
}