package com.esma.reservation.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailMessageDto {

    public enum EmailType {
        RESERVATION_FORM_LINK,
        CONFIRMATION,
        DETAILED_RESERVATION
    }

    private EmailType emailType;
    private String toEmail;
    private String reservationNumber;
}

