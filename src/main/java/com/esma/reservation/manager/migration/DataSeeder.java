package com.esma.reservation.manager.migration;

import java.util.List;
import java.util.UUID;

import com.esma.reservation.manager.model.type.ReservationStatus;
import com.esma.reservation.manager.repository.ReservationRepository;
import com.esma.reservation.manager.repository.CustomerRepository;
import com.esma.reservation.manager.repository.ReservationGuestRepository;
import com.esma.reservation.manager.repository.RoomRepository;
import com.esma.reservation.manager.repository.AdminAccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.esma.reservation.manager.model.entity.Customer;
import com.esma.reservation.manager.model.entity.Reservation;
import com.esma.reservation.manager.model.entity.ReservationGuest;
import com.esma.reservation.manager.model.entity.ReservationGuestDetail;
import com.esma.reservation.manager.model.entity.AdminAccount;
import com.esma.reservation.manager.model.type.Role;
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
    private final AdminAccountRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {

        // Create admin accounts
        AdminAccount superAdmin = new AdminAccount();
        superAdmin.setUsername("admin");
        superAdmin.setEmail("admin@hotel.com");
        superAdmin.setPassword(passwordEncoder.encode("admin123"));
        superAdmin.setRole(Role.ADMIN);
        superAdmin.setCreatedAt(LocalDateTime.now());
        superAdmin.setUpdatedAt(LocalDateTime.now());
        superAdmin = adminRepository.save(superAdmin);

        AdminAccount staffAdmin = new AdminAccount();
        staffAdmin.setUsername("staff");
        staffAdmin.setEmail("staff@hotel.com");
        staffAdmin.setPassword(passwordEncoder.encode("staff123"));
        staffAdmin.setRole(Role.ADMIN);
        staffAdmin.setCreatedAt(LocalDateTime.now());
        staffAdmin.setUpdatedAt(LocalDateTime.now());
        staffAdmin = adminRepository.save(staffAdmin);


        // Create rooms first
        Room familyRoom = new Room();
        familyRoom.setCapacity(4);
        familyRoom.setRoomNumber("8174");
        familyRoom.setRoomType("FAMILY");
        familyRoom = roomRepository.save(familyRoom);

        Room suiteRoom = new Room();
        suiteRoom.setCapacity(2);
        suiteRoom.setRoomNumber("9201");
        suiteRoom.setRoomType("SUITE");
        suiteRoom = roomRepository.save(suiteRoom);

        Room standardRoom = new Room();
        standardRoom.setCapacity(2);
        standardRoom.setRoomNumber("7105");
        standardRoom.setRoomType("STANDARD");
        standardRoom = roomRepository.save(standardRoom);

        Room deluxeRoom = new Room();
        deluxeRoom.setCapacity(3);
        deluxeRoom.setRoomNumber("8502");
        deluxeRoom.setRoomType("DELUXE");
        deluxeRoom = roomRepository.save(deluxeRoom);

        // customer
        Customer customer = new Customer();
        customer.setEmail("esma-toksoy@hotmail.com");
        customer.setFirstName("Esma");
        customer.setLastName("Toksoy");
        customer.setPhoneNumber("1234567891");
        customer = customerRepository.save(customer);

        // reservation
        Reservation reservation = new Reservation();
        reservation.setCustomer(customer);
        reservation.setReservationNumber(UUID.randomUUID().toString());
        reservation.setBookedDate(LocalDateTime.now());
        reservation.setStatus(ReservationStatus.PENDING);
        reservation = reservationRepository.save(reservation);

        customer.getReservations().add(reservation);
        customerRepository.save(customer);

        // reservation guest
        ReservationGuest guest = new ReservationGuest();
        guest.setFirstName("Ecesu");
        guest.setLastName("Yıldıran");
        guest.setAge(22);
        guest.setGender("F");
        guest.setReservation(reservation);
        guest.setRoom(familyRoom);

        // reservation guest 2
        ReservationGuest guest2 = new ReservationGuest();
        guest2.setFirstName("Ümmühan");
        guest2.setLastName("Yıldıran");
        guest2.setAge(44);
        guest2.setGender("F");
        guest2.setReservation(reservation);
        guest2.setRoom(familyRoom);

        guest = reservationGuestRepository.save(guest);
        guest2 = reservationGuestRepository.save(guest2);

        familyRoom.getReservationRoomGuests().add(guest);
        familyRoom.getReservationRoomGuests().add(guest2);
        roomRepository.save(familyRoom);
    }
}