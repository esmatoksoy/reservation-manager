package com.esma.reservation.manager.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reservation_guest_detail")
public class ReservationGuestDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "allergies")
    private List<String> allergies;

    @Column(name = "room_preferences")
    private String roomPreferences;

    @Column(name = "extra_needs")
    private String extraNeeds;

    @Column(name = "special_requests")
    private String specialRequests;

    @Column(name = "allergies_other")
    private String allergiesOther;

    @Column(name = "expected_arrival")
    private LocalDateTime expectedArrival;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_guest_id", referencedColumnName = "id")
    private ReservationGuest reservationGuest;

}