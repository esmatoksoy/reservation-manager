package com.esma.reservation.manager.model.entity;
import com.esma.reservation.manager.model.enums.Role;
import jakarta.persistence.*;
import java.time.Instant;
import lombok.*; //added to use for getter setter,it helps to reduce boilerplate(means repetitive code) code

@Data//we can use @Data to generate both getter and setter methods
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "created_at", updatable = false)
    @Builder.Default
    private Instant createdAt = Instant.now();

}
