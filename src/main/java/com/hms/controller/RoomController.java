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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * Contains endpoints related to the Room resource.
 */
@RequestMapping("/api/v1/room")
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
    @GetMapping(produces = "application/json")
    public ResponseEntity<RoomDto[]> getRooms(@Validated @RequestParam String pageNumber, @RequestParam String pageSize) {
        int pageNumberInt, pageSizeInt;
        try{
            pageNumberInt = Integer.parseInt(pageNumber);
            pageSizeInt = Integer.parseInt(pageSize);
            if(pageNumberInt < 0 || pageNumberInt > pageSizeInt || pageSizeInt < 1)
                throw new IllegalInputException("Room", "pageNumber and or pageSize", pageNumber + " and or " + pageSize);
        } catch(NumberFormatException e){
            throw new IllegalInputException("Room", "pageNumber and or pageSize", pageNumber + " and or " + pageSize);
        }
        return ResponseEntity.ok().body(roomService.getRooms(pageNumberInt, pageSizeInt));
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
