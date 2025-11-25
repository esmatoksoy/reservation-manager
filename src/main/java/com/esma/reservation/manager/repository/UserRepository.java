package com.esma.reservation.manager.repository;

import com.esma.reservation.manager.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
    // Spring Data JPA automatically understands this method name, it will return a User by searching the 'email' column.Also JpaRepository provides CRUD operations for User entity
    // This is a custom "query method." By naming it findBy..., Spring automatically writes the SQL query SELECT * FROM users WHERE email = ?.
    Optional<User> findByEmail(String email);
    // Optional to handle the case where no user is found with the given email
}