package com.esma.reservation.manager.dto;

// This record will carry the registration data from the user
public record RegisterRequest(
        String email,
        String password,
        String firstName,
        String lastName
) {}