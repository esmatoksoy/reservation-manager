package com.esma.reservation.manager.mapper;

import com.esma.reservation.manager.dto.ReservationGuestDetailDto;
import com.esma.reservation.manager.model.entity.ReservationGuest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    @Mapping(target = "guestId", source = "id")
    @Mapping(target = "guestName", expression = "java(guest.getFirstName() + \" \" + guest.getLastName())")
    @Mapping(target = "allergies", source = "reservationGuestDetail.allergies")
    @Mapping(target = "allergiesOther", source = "reservationGuestDetail.allergiesOther")
    @Mapping(target = "roomPreferences", source = "reservationGuestDetail.roomPreferences")
    @Mapping(target = "extraNeeds", source = "reservationGuestDetail.extraNeeds")
    @Mapping(target = "specialRequests", source = "reservationGuestDetail.specialRequests")
    @Mapping(target = "expectedArrival", source = "reservationGuestDetail.expectedArrival")

    ReservationGuestDetailDto toGuestDetailDto(ReservationGuest guest);// Mapping for a single ReservationGuest to ReservationGuestDetailDto
    List<ReservationGuestDetailDto> toGuestDetailDtos(List<ReservationGuest> reservationGuests);
}