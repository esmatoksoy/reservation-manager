package com.esma.reservation.manager.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewGuestDto {
    private String firstName;
    private String lastName;
    private String gender;
    private Integer age;
    private Long roomId;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime expectedArrival;

    private List<String> allergies;
    private String allergiesOther;
    private String roomPreferences;
    private String extraNeeds;
    private String specialRequests;
}

