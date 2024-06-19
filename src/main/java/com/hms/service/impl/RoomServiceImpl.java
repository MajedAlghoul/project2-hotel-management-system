package com.hms.service.impl;

import com.hms.dto.RoomDto;
import com.hms.exception.DuplicateResourceException;
import com.hms.exception.ResourceNotFoundException;
import com.hms.model.Room;
import com.hms.repository.RoomRepository;
import com.hms.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service // Indicates that this class is a service provider (contains business functionalities)
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;

    @Autowired
    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public RoomDto postRoom(RoomDto room) {
        if(roomRepository.existsById(room.getRoomId()))
            throw new DuplicateResourceException("Room", "room_id", String.valueOf(room.getRoomId()));
        Room newRoom = mapToEntity(room);
        return mapToDto(roomRepository.save(newRoom));
    }

    @Override
    public RoomDto[] getRooms(Integer pageNumber, Integer pageSize) {
        Object[] roomsObjects = roomRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent().toArray();
        RoomDto[] roomsDtos = new RoomDto[roomsObjects.length];
        for(int i = 0; i < roomsObjects.length; i++)
            if (roomsObjects[i] instanceof Room)
                roomsDtos[i] = mapToDto((Room) roomsObjects[i]);
        if(roomsDtos.length == 0)
            throw new ResourceNotFoundException("Room", "pageNumber and or pageSize", pageNumber + " and or " + pageSize);
        return roomsDtos;
    }

    @Override
    public RoomDto replaceRoom(RoomDto room) {
        if(!roomRepository.existsById(room.getRoomId()))
            throw new ResourceNotFoundException("Room", "room_id", String.valueOf(room.getRoomId()));
        return mapToDto(roomRepository.save(mapToEntity(room)));
    }

    @Override
    public RoomDto modifyRoom(RoomDto partialRoom) {
        Room originalRoom = roomRepository.findById(partialRoom.getRoomId()).orElseThrow(() -> new ResourceNotFoundException("Room", "email", String.valueOf(partialRoom.getRoomId())));
        if(!partialRoom.getDetails().isEmpty())
            originalRoom.setDetails(partialRoom.getDetails());
        if(partialRoom.getPrice() != null)
            originalRoom.setPrice(partialRoom.getPrice());
        if(partialRoom.getFacilities() != null)
            originalRoom.setFacilities(partialRoom.getFacilities());
        if(partialRoom.getCapacity() != null)
            originalRoom.setCapacity(partialRoom.getCapacity());
        if(partialRoom.getSize() != null)
            originalRoom.setSize(partialRoom.getSize());
        if(partialRoom.getFeatures() != null)
            originalRoom.setFeatures(partialRoom.getFeatures());
        if(partialRoom.getType() != null)
            originalRoom.setType(partialRoom.getType());
        if(partialRoom.getAvailability() != null)
            originalRoom.setAvailability(partialRoom.getAvailability());
        if(partialRoom.getStatus() != null)
            originalRoom.setStatus(partialRoom.getStatus());
        roomRepository.save(originalRoom);
        return mapToDto(originalRoom);
    }


    @Override
    public void deleteRoom(Long room_id) {
        if(!roomRepository.existsById(room_id))
            throw new ResourceNotFoundException("Room", "room_id", String.valueOf(room_id));
        roomRepository.deleteById(room_id);
    }

    @Override
    public RoomDto getRoomById(Long room_id) {
        return mapToDto(roomRepository.findById(room_id).orElseThrow(() -> new ResourceNotFoundException("Room", "room_id", String.valueOf(room_id))));
    }

    private RoomDto mapToDto(Room room) {
        return RoomDto.builder()
                .details(room.getDetails())
                .price(room.getPrice())
                .facilities(room.getFacilities())
                .capacity(room.getCapacity())
                .size(room.getSize())
                .features(room.getFeatures())
                .type(room.getType())
                .availability(room.getAvailability())
                .status(room.getStatus())
                .build();
    }

    private Room mapToEntity(RoomDto roomDto) {
        return Room.builder()
                .details(roomDto.getDetails())
                .price(roomDto.getPrice())
                .facilities(roomDto.getFacilities())
                .capacity(roomDto.getCapacity())
                .size(roomDto.getSize())
                .features(roomDto.getFeatures())
                .type(roomDto.getType())
                .availability(roomDto.getAvailability())
                .status(roomDto.getStatus())
                .build();
    }
}
