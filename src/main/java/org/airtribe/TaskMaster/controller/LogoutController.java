package org.airtribe.TaskMaster.controller;

import org.airtribe.TaskMaster.service.LogoutService;
import org.airtribe.TaskMaster.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class LogoutController {

    @Autowired
    private LogoutService logoutService;

    @PostMapping("/auth/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
       String authHeader = request.getHeader("Authorization");

    if (authHeader != null) {
        String token = authHeader;

        try {
            long expiryMillis = JwtUtil.getExpiryFromToken(token).getTime();
            long ttlMillis = expiryMillis - System.currentTimeMillis();
            long ttlSeconds = Math.max(ttlMillis / 1000, 0); // avoid negative values

            if (ttlSeconds < 2) {
                return ResponseEntity.badRequest().body("Token is already expired.");
            }

            logoutService.blacklistToken(token, ttlSeconds * 1000);
            return ResponseEntity.ok("Logged out successfully");

        } catch (Exception e) {
            return ResponseEntity.status(400).body("Invalid token format or parsing failed.");
        }

    } else {
        return ResponseEntity.badRequest().body("Missing or invalid Authorization header.");
    }
}
}
