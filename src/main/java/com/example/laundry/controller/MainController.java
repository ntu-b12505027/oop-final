package com.example.laundry.controller;

import com.example.laundry.entity.Machine;
import com.example.laundry.entity.Reservation;
import com.example.laundry.service.MachineService;
import com.example.laundry.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;  // 🆕 新增的 import

@Controller
public class MainController {
    
    @Autowired
    private MachineService machineService;
    
    @Autowired
    private ReservationService reservationService;
    
    @GetMapping("/")
    public String index(@RequestParam(defaultValue = "1") Integer floor, Model model) {
        List<Machine> machines = machineService.getMachinesByFloor(floor);
        model.addAttribute("title", "宿舍洗衣機管理系統");
        model.addAttribute("currentFloor", floor);
        model.addAttribute("machines", machines);
        return "index";
    }
    
    // 管理員面板主頁
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String admin(@RequestParam(defaultValue = "all") String view, Model model) {
        
        if ("machines".equals(view)) {
            // 機台管理視圖
            List<Machine> machines = machineService.getAllMachines();
            model.addAttribute("machines", machines);
            model.addAttribute("currentView", "machines");
            
        } else if ("reservations".equals(view)) {
            // 預約管理視圖
            List<Reservation> reservations = reservationService.getAllReservations();
            model.addAttribute("reservations", reservations);
            model.addAttribute("currentView", "reservations");
            
        } else {
            // 總覽視圖
            List<Machine> machines = machineService.getAllMachines();
            List<Reservation> reservations = reservationService.getAllReservations();
            
            // 統計資訊
            long availableCount = machines.stream().filter(m -> m.getStatus() == Machine.MachineStatus.AVAILABLE).count();
            long inUseCount = machines.stream().filter(m -> m.getStatus() == Machine.MachineStatus.IN_USE).count();
            long outOfOrderCount = machines.stream().filter(m -> m.getStatus() == Machine.MachineStatus.OUT_OF_ORDER).count();
            
            model.addAttribute("machines", machines);
            model.addAttribute("reservations", reservations);
            model.addAttribute("totalMachines", machines.size());
            model.addAttribute("availableCount", availableCount);
            model.addAttribute("inUseCount", inUseCount);
            model.addAttribute("outOfOrderCount", outOfOrderCount);
            model.addAttribute("currentView", "overview");
        }
        
        return "admin";
    }
    
    // ===== 新增的機台編輯功能 =====
    
    /**
     * 獨立的機台管理頁面
     */
    @GetMapping("/admin/machines")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminMachines(Model model) {
        List<Machine> machines = machineService.getAllMachines();
        model.addAttribute("machines", machines);
        model.addAttribute("currentView", "machines");
        return "admin";
    }
    
    /**
     * 顯示編輯機台頁面
     */
    @GetMapping("/admin/machines/{id}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String showEditMachine(@PathVariable Integer id, Model model) {
        Machine machine = machineService.findById(id);
        if (machine == null) {
            return "redirect:/admin?view=machines&error=機器不存在";
        }
        
        model.addAttribute("machine", machine);
        model.addAttribute("statuses", Machine.MachineStatus.values());
        return "admin/edit-machine";
    }
    
    /**
     * 處理機台編輯更新
     */
    @PostMapping("/admin/machines/{id}/update")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateMachine(
            @PathVariable Integer id,
            @RequestParam Machine.MachineStatus status,
            @RequestParam(required = false) String currentUserRoom,
            @RequestParam(required = false) Integer remainingMinutes,
            RedirectAttributes redirectAttributes) {
        
        try {
            Machine machine = machineService.findById(id);
            if (machine == null) {
                redirectAttributes.addFlashAttribute("error", "機器不存在");
                return "redirect:/admin?view=machines";
            }
            
            machine.setStatus(status);
            
            // 根據狀態設定相關欄位
            if (status == Machine.MachineStatus.AVAILABLE || 
                status == Machine.MachineStatus.OUT_OF_ORDER) {
                machine.setCurrentUserRoom(null);
                machine.setRemainingMinutes(null);
            } else if (status == Machine.MachineStatus.IN_USE) {
                machine.setCurrentUserRoom(currentUserRoom);
                machine.setRemainingMinutes(remainingMinutes);
            }
            
            machineService.save(machine);
            
            redirectAttributes.addFlashAttribute("success", 
                String.format("機器 %s 的狀態已更新為：%s", machine.getName(), getStatusText(status)));
                
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "更新失敗：" + e.getMessage());
        }
        
        return "redirect:/admin?view=machines";
    }
    
    /**
     * 快速狀態切換（AJAX）
     */
    @PostMapping("/admin/machines/{id}/toggle")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public Map<String, Object> toggleMachineStatus(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Machine machine = machineService.findById(id);
            if (machine == null) {
                response.put("success", false);
                response.put("message", "機器不存在");
                return response;
            }
            
            // 在可用和故障之間切換
            Machine.MachineStatus newStatus = 
                (machine.getStatus() == Machine.MachineStatus.AVAILABLE) ? 
                    Machine.MachineStatus.OUT_OF_ORDER : 
                    Machine.MachineStatus.AVAILABLE;
            
            machine.setStatus(newStatus);
            machine.setCurrentUserRoom(null);
            machine.setRemainingMinutes(null);
            
            machineService.save(machine);
            
            response.put("success", true);
            response.put("newStatus", newStatus.toString());
            response.put("newStatusText", getStatusText(newStatus));
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        
        return response;
    }
    
    // 🆕 ===== 倒數計時支援 API =====
    
    /**
     * 機器時間到自動設為可用
     */
    @PostMapping("/admin/machines/{id}/auto-complete")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public Map<String, Object> autoCompleteMachine(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Machine machine = machineService.findById(id);
            if (machine == null) {
                response.put("success", false);
                response.put("message", "機器不存在");
                return response;
            }
            
            // 設為可用狀態
            machine.setStatus(Machine.MachineStatus.AVAILABLE);
            machine.setCurrentUserRoom(null);
            machine.setRemainingMinutes(null);
            
            machineService.save(machine);
            
            response.put("success", true);
            response.put("message", "機器已自動設為可用");
            
            System.out.println("⏰ 機器 " + machine.getName() + " 時間到，自動設為可用");
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        
        return response;
    }
    
    /**
     * 獲取所有機器的即時狀態（用於同步）
     */
    @GetMapping("/admin/machines/status")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public List<Map<String, Object>> getMachinesStatus() {
        List<Machine> machines = machineService.getAllMachines();
        
        return machines.stream().map(machine -> {
            Map<String, Object> status = new HashMap<>();
            status.put("machineId", machine.getMachineId());
            status.put("status", machine.getStatus().name());
            status.put("remainingMinutes", machine.getRemainingMinutes());
            status.put("currentUserRoom", machine.getCurrentUserRoom());
            return status;
        }).collect(Collectors.toList());
    }
    
    // ===== 原有的管理功能 =====
    
    // 更新機台狀態
    @PostMapping("/admin/update-status")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateMachineStatus(@RequestParam Integer machineId, 
                                    @RequestParam Machine.MachineStatus status,
                                    @RequestParam(required = false) String userRoom,
                                    @RequestParam(required = false) Integer remainingMinutes,
                                    RedirectAttributes redirectAttributes) {
        try {
            Machine machine = machineService.getMachineById(machineId).orElse(null);
            if (machine != null) {
                machine.setStatus(status);
                
                // 根據狀態設定相關欄位
                if (status == Machine.MachineStatus.IN_USE) {
                    machine.setCurrentUserRoom(userRoom != null ? userRoom : "未知");
                    machine.setRemainingMinutes(remainingMinutes != null ? remainingMinutes : 60);
                } else {
                    machine.setCurrentUserRoom(null);
                    machine.setRemainingMinutes(null);
                }
                
                machineService.save(machine);
                redirectAttributes.addFlashAttribute("success", 
                    "機台 " + machine.getName() + " 狀態已更新為：" + getStatusText(status));
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "更新失敗：" + e.getMessage());
        }
        
        return "redirect:/admin?view=machines";
    }
    
    // 批量重置機台狀態
    @PostMapping("/admin/reset-all")
    @PreAuthorize("hasRole('ADMIN')")
    public String resetAllMachines(RedirectAttributes redirectAttributes) {
        try {
            List<Machine> machines = machineService.getAllMachines();
            int resetCount = 0;
            
            for (Machine machine : machines) {
                if (machine.getStatus() == Machine.MachineStatus.IN_USE) {
                    machine.setStatus(Machine.MachineStatus.AVAILABLE);
                    machine.setCurrentUserRoom(null);
                    machine.setRemainingMinutes(null);
                    machineService.save(machine);
                    resetCount++;
                }
            }
            
            redirectAttributes.addFlashAttribute("success", 
                "已重置 " + resetCount + " 台使用中的機台為可用狀態");
                
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "批量重置失敗：" + e.getMessage());
        }
        
        return "redirect:/admin?view=machines";
    }
    
    // 狀態文字轉換
    private String getStatusText(Machine.MachineStatus status) {
        switch (status) {
            case AVAILABLE: return "可用";
            case IN_USE: return "使用中";
            case OUT_OF_ORDER: return "故障";
            default: return "未知";
        }
    }
}
