package com.hms.service;

import com.hms.dto.RoomDto;

import java.util.List;

public interface RoomService {
    RoomDto postRoom(RoomDto room);
    List<RoomDto> getRooms();
    RoomDto replaceRoom(RoomDto room);
    RoomDto modifyRoom(RoomDto room);
    void deleteRoom(Long room_id);
    RoomDto getRoomById(Long room_id);
}
