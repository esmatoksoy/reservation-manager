package com.esma.reservation.manager.repository;

import com.esma.reservation.manager.model.entity.ReservationGuest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationGuestRepository extends JpaRepository<ReservationGuest, Long> {

}

