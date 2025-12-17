package com.esma.reservation.manager.model.entity;
import jakarta.persistence.*;
import java.time.Instant;
import lombok.*; //added to use for getter setter,it helps to reduce boilerplate(means repetitive code) code

@Data//we can use @Data to generate both getter and setter methods
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//auto increment for primary key
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "created_at", updatable = false)
    @Builder.Default
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at")
    @Builder.Default
    private Instant updatedAt = Instant.now();

    @Column(name ="deleted_at")
    private Instant deletedAt;//soft delete
}
