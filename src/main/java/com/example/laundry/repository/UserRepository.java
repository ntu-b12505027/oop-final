package com.example.laundry.repository;

import com.example.laundry.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;  // 加入這個 import！
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // 根據學號查詢使用者
    Optional<User> findByStudentId(String studentId);
    
    // 根據房號查詢使用者
    List<User> findByRoomNumber(String roomNumber);
    
    // 根據角色查詢使用者
    List<User> findByRole(User.UserRole role);
}
