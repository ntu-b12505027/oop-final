package com.example.laundry.service;

import com.example.laundry.entity.User;
import com.example.laundry.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;  // 密碼加密器
    
    // 原有方法...
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public Optional<User> getUserByStudentId(String studentId) {
        return userRepository.findByStudentId(studentId);
    }
    
    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }
    
    public List<User> getUsersByRoomNumber(String roomNumber) {
        return userRepository.findByRoomNumber(roomNumber);
    }
    
    public boolean isStudentIdExists(String studentId) {
        return userRepository.findByStudentId(studentId).isPresent();
    }
    
    // 新增登入相關方法
    
    // 註冊新用戶
    public User registerUser(String studentId, String name, String roomNumber, 
                           String password, User.UserRole role) {
        // 檢查學號是否已存在
        if (isStudentIdExists(studentId)) {
            throw new RuntimeException("學號已存在：" + studentId);
        }
        
        // 加密密碼
        String encodedPassword = passwordEncoder.encode(password);
        
        // 建立新用戶
        User user = new User(studentId, name, roomNumber, encodedPassword, role);
        return userRepository.save(user);
    }
    
    // 驗證登入
    public boolean validateLogin(String studentId, String password) {
        Optional<User> userOpt = getUserByStudentId(studentId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // 使用密碼編碼器驗證密碼
            return passwordEncoder.matches(password, user.getPasswordHash()) && user.isEnabled();
        }
        return false;
    }
    
    // 一般儲存用戶（密碼已加密）
    public User saveUser(User user) {
        return userRepository.save(user);
    }
    
    // 更新密碼
    public void updatePassword(String studentId, String newPassword) {
        Optional<User> userOpt = getUserByStudentId(studentId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setPasswordHash(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        }
    }
}
