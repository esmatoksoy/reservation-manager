package com.esma.reservation.manager.model.entity;
import com.esma.reservation.manager.model.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reservation_number", nullable = false, unique = true)
    private String reservationNumber;

    @Column(name = "booked_date")
    private LocalDate bookedDate;

    @Enumerated(EnumType.STRING)
        @Column(name = "status", nullable = false)
        private ReservationStatus status;


}