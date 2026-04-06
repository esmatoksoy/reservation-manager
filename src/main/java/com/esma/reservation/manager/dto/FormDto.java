package com.esma.reservation.manager.dto;

import org.springframework.format.annotation.DateTimeFormat;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class FormDto {
    private String roomType;
    private String specialRequests;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime expectedArrival;
    private List<ReservationGuestDetailDto> guests;
    private boolean sendEmail;
    private boolean isAdminUpdate;

}
