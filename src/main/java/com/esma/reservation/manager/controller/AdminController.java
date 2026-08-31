package com.esma.reservation.manager.controller;

import com.esma.reservation.manager.dto.AdminLoginRequestDto;
import com.esma.reservation.manager.dto.AdminLoginResponseDto;
import com.esma.reservation.manager.model.entity.AdminAccount;
import com.esma.reservation.manager.repository.AdminAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AdminController {

    private final AdminAccountRepository adminAccountRepository;

    @PostMapping("/login")
    public ResponseEntity<AdminLoginResponseDto> login(@RequestBody AdminLoginRequestDto request) {
        AdminAccount admin = adminAccountRepository.findByUsername(request.getUsername())
                .orElse(null);

        if (admin == null || !admin.getPassword().equals(request.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        AdminLoginResponseDto response = AdminLoginResponseDto.builder()
                .adminId(admin.getId())
                .username(admin.getUsername())
                .role(admin.getRole() != null ? admin.getRole().name() : "ADMIN")
                .build();

        return ResponseEntity.ok(response);

    }
}
