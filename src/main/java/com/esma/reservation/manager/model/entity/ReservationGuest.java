package com.esma.reservation.manager.model.entity;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reservation_guest")
public class ReservationGuest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "gender")
    private String gender;

    @Column(name = "age")
    private Integer age;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @OneToOne(mappedBy = "reservationGuest", cascade = CascadeType.ALL, orphanRemoval = true)
    private ReservationGuestDetail reservationGuestDetail;

    public void setReservationGuestDetail(ReservationGuestDetail detail) {
        this.reservationGuestDetail = detail;
        if (detail != null) {
            detail.setReservationGuest(this);
        }
    }

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

}