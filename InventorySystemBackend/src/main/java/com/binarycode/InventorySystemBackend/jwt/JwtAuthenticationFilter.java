package com.binarycode.InventorySystemBackend.jwt;

import com.binarycode.InventorySystemBackend.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
        throws ServletException, IOException {

                String path = request.getRequestURI();
    System.out.println("Request URI: " + path);

    if (
        path.startsWith("/auth") ||
        path.startsWith("/test") 
    ) {
        filterChain.doFilter(request, response);
        return;
    }

        final String authHeader = request.getHeader("Authorization");
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        try {
            final String jwt = authHeader.substring(7);
            final String username = jwtService.extractUsername(jwt);
            
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                System.out.println("=== JWT FILTER REQUEST DEBUG ===");
                System.out.println("Request URI: " + request.getRequestURI());
                System.out.println("Request Method: " + request.getMethod());
                System.out.println("Authorization Header: " + request.getHeader("Authorization"));
                System.out.println("=== END REQUEST DEBUG ===");

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    var authorities = userDetails.getAuthorities();

                    UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    System.out.println("Authentication set with: " +
                        SecurityContextHolder.getContext().getAuthentication().getAuthorities());
                }

                System.out.println("=== END DEBUG ===");
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }
        
        filterChain.doFilter(request, response);
    }
}