package com.example.laundry.repository;

import com.example.laundry.entity.Dormitory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DormitoryRepository extends JpaRepository<Dormitory, Integer> {
    // 根據宿舍名稱查詢
    Optional<Dormitory> findByName(String name);
}
