package com.example.laundry;

import com.example.laundry.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
        System.out.println("=== SecurityConfig 建構函數 ===");
        System.out.println("注入的 UserDetailsService: " + userDetailsService.getClass().getSimpleName());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        System.out.println("=== 建立 BCryptPasswordEncoder ===");
        
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // 測試密碼雜湊
        String testHash = "$2a$10$dXJ3SW6G7P7E.LB4xB1/d.rPrpvQKLOhwWKfOLDZP7gAtYLHhTMiK";
        boolean matches = encoder.matches("password", testHash);
        System.out.println("密碼測試結果: " + matches);
        
        if (matches) {
            System.out.println("✅ 密碼雜湊正確！可以直接使用");
        } else {
            System.out.println("❌ 密碼雜湊錯誤，產生新的雜湊");
            String newHash = encoder.encode("password");
            System.out.println("新的密碼雜湊: " + newHash);
            System.out.println("建議的 SQL: UPDATE users SET password_hash = '" + newHash + "' WHERE student_id = 'ADMIN001';");
        }
        
        return encoder;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        System.out.println("=== 建立 DaoAuthenticationProvider ===");
        
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        
        System.out.println("DaoAuthenticationProvider 設定完成");
        return provider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        System.out.println("=== 配置 SecurityFilterChain ===");
        
        http
            .authenticationProvider(authenticationProvider())
            .authorizeRequests(auth -> auth
                .antMatchers("/login", "/register", "/h2-console/**", "/css/**", "/js/**", 
                           "/generate-password", "/admin/machines/status").permitAll()  // 🆕 開放機台狀態 API
                .antMatchers("/admin/**").hasRole("ADMIN")  // 其他管理員功能仍需要權限
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .usernameParameter("studentId")
                .passwordParameter("password")
                .defaultSuccessUrl("/admin", true)
                .failureUrl("/login?error=true")
                .successHandler((request, response, authentication) -> {
                    System.out.println("=== 🎉 登入成功 ===");
                    System.out.println("認證用戶: " + authentication.getName());
                    System.out.println("認證權限: " + authentication.getAuthorities());
                    response.sendRedirect("/admin");
                })
                .failureHandler((request, response, exception) -> {
                    System.out.println("=== ❌ 登入失敗 ===");
                    System.out.println("異常類型: " + exception.getClass().getSimpleName());
                    System.out.println("異常訊息: " + exception.getMessage());
                    response.sendRedirect("/login?error=true");
                })
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            )
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions().disable());

        System.out.println("SecurityFilterChain 配置完成");
        return http.build();
    }
}
