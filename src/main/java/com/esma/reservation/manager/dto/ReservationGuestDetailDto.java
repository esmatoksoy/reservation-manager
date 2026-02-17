package com.esma.reservation.manager.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class ReservationGuestDetailDto {
    private Long guestId;
    private String guestName;
    private List<String> allergies;
    private String allergiesOther;
    private String roomPreferences;
    private String extraNeeds;
    private String specialRequests;
    private LocalDateTime expectedArrival;
}



