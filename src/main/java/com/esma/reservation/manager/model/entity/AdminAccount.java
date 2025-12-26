package com.esma.reservation.manager.model.entity;
import com.esma.reservation.manager.model.type.Role;
import jakarta.persistence.*;

import java.util.*;
import java.time.LocalDateTime;

import lombok.*; //added to use for getter setter

@Data//we can use @Data to generate both getter and setter methods
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "admin_account")
public class AdminAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "admin_reservation_map",
            joinColumns = @JoinColumn(name = "admin_id"),
            inverseJoinColumns = @JoinColumn(name = "reservation_id")
    )
    @Builder.Default
    private Set<Reservation> reservations = new HashSet<>();

    @Column(name = "created_at", updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name ="deleted_at")
    private LocalDateTime deletedAt;

}