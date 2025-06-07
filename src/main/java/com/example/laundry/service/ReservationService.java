package com.example.laundry.service;

import com.example.laundry.entity.Reservation;
import com.example.laundry.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {
    
    @Autowired
    private ReservationRepository reservationRepository;
    
    // 查詢所有預約
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }
    
    // 根據使用者ID查詢預約
    public List<Reservation> getReservationsByUserId(Integer userId) {
        return reservationRepository.findByUserId(userId);
    }
    
    // 根據機台ID查詢預約
    public List<Reservation> getReservationsByMachineId(Integer machineId) {
        return reservationRepository.findByMachineId(machineId);
    }
    
    // 新增預約
    public Reservation saveReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }
    
    // 取消預約
    public Reservation cancelReservation(Integer reservationId) {
        Optional<Reservation> reservationOpt = reservationRepository.findById(reservationId);
        if (reservationOpt.isPresent()) {
            Reservation reservation = reservationOpt.get();
            reservation.setStatus(Reservation.ReservationStatus.CANCELED);
            return reservationRepository.save(reservation);
        }
        return null;
    }
    
    // 根據狀態查詢預約
    public List<Reservation> getReservationsByStatus(Reservation.ReservationStatus status) {
        return reservationRepository.findByStatus(status);
    }
}
