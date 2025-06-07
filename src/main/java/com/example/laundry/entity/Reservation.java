package com.example.laundry.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reservationId;
    
    private Integer userId;
    private Integer machineId;
    private String studentId;  // 新增學號欄位
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime bookingTime;
    
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;
    
    // 建構子
    public Reservation() {}
    
    public Reservation(Integer userId, Integer machineId, String studentId, 
                      LocalDateTime startTime, LocalDateTime endTime, ReservationStatus status) {
        this.userId = userId;
        this.machineId = machineId;
        this.studentId = studentId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.bookingTime = LocalDateTime.now();
    }
    
    // 原有的 getter/setter...
    
    // 新增學號的 getter/setter
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    
    // 其他 getter/setter 保持不變...
    public Integer getReservationId() { return reservationId; }
    public void setReservationId(Integer reservationId) { this.reservationId = reservationId; }
    
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    
    public Integer getMachineId() { return machineId; }
    public void setMachineId(Integer machineId) { this.machineId = machineId; }
    
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    
    public LocalDateTime getBookingTime() { return bookingTime; }
    public void setBookingTime(LocalDateTime bookingTime) { this.bookingTime = bookingTime; }
    
    public ReservationStatus getStatus() { return status; }
    public void setStatus(ReservationStatus status) { this.status = status; }
    
    // 列舉
    public enum ReservationStatus {
        UPCOMING, IN_PROGRESS, COMPLETED, CANCELED
    }
}
