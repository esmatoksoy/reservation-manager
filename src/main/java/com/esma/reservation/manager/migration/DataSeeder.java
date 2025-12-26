
package com.esma.reservation.manager.migration;

import com.esma.reservation.manager.model.type.ReservationStatus;
import com.esma.reservation.manager.repository.ReservationRepository;
import com.esma.reservation.manager.repository.CustomerRepository;
import com.esma.reservation.manager.repository.ReservationGuestRepository;
import com.esma.reservation.manager.repository.RoomRepository;

import com.esma.reservation.manager.model.entity.Customer;
import com.esma.reservation.manager.model.entity.Reservation;
import com.esma.reservation.manager.model.entity.ReservationGuest;
import com.esma.reservation.manager.model.entity.ReservationGuestDetail;
import com.esma.reservation.manager.model.entity.Room;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component

public class DataSeeder implements ApplicationRunner {

    private final CustomerRepository customerRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationGuestRepository reservationGuestRepository;
    private final RoomRepository roomRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {

        // customer
        Customer customer = new Customer();
        customer.setEmail("esma@example.com");
        customer.setFirstName("Esma");
        customer.setLastName("Toksoy");
        customer.setPhoneNumber("1234567891");
        customer = customerRepository.save(customer);

        // reservation
        Reservation reservation = new Reservation();
        reservation.setCustomer(customer);
        reservation.setReservationNumber("RES123456");
        reservation.setBookedDate(LocalDateTime.now());
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservation = reservationRepository.save(reservation);
        // reservation linked to created customer
        customer.getReservations().add(reservation);
        customerRepository.save(customer);

        // reservation guest
        ReservationGuest guest = new ReservationGuest();
        guest.setFirstName("Ecesu");
        guest.setLastName("Yıldıran");
        guest.setAge(22);
        guest.setGender("F");
        guest.setReservation(reservation);// link to reservation

        // reservation guest detail
        ReservationGuestDetail detail = new ReservationGuestDetail();
        detail.setAllergies("Peanuts");
        detail.setRoomPreferences("High floor");
        detail.setExtraNeeds("Late check-in");
        detail.setExpectedArrival(LocalDateTime.now().plusDays(1));
        detail.setReservationGuest(guest);// link to guest
        guest.setReservationGuestDetail(detail);// link to reservation detail
        guest = reservationGuestRepository.save(guest);

        // room
        Room room = new Room();
        room.setCapacity(3);
        room.setRoomNumber("8174");
        room.setRoomType("Family");
        room = roomRepository.save(room);

        guest.setRoom(room);
        guest = reservationGuestRepository.save(guest);
        room.getReservationRoomGuests().add(guest);
        roomRepository.save(room);
    }
}