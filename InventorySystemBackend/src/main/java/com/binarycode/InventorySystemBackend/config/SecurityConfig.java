package com.binarycode.InventorySystemBackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/auth/login", 
                    "/auth/register", 
                    "/auth/verify-2fa", 
                    "/auth/resend-2fa"
                ).permitAll()
                
                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                
                .requestMatchers("/warehouse/**").hasAnyAuthority("ADMIN", "WAREHOUSE_STAFF")
            
                .requestMatchers("/sales/**").hasAnyAuthority("ADMIN", "SALES")
                
                .requestMatchers("/reports/**").hasAnyAuthority("ADMIN", "WAREHOUSE_STAFF")
                
                .requestMatchers("/dashboard/**").authenticated()
            
                .anyRequest().authenticated()
            );
        
        return http.build();
    }
}