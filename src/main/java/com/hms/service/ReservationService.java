package com.hms.service;

import com.hms.dto.ReservationDto;

public interface ReservationService {
    ReservationDto postReservation(ReservationDto employee);
    ReservationDto[] getReservations(Integer pageNumber, Integer pageSize);
    ReservationDto replaceReservation(ReservationDto employee);
    ReservationDto modifyReservation(ReservationDto employee);
    void deleteReservation(Long employee_id);
    ReservationDto getReservationById(Long employee_id);
}
