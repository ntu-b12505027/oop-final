package com.example.laundry.entity;

import javax.persistence.*;

@Entity
@Table(name = "dormitories")
public class Dormitory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer dormitoryId;
    
    private String name;
    
    // 建構子
    public Dormitory() {}
    
    public Dormitory(String name) {
        this.name = name;
    }
    
    // Getter/Setter
    public Integer getDormitoryId() { return dormitoryId; }
    public void setDormitoryId(Integer dormitoryId) { this.dormitoryId = dormitoryId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
