package com.example.laundry.entity;

import javax.persistence.*;

@Entity
@Table(name = "machines")
public class Machine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer machineId;
    
    private Integer dormitoryId;
    private String name;
    
    @Enumerated(EnumType.STRING)
    private MachineType type;
    
    @Enumerated(EnumType.STRING)
    private MachineStatus status;
    
    private Integer floor;
    private String currentUserRoom;
    private Integer remainingMinutes;
    
    // 建構子
    public Machine() {}
    
    public Machine(Integer dormitoryId, String name, MachineType type, 
                  MachineStatus status, Integer floor) {
        this.dormitoryId = dormitoryId;
        this.name = name;
        this.type = type;
        this.status = status;
        this.floor = floor;
    }
    
    // Getter/Setter 方法
    public Integer getMachineId() { 
        return machineId; 
    }
    public void setMachineId(Integer machineId) { 
        this.machineId = machineId; 
    }
    
    public Integer getDormitoryId() { 
        return dormitoryId; 
    }
    public void setDormitoryId(Integer dormitoryId) { 
        this.dormitoryId = dormitoryId; 
    }
    
    public String getName() { 
        return name; 
    }
    public void setName(String name) { 
        this.name = name; 
    }
    
    public MachineType getType() { 
        return type; 
    }
    public void setType(MachineType type) { 
        this.type = type; 
    }
    
    public MachineStatus getStatus() { 
        return status; 
    }
    public void setStatus(MachineStatus status) {  // 這個方法很重要！
        this.status = status; 
    }
    
    public Integer getFloor() { 
        return floor; 
    }
    public void setFloor(Integer floor) { 
        this.floor = floor; 
    }
    
    public String getCurrentUserRoom() { 
        return currentUserRoom; 
    }
    public void setCurrentUserRoom(String currentUserRoom) { 
        this.currentUserRoom = currentUserRoom; 
    }
    
    public Integer getRemainingMinutes() { 
        return remainingMinutes; 
    }
    public void setRemainingMinutes(Integer remainingMinutes) { 
        this.remainingMinutes = remainingMinutes; 
    }
    
    // 列舉定義
    public enum MachineType {
        WASHING, DRYING
    }
    
    public enum MachineStatus {
        AVAILABLE, IN_USE, OUT_OF_ORDER
    }
}
