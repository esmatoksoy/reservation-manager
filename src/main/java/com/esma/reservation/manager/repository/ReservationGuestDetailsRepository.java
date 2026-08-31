// Java
package com.esma.reservation.manager.repository;

import com.esma.reservation.manager.model.entity.ReservationGuestDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReservationGuestDetailsRepository extends JpaRepository<ReservationGuestDetail, Long> {
    @Query("SELECT rgd FROM ReservationGuestDetail rgd WHERE rgd.reservationGuest.id = :guestId")
    Optional<ReservationGuestDetail> findByReservationGuestId(@Param("guestId") Long guestId);
}
