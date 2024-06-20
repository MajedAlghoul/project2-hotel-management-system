package com.hms.service.impl;

import com.hms.dto.ReservationDto;
import com.hms.exception.DuplicateResourceException;
import com.hms.exception.ResourceNotFoundException;
import com.hms.model.Reservation;
import com.hms.model.Role;
import com.hms.repository.ReservationRepository;
import com.hms.repository.RoleRepository;
import com.hms.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service // Indicates that this class is a service provider (contains business functionalities)
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public ReservationDto postReservation(ReservationDto reservation) {
        if(reservationRepository.existsById(reservation.getId()))
            throw new DuplicateResourceException("Reservation", "reservation_id", String.valueOf(reservation.getId()));
        Reservation newReservation = mapToEntity(reservation);
        return mapToDto(reservationRepository.save(newReservation));
    }

    @Override
    public ReservationDto[] getReservations(Integer pageNumber, Integer pageSize) {
        Object[] reservationsObjects = reservationRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent().toArray();
        ReservationDto[] reservationsDtos = new ReservationDto[reservationsObjects.length];
        for(int i = 0; i < reservationsObjects.length; i++)
            if (reservationsObjects[i] instanceof Reservation)
                reservationsDtos[i] = mapToDto((Reservation) reservationsObjects[i]);
        if(reservationsDtos.length == 0)
            throw new ResourceNotFoundException("Reservation", "pageNumber and or pageSize", pageNumber + " and or " + pageSize);
        return reservationsDtos;
    }

    @Override
    public ReservationDto replaceReservation(ReservationDto reservation) {
        if(!reservationRepository.existsById(reservation.getId()))
            throw new ResourceNotFoundException("Reservation", "reservation_id", String.valueOf(reservation.getId()));
        return mapToDto(reservationRepository.save(mapToEntity(reservation)));
    }

    @Override
    public ReservationDto modifyReservation(ReservationDto partialReservation) {
        Reservation originalReservation = reservationRepository.findById(partialReservation.getId()).orElseThrow(() -> new ResourceNotFoundException("Reservation", "email", String.valueOf(partialReservation.getId())));
        if(!partialReservation.getCheckingStatus().isEmpty())
            originalReservation.setCheckingStatus(partialReservation.getCheckingStatus());
        if(partialReservation.getCheckin() != null)
            originalReservation.setCheckin(partialReservation.getCheckin());
        if(partialReservation.getCheckout() != null)
            originalReservation.setCheckout(partialReservation.getCheckout());
        reservationRepository.save(originalReservation);
        return mapToDto(originalReservation);
    }


    @Override
    public void deleteReservation(Long reservation_id) {
        if(!reservationRepository.existsById(reservation_id))
            throw new ResourceNotFoundException("Reservation", "reservation_id", String.valueOf(reservation_id));
        reservationRepository.deleteById(reservation_id);
    }

    @Override
    public ReservationDto getReservationById(Long reservation_id) {
        return mapToDto(reservationRepository.findById(reservation_id).orElseThrow(() -> new ResourceNotFoundException("Reservation", "reservation_id", String.valueOf(reservation_id))));
    }

    private ReservationDto mapToDto(Reservation reservation) {
        return ReservationDto.builder()
                .customerEmail(reservation.getCustomerEmail())
                .checkingStatus(reservation.getCheckingStatus())
                .checkin(reservation.getCheckin())
                .checkout(reservation.getCheckout())
                .build();
    }

    private Reservation mapToEntity(ReservationDto reservationDto) {
        return Reservation.builder()
                .customerEmail(reservationDto.getCustomerEmail())
                .checkingStatus(reservationDto.getCheckingStatus())
                .checkin(reservationDto.getCheckin())
                .checkout(reservationDto.getCheckout())
                .build();
    }
}
