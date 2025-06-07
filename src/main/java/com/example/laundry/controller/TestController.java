package com.example.laundry.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @GetMapping("/test-password")
    public String testPassword() {
        String rawPassword = "password";
        String currentHash = "$2a$10$eB5hF1QkQzK8o2wE2q9tJeYvA8G0lX6nRZQ5z6Y5K8p6F5gJ6vYyW";
        
        // 測試當前雜湊
        boolean currentMatches = passwordEncoder.matches(rawPassword, currentHash);
        
        // 產生新的雜湊
        String newHash = passwordEncoder.encode(rawPassword);
        boolean newMatches = passwordEncoder.matches(rawPassword, newHash);
        
        return String.format(
            "<h2>密碼測試結果</h2>" +
            "<p><strong>原始密碼:</strong> %s</p>" +
            "<p><strong>當前資料庫雜湊:</strong> %s</p>" +
            "<p><strong>當前雜湊比對結果:</strong> <span style='color: %s'>%s</span></p>" +
            "<p><strong>新產生的雜湊:</strong> %s</p>" +
            "<p><strong>新雜湊比對結果:</strong> <span style='color: %s'>%s</span></p>" +
            "<p><strong>PasswordEncoder 類型:</strong> %s</p>" +
            "<hr>" +
            "<p><strong>如果當前雜湊比對失敗，請在 H2 Console 執行：</strong></p>" +
            "<code>UPDATE users SET password_hash = '%s' WHERE student_id = 'ADMIN001';</code>",
            rawPassword,
            currentHash,
            currentMatches ? "green" : "red", currentMatches,
            newHash,
            newMatches ? "green" : "red", newMatches,
            passwordEncoder.getClass().getSimpleName(),
            newHash
        );
    }
}
