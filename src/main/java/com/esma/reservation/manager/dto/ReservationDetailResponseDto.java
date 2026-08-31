package com.esma.reservation.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDetailResponseDto {
    private String requestId;
    private String guestName;
    private String checkInDate;
    private String checkOutDate;
    private String roomType;
    private Long roomId;
    private Integer numberOfGuests;
    private List<ReservationGuestDetailDto> guestDetails;
}