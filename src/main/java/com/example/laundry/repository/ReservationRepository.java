package com.example.laundry.repository;

import com.example.laundry.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;  // 確保有這個 import

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    List<Reservation> findByUserId(Integer userId);
    List<Reservation> findByMachineId(Integer machineId);
    List<Reservation> findByStatus(Reservation.ReservationStatus status);
    List<Reservation> findByUserIdAndStatus(Integer userId, Reservation.ReservationStatus status);
}
