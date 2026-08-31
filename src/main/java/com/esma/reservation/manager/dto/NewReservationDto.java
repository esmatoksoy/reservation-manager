package com.esma.reservation.manager.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewReservationDto {
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime arrivalDate;

    private String roomType;
    private String specialRequests;
    private List<NewGuestDto> guests;
    private boolean sendEmail;
    private boolean isAdminUpdate;
}
