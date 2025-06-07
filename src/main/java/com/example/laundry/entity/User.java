package com.example.laundry.entity;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
    
    @Column(unique = true)  // 學號不能重複
    private String studentId;
    
    private String name;
    private String roomNumber;
    private String passwordHash;
    
    @Enumerated(EnumType.STRING)
    private UserRole role;
    
    @Column(nullable = false)
    private Boolean enabled = true;  // 帳號是否啟用
    
    // 建構子
    public User() {}
    
    public User(String studentId, String name, String roomNumber, 
                String passwordHash, UserRole role) {
        this.studentId = studentId;
        this.name = name;
        this.roomNumber = roomNumber;
        this.passwordHash = passwordHash;
        this.role = role;
        this.enabled = true;
    }
    
    // 完整的 Getter/Setter
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
    
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
    
    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }
    
    // Spring Security 需要的方法
    public String getUsername() { return studentId; }  // 用學號作為使用者名稱
    public String getPassword() { return passwordHash; }
    
    // 帳號狀態檢查方法
    public boolean isAccountNonExpired() { return true; }
    public boolean isAccountNonLocked() { return true; }
    public boolean isCredentialsNonExpired() { return true; }
    public boolean isEnabled() { return enabled; }
    
    // 列舉
    public enum UserRole {
        STUDENT, ADMIN
    }
}
