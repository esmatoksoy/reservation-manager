package com.esma.reservation.manager.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ReservationSummaryDto {
    private Long id;
    private String reservationNumber;
    private LocalDateTime bookedDate;
    private LocalDateTime createdAt; // we will set it to bookedDate when createdAt is not available
    private String status;
    private Long customerId;
}

