package com.example.laundry.controller;

import com.example.laundry.entity.User;
import com.example.laundry.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    // 登入頁面
    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                       @RequestParam(value = "logout", required = false) String logout,
                       Model model) {
        
        if (error != null) {
            model.addAttribute("error", "學號或密碼錯誤！");
        }
        
        if (logout != null) {
            model.addAttribute("success", "已成功登出！");
        }
        
        return "login";
    }
    
    // 註冊頁面
    @GetMapping("/register")
    public String register() {
        return "register";
    }
    
    // 處理註冊
    @PostMapping("/register")
    public String processRegister(@RequestParam String studentId,
                                @RequestParam String name,
                                @RequestParam String roomNumber,
                                @RequestParam String password,
                                @RequestParam String confirmPassword,
                                @RequestParam(defaultValue = "STUDENT") String role,
                                RedirectAttributes redirectAttributes) {
        
        try {
            // 驗證輸入
            if (studentId == null || studentId.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "請輸入學號！");
                return "redirect:/register";
            }
            
            if (password == null || password.length() < 6) {
                redirectAttributes.addFlashAttribute("error", "密碼至少需要6個字元！");
                return "redirect:/register";
            }
            
            if (!password.equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("error", "密碼確認不符！");
                return "redirect:/register";
            }
            
            // 檢查學號是否已存在
            if (userService.isStudentIdExists(studentId)) {
                redirectAttributes.addFlashAttribute("error", "學號已存在：" + studentId);
                return "redirect:/register";
            }
            
            // 建立新用戶
            User.UserRole userRole = "ADMIN".equals(role) ? User.UserRole.ADMIN : User.UserRole.STUDENT;
            userService.registerUser(studentId, name, roomNumber, password, userRole);
            
            redirectAttributes.addFlashAttribute("success", 
                "註冊成功！請使用學號：" + studentId + " 登入");
            return "redirect:/login";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "註冊失敗：" + e.getMessage());
            return "redirect:/register";
        }
    }
    
    // 獲取當前登入使用者
    @ModelAttribute("currentUser")
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            Optional<User> userOpt = userService.getUserByStudentId(auth.getName());
            return userOpt.orElse(null);
        }
        return null;
    }
    
    // 個人資料頁面
    @GetMapping("/profile")
    public String profile(Model model) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", currentUser);
        return "profile";
    }
    
    // 更新個人資料
    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam String name,
                              @RequestParam String roomNumber,
                              RedirectAttributes redirectAttributes) {
        try {
            User currentUser = getCurrentUser();
            if (currentUser != null) {
                currentUser.setName(name);
                currentUser.setRoomNumber(roomNumber);
                userService.saveUser(currentUser);
                redirectAttributes.addFlashAttribute("success", "個人資料更新成功！");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "更新失敗：" + e.getMessage());
        }
        return "redirect:/profile";
    }
}
