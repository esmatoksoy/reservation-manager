package com.esma.reservation.manager.repository;

import com.esma.reservation.manager.model.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // You will need this for your employee workflow
    Optional<Reservation> findByReservationNumber(String reservationNumber);

}