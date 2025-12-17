package com.esma.reservation.manager.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "room_number", unique = true)
    private String roomNumber;

    @Column(name = "room_type")
    private String roomType;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "is_full")
    private Boolean isFull;
}