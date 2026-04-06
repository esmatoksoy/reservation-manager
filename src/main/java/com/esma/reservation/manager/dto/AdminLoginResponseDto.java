package com.esma.reservation.manager.dto;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class AdminLoginResponseDto {
    private Long adminId;
    private String username;
    private String role;
}

