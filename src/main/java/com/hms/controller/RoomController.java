package com.hms.controller;

import com.hms.dto.RoomDto;
import com.hms.exception.IllegalInputException;
import com.hms.responsebody.StandardMessageBody;
import com.hms.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * Contains endpoints related to the Room resource.
 */
@RequestMapping("/api/room")
@RestController
@Validated
@Tag(name = "Room")
public class RoomController {
    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @Operation(
            description = "Endpoint for creating a Room",
            summary = "Create Room",
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
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @PostMapping(produces = "application/json")
    public ResponseEntity<?> registerRoom (@Validated @RequestBody RoomDto roomDto){

        return ResponseEntity.ok().body(roomService.postRoom(roomDto));
    }

    @Operation(
            description = "Endpoint for fetching a list of Rooms",
            summary = "Fetch Rooms",
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
    public ResponseEntity<List<RoomDto>> getRooms() {
        return ResponseEntity.ok().body(roomService.getRooms());
    }

    @Operation(
            description = "Endpoint for replacing an Room",
            summary = "Replace Room",
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
    public ResponseEntity<RoomDto> putRoom(@Validated @RequestBody RoomDto room) {
        return ResponseEntity.ok().body(roomService.replaceRoom(room));
    }

    @Operation(
            description = "Endpoint for partially updating an Room",
            summary = "Partially update Room",
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
    public ResponseEntity<RoomDto> patchRoom(@Validated @RequestBody RoomDto room) {
        return ResponseEntity.ok().body(roomService.modifyRoom(room));
    }

    @Operation(
            description = "Endpoint for deleting an Room",
            summary = "Delete Room",
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
    @DeleteMapping(value = "/{room_id}", produces = "application/json")
    public ResponseEntity<HashMap<String, String>> deleteRoom(@Validated @PathVariable String room_id) {
        long id;
        try{
            id = Integer.parseInt(room_id);
        } catch(NumberFormatException e){
            throw new IllegalInputException("Room", "room_id", room_id);
        }
        roomService.deleteRoom(id);
        return ResponseEntity.ok().body(new StandardMessageBody("Deleted successfully.").getContent());
    }

    @Operation(
            description = "Endpoint for getting an Room",
            summary = "Get Room",
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
    @GetMapping(value = "/{room_id}", produces = "application/json")
    public ResponseEntity<RoomDto> getRoom(@Validated @PathVariable String room_id) {
        long id;
        try{
            id = Integer.parseInt(room_id);
        } catch(NumberFormatException e){
            throw new IllegalInputException("Room", "room_id", room_id);
        }
        return ResponseEntity.ok().body(roomService.getRoomById(id));
    }
}
