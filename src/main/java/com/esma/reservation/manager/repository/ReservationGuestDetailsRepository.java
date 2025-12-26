// Java
package com.esma.reservation.manager.repository;

import com.esma.reservation.manager.model.entity.ReservationGuestDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationGuestDetailsRepository extends JpaRepository<ReservationGuestDetail, Long> {

}
