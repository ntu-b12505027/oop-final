package com.example.laundry.controller;

import com.example.laundry.entity.Machine;
import com.example.laundry.entity.Reservation;
import com.example.laundry.service.MachineService;
import com.example.laundry.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;        // 加入這個 import！
import java.util.Optional;

@Controller
@RequestMapping("/reservation")
public class ReservationController {
    
    @Autowired
    private ReservationService reservationService;
    
    @Autowired
    private MachineService machineService;
    
 // 預約機台
    @PostMapping("/make")
    public String makeReservation(@RequestParam Integer machineId,
                                @RequestParam String userRoom,
                                @RequestParam String studentId,  // 新增學號欄位
                                @RequestParam(defaultValue = "1") Integer floor,
                                RedirectAttributes redirectAttributes) {
        
        try {
            // 檢查機台是否可用
            Optional<Machine> machineOpt = machineService.getMachineById(machineId);
            if (!machineOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "機台不存在！");
                return "redirect:/?floor=" + floor;
            }
            
            Machine machine = machineOpt.get();
            if (machine.getStatus() != Machine.MachineStatus.AVAILABLE) {
                redirectAttributes.addFlashAttribute("error", "機台目前不可預約！");
                return "redirect:/?floor=" + floor;
            }
            
            // 建立預約
            Reservation reservation = new Reservation();
            reservation.setUserId(1);  // 暫時使用固定用戶ID
            reservation.setMachineId(machineId);
            reservation.setStudentId(studentId);  // 儲存學號
            reservation.setStartTime(LocalDateTime.now());
            reservation.setEndTime(LocalDateTime.now().plusHours(1));
            reservation.setBookingTime(LocalDateTime.now());
            reservation.setStatus(Reservation.ReservationStatus.UPCOMING);
            
            // 儲存預約
            reservationService.saveReservation(reservation);
            
            // 更新機台狀態為使用中（顯示房號）
            machine.setStatus(Machine.MachineStatus.IN_USE);
            machine.setCurrentUserRoom(userRoom);  // 顯示房號
            machine.setRemainingMinutes(60);
            machineService.saveMachine(machine);
            
            redirectAttributes.addFlashAttribute("success", 
                "預約成功！機台：" + machine.getName() + "，學號：" + studentId + "，房號：" + userRoom);
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "預約失敗：" + e.getMessage());
        }
        
        return "redirect:/?floor=" + floor;
    }

    // 修改「我的預約」方法，新增學號過濾功能
    @GetMapping("/my")
    public String myReservations(@RequestParam(required = false) String studentId, 
                               Model model) {
        
        List<Reservation> reservations;
        
        if (studentId != null && !studentId.trim().isEmpty()) {
            // 根據學號過濾預約
            reservations = reservationService.getAllReservations().stream()
                .filter(r -> studentId.equals(r.getStudentId()))
                .collect(java.util.stream.Collectors.toList());
        } else {
            // 如果沒有學號，顯示所有預約
            reservations = reservationService.getAllReservations();
        }
        
        // 統計各狀態的預約數量
        long upcomingCount = reservations.stream()
            .filter(r -> r.getStatus() == Reservation.ReservationStatus.UPCOMING)
            .count();
        long inProgressCount = reservations.stream()
            .filter(r -> r.getStatus() == Reservation.ReservationStatus.IN_PROGRESS)
            .count();
        long completedCount = reservations.stream()
            .filter(r -> r.getStatus() == Reservation.ReservationStatus.COMPLETED)
            .count();
        long canceledCount = reservations.stream()
            .filter(r -> r.getStatus() == Reservation.ReservationStatus.CANCELED)
            .count();
        
        model.addAttribute("reservations", reservations);
        model.addAttribute("upcomingCount", upcomingCount);
        model.addAttribute("inProgressCount", inProgressCount);
        model.addAttribute("completedCount", completedCount);
        model.addAttribute("canceledCount", canceledCount);
        model.addAttribute("currentStudentId", studentId);
        
        return "my-reservations";
    }


}
