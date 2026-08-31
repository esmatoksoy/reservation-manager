package com.esma.reservation.manager.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CustomerDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
}

