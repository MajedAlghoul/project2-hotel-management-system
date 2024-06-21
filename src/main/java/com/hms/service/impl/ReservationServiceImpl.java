package com.hms.service.impl;

import com.hms.dto.ReservationDto;
import com.hms.exception.DuplicateResourceException;
import com.hms.exception.NoContentException;
import com.hms.exception.ResourceNotFoundException;
import com.hms.model.Reservation;
import com.hms.model.Role;
import com.hms.model.Reservation;
import com.hms.model.Room;
import com.hms.repository.CustomerRepository;
import com.hms.repository.ReservationRepository;
import com.hms.repository.RoleRepository;
import com.hms.repository.RoomRepository;
import com.hms.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service // Indicates that this class is a service provider (contains business functionalities)
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final CustomerRepository customerRepository;
    private final RoomRepository roomRepository;

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository, CustomerRepository customerRepository, RoomRepository roomRepository) {
        this.reservationRepository = reservationRepository;
        this.customerRepository = customerRepository;
        this.roomRepository = roomRepository;
    }

    public ReservationDto postReservation(ReservationDto reservation) {
        //if(reservationRepository.existsById(reservation.getId()))
        //    throw new DuplicateResourceException("Reservation", "reservation_id", String.valueOf(reservation.getId()));
        Reservation newReservation = mapToEntity(reservation);
        return mapToDto(reservationRepository.save(newReservation));
    }

    @Override
    public List<ReservationDto> getReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        if (reservations.isEmpty()) {
            throw new NoContentException("No reservations registered yet");
        }
        return reservations.stream().map(this::mapToDto).collect(Collectors.toList());
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
            originalReservation.setCheckInData(partialReservation.getCheckin());
        if(partialReservation.getCheckout() != null)
            originalReservation.setCheckOutData(partialReservation.getCheckout());
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
                .customerEmail(reservation.getCustomer().getEmail())
                .checkingStatus(reservation.getCheckingStatus())
                .checkin(reservation.getCheckInData())
                .checkout(reservation.getCheckOutData())
                .room(new ArrayList<>(reservation.getRoom()).get(0).getId())
                .id(reservation.getId())
                .build();
    }

    private Reservation mapToEntity(ReservationDto reservationDto) {
        Set<Room> rooms = new HashSet<>();
        rooms.add(roomRepository.findById(reservationDto.getRoom()).get() ) ;
        return Reservation.builder()
                .customer(customerRepository.findByEmail(reservationDto.getCustomerEmail()).get() )
                .checkingStatus(reservationDto.getCheckingStatus())
                .checkInData(reservationDto.getCheckin())
                .checkOutData(reservationDto.getCheckout())
                .room(rooms)
                .build();
    }
}
