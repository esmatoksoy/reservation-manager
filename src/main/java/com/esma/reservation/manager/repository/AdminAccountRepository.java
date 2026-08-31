// Java
package com.esma.reservation.manager.repository;

import com.esma.reservation.manager.model.entity.AdminAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminAccountRepository extends JpaRepository<AdminAccount, Long> {
    Optional<AdminAccount> findByUsername(String username);
}
