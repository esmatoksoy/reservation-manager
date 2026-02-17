package com.esma.reservation.manager.dto;

import jakarta.validation.constraints.NotNull;

public class ReservationRequestDto {
    @NotNull(message = "Request ID cannot be null")
    private Long requestId;
}
