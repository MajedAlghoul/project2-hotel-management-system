package com.hms.controller;

import com.hms.dto.ReservationDto;
import com.hms.exception.IllegalInputException;
import com.hms.responsebody.StandardMessageBody;
import com.hms.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * Contains endpoints related to the Reservation resource.
 */
@RequestMapping("/api/v2/reservation")
@RestController
@Validated
@Tag(name = "Reservation")
public class ReservationController {
    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
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
    @GetMapping(produces = "application/json")
    public ResponseEntity<ReservationDto[]> getReservations(@Validated @RequestParam String pageNumber, @RequestParam String pageSize) {
        int pageNumberInt, pageSizeInt;
        try{
            pageNumberInt = Integer.parseInt(pageNumber);
            pageSizeInt = Integer.parseInt(pageSize);
            if(pageNumberInt < 0 || pageNumberInt > pageSizeInt || pageSizeInt < 1)
                throw new IllegalInputException("Reservation", "pageNumber and or pageSize", pageNumber + " and or " + pageSize);
        } catch(NumberFormatException e){
            throw new IllegalInputException("Reservation", "pageNumber and or pageSize", pageNumber + " and or " + pageSize);
        }
        return ResponseEntity.ok().body(reservationService.getReservations(pageNumberInt, pageSizeInt));
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
    @GetMapping(value = "/{reservation_id}", produces = "application/json")
    public ResponseEntity<ReservationDto> getReservation(@Validated @PathVariable String reservation_id) {
        long id;
        try{
            id = Integer.parseInt(reservation_id);
        } catch(NumberFormatException e){
            throw new IllegalInputException("Reservation", "reservation_id", reservation_id);
        }
        return ResponseEntity.ok().body(reservationService.getReservationById(id));
    }
}
