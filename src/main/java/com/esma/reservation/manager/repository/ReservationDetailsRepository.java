package com.esma.reservation.manager.repository;

import com.esma.reservation.manager.model.entity.ReservationDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationDetailsRepository extends JpaRepository<ReservationDetails, Long> {


}

