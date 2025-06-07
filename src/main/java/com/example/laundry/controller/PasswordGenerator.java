package com.example.laundry.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PasswordGenerator {
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @GetMapping("/generate-password")
    public String generatePassword() {
        String rawPassword = "password";
        String newHash = passwordEncoder.encode(rawPassword);
        
        return String.format(
            "<h2>🔐 密碼產生器</h2>" +
            "<p><strong>原始密碼:</strong> %s</p>" +
            "<p><strong>新的雜湊值:</strong> %s</p>" +
            "<hr>" +
            "<h3>📋 請在 H2 Console 執行：</h3>" +
            "<textarea rows='3' cols='80' readonly>" +
            "UPDATE users SET password_hash = '%s' WHERE student_id = 'ADMIN001';" +
            "</textarea>" +
            "<br><br>" +
            "<a href='/h2-console' target='_blank'>🔗 前往 H2 Console</a>",
            rawPassword, newHash, newHash
        );
    }
}
