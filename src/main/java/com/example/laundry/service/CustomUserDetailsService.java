package com.example.laundry.service;

import com.example.laundry.entity.User;
import com.example.laundry.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;  // ← 🆕 加入這行
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String studentId) throws UsernameNotFoundException {
        System.out.println("=== 登入偵錯 ===");
        System.out.println("查詢學號：" + studentId);
        
        User user = userRepository.findByStudentId(studentId)
            .orElseThrow(() -> new UsernameNotFoundException("找不到學號：" + studentId));
        
        System.out.println("資料庫密碼雜湊：" + user.getPasswordHash());
        System.out.println("使用者啟用狀態：" + user.isEnabled());
        System.out.println("帳號狀態 - 未過期：" + user.isAccountNonExpired());
        System.out.println("帳號狀態 - 未鎖定：" + user.isAccountNonLocked());
        
        // 🆕 直接在這裡測試密碼比對
        BCryptPasswordEncoder testEncoder = new BCryptPasswordEncoder();
        boolean passwordTest = testEncoder.matches("password", user.getPasswordHash());
        System.out.println("🔐 密碼比對測試結果：" + passwordTest);
        
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
        
        System.out.println("權限：" + authorities);
        
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
            .username(user.getStudentId())
            .password(user.getPasswordHash())  // 🔍 確認這裡返回的是什麼
            .authorities(authorities)
            .accountExpired(false)
            .accountLocked(false)
            .credentialsExpired(false)
            .disabled(!user.isEnabled())
            .build();
            
        System.out.println("UserDetails 建立成功");
        System.out.println("UserDetails 密碼：" + userDetails.getPassword());  // 🆕 確認 UserDetails 中的密碼
        
        return userDetails;
    }

    }


