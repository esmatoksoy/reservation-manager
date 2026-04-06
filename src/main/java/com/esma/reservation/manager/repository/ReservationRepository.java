package com.esma.reservation.manager.repository;

import com.esma.reservation.manager.model.entity.Reservation;
import com.esma.reservation.manager.model.type.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findByReservationNumber(String requestId);
    List<Reservation> findByStatus(ReservationStatus status);
    List<Reservation> findByCustomerIdOrderByBookedDateDesc(Long customerId);

}