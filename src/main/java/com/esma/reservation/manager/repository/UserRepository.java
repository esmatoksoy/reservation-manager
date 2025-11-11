package com.esma.reservation.manager.repository;

import com.esma.reservation.manager.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring Data JPA automatically understands this method name
    // It will return a User by searching the 'email' column
    Optional<User> findByEmail(String email);

}