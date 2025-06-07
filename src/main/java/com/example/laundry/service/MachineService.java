package com.example.laundry.service;

import com.example.laundry.entity.Machine;
import com.example.laundry.repository.MachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MachineService {
    
    @Autowired
    private MachineRepository machineRepository;

    // ===== 基本查詢方法 =====
    
    public List<Machine> getAllMachines() {
        return machineRepository.findAll();
    }
    
    public List<Machine> getMachinesByFloor(Integer floor) {
        return machineRepository.findByFloor(floor);
    }
    
    public Machine findById(Integer id) {
        return machineRepository.findById(id).orElse(null);
    }
    
    public Optional<Machine> getMachineById(Integer id) {
        return machineRepository.findById(id);
    }
    
    public List<Machine> getMachinesByType(Machine.MachineType type) {
        return machineRepository.findByType(type);
    }
    
    public List<Machine> getMachinesByStatus(Machine.MachineStatus status) {
        return machineRepository.findByStatus(status);
    }
    
    public List<Machine> getAvailableMachinesByFloor(Integer floor) {
        return machineRepository.findByFloorAndStatus(floor, Machine.MachineStatus.AVAILABLE);
    }

    // ===== 狀態管理方法 =====
    
    public Machine updateMachineStatus(Integer machineId, Machine.MachineStatus status) {
        Machine machine = findById(machineId);
        if (machine == null) {
            return null;
        }
        
        machine.setStatus(status);
        
        if (status != Machine.MachineStatus.IN_USE) {
            machine.setCurrentUserRoom(null);
            machine.setRemainingMinutes(null);
        }
        
        return machineRepository.save(machine);
    }
    
    public Machine updateStatus(Integer id, Machine.MachineStatus status, 
                               String currentUserRoom, Integer remainingMinutes) {
        Machine machine = findById(id);
        if (machine == null) {
            return null;
        }
        
        machine.setStatus(status);
        
        if (status == Machine.MachineStatus.AVAILABLE || 
            status == Machine.MachineStatus.OUT_OF_ORDER) {
            machine.setCurrentUserRoom(null);
            machine.setRemainingMinutes(null);
        } else if (status == Machine.MachineStatus.IN_USE) {
            machine.setCurrentUserRoom(currentUserRoom);
            machine.setRemainingMinutes(remainingMinutes);
        }
        
        return machineRepository.save(machine);
    }
    
    public Machine toggleStatus(Integer id) {
        Machine machine = findById(id);
        if (machine == null) {
            return null;
        }
        
        Machine.MachineStatus newStatus = 
            (machine.getStatus() == Machine.MachineStatus.AVAILABLE) ? 
                Machine.MachineStatus.OUT_OF_ORDER : 
                Machine.MachineStatus.AVAILABLE;
        
        return updateStatus(id, newStatus, null, null);
    }

    // ===== 使用者操作方法 =====
    
    public Machine startUsing(Integer machineId, String userRoom, Integer minutes) {
        return updateStatus(machineId, Machine.MachineStatus.IN_USE, userRoom, minutes);
    }
    
    public Machine stopUsing(Integer machineId) {
        return updateStatus(machineId, Machine.MachineStatus.AVAILABLE, null, null);
    }

    // ===== 儲存方法 =====
    
    /**
     * 主要儲存方法
     */
    public Machine save(Machine machine) {
        return machineRepository.save(machine);
    }
    
    // 🆕 向後兼容方法：讓其他程式可以繼續使用 saveMachine()
    /**
     * 向後兼容方法（其他程式使用）
     */
    public Machine saveMachine(Machine machine) {
        return save(machine);
    }

    // ===== 實用方法 =====
    
    public boolean existsById(Integer id) {
        return machineRepository.existsById(id);
    }
    
    public boolean isUserUsingAnyMachine(String userRoom) {
        List<Machine> machines = getAllMachines();
        return machines.stream()
                .anyMatch(m -> userRoom.equals(m.getCurrentUserRoom()) && 
                              m.getStatus() == Machine.MachineStatus.IN_USE);
    }
    
    public Machine getUserCurrentMachine(String userRoom) {
        List<Machine> machines = getAllMachines();
        return machines.stream()
                .filter(m -> userRoom.equals(m.getCurrentUserRoom()) && 
                            m.getStatus() == Machine.MachineStatus.IN_USE)
                .findFirst()
                .orElse(null);
    }
    
    public long countByFloorAndStatus(Integer floor, Machine.MachineStatus status) {
        return getMachinesByFloor(floor).stream()
                .filter(m -> m.getStatus() == status)
                .count();
    }
}
