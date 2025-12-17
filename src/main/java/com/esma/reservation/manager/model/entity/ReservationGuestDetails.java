package com.esma.reservation.manager.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reservation_guest_details")
public class ReservationGuestDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "allergies")
    private String allergies;

    @Column(name = "room_preferences")
    private String roomPreferences;

    @Column(name = "extra_needs")
    private String extraNeeds;

    @Column(name = "expected_arrival")
    private LocalDateTime expectedArrival;

}