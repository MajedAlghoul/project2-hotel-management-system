package com.hms.service;

import com.hms.dto.ReservationDto;

import java.util.List;

public interface ReservationService {
    ReservationDto postReservation(ReservationDto employee);
    List<ReservationDto> getReservations();
    ReservationDto replaceReservation(ReservationDto employee);
    ReservationDto modifyReservation(ReservationDto employee);
    void deleteReservation(Long employee_id);
    ReservationDto getReservationById(Long employee_id);
}
