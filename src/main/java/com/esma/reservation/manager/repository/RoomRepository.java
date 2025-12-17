// Java
package com.esma.reservation.manager.repository;

import com.esma.reservation.manager.model.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    Optional<Room> findByRoomNumber(String roomNumber);
}
