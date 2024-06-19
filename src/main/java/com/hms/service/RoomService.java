package com.hms.service;

import com.hms.dto.RoomDto;

public interface RoomService {
    RoomDto postRoom(RoomDto room);
    RoomDto[] getRooms(Integer pageNumber, Integer pageSize);
    RoomDto replaceRoom(RoomDto room);
    RoomDto modifyRoom(RoomDto room);
    void deleteRoom(Long room_id);
    RoomDto getRoomById(Long room_id);
}
