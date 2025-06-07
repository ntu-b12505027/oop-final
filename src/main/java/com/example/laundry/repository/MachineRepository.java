package com.example.laundry.repository;

import com.example.laundry.entity.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MachineRepository extends JpaRepository<Machine, Integer> {
    List<Machine> findByFloor(Integer floor);
    List<Machine> findByFloorAndStatus(Integer floor, Machine.MachineStatus status);
    List<Machine> findByType(Machine.MachineType type);
    List<Machine> findByDormitoryId(Integer dormitoryId);
    
    // 🆕 只添加這一行來解決編譯錯誤
    List<Machine> findByStatus(Machine.MachineStatus status);
}
