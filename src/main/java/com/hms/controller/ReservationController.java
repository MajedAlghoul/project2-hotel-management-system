package com.hms.controller;

import com.hms.dto.ReservationDto;
import com.hms.exception.IllegalInputException;
import com.hms.model.Reservation;
import com.hms.repository.CustomerRepository;
import com.hms.responsebody.StandardMessageBody;
import com.hms.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

/**
 * Contains endpoints related to the Reservation resource.
 */
@RequestMapping("/api/v2/reservation")
@RestController
@Validated
@Tag(name = "Reservation")
public class ReservationController {
    private final ReservationService reservationService;
    private final CustomerRepository customerRepository;

    @Autowired
    public ReservationController(ReservationService reservationService, CustomerRepository customerRepository) {
        this.reservationService = reservationService;
        this.customerRepository = customerRepository;
    }

    @Operation(
            description = "Endpoint for creating a Reservation",
            summary = "Create Reservation",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"

                    ),
                    @ApiResponse(
                            description = "Unauthorized",
                            responseCode = "403"
                    ),
            }
    )
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @PostMapping(produces = "application/json")
    public ResponseEntity<?> registerReservation (@Validated @RequestBody ReservationDto reservationDto){

        return ResponseEntity.ok().body(reservationService.postReservation(reservationDto));
    }

    @Operation(
            description = "Endpoint for fetching a list of Reservations",
            summary = "Fetch Reservations",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized",
                            responseCode = "403"
                    )
            }
    )
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @GetMapping(produces = "application/json")
    public ResponseEntity<List<ReservationDto>> getReservations() {
        return ResponseEntity.ok().body(reservationService.getReservations());
    }

    @Operation(
            description = "Endpoint for replacing an Reservation",
            summary = "Replace Reservation",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized",
                            responseCode = "403"
                    )
            }
    )
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @PutMapping(produces = "application/json")
    public ResponseEntity<ReservationDto> putReservation(@Validated @RequestBody ReservationDto reservation) {
        return ResponseEntity.ok().body(reservationService.replaceReservation(reservation));
    }

    @Operation(
            description = "Endpoint for partially updating an Reservation",
            summary = "Partially update Reservation",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized",
                            responseCode = "403"
                    )
            }
    )
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @PatchMapping(produces = "application/json")
    public ResponseEntity<ReservationDto> patchReservation(@Validated @RequestBody ReservationDto reservation) {
        return ResponseEntity.ok().body(reservationService.modifyReservation(reservation));
    }

    @Operation(
            description = "Endpoint for deleting an Reservation",
            summary = "Delete Reservation",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized",
                            responseCode = "403"
                    )
            }
    )
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @DeleteMapping(value = "/{reservation_id}", produces = "application/json")
    public ResponseEntity<HashMap<String, String>> deleteReservation(@Validated @PathVariable String reservation_id) {
        long id;
        try{
            id = Integer.parseInt(reservation_id);
        } catch(NumberFormatException e){
            throw new IllegalInputException("Reservation", "reservation_id", reservation_id);
        }
        reservationService.deleteReservation(id);
        return ResponseEntity.ok().body(new StandardMessageBody("Deleted successfully.").getContent());
    }

    @Operation(
            description = "Endpoint for getting an Reservation",
            summary = "Get Reservation",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized",
                            responseCode = "403"
                    )
            }
    )
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @GetMapping(value = "/{reservation_id}", produces = "application/json")
    public ResponseEntity<ReservationDto> getReservation(@Validated @PathVariable String reservation_id, Principal principal) {
        long id;
        try{
            id = Integer.parseInt(reservation_id);
        } catch(NumberFormatException e){
            throw new IllegalInputException("Reservation", "reservation_id", reservation_id);
        }
        //BillDto b=billService.getBillById(id);
        ReservationDto r=reservationService.getReservationById(id);

        //logger.info("Principal name: {}", principal.getName());
        //billService.
        //CustomerDto cust = customerService.getCustomerById(id);

        // Ensure only the account owner can access their account details
        if (!r.getCustomerEmail().equals(principal.getName())) {
            throw new AccessDeniedException("You are not authorized to access this reservation");
        }
        return ResponseEntity.ok().body(r);
    }
}
