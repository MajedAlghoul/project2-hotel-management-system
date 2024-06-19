package com.hms.service.impl;

import com.hms.dto.RoomDto;
import com.hms.exception.DuplicateResourceException;
import com.hms.exception.ResourceNotFoundException;
import com.hms.model.Room;
import com.hms.model.Role;
import com.hms.repository.RoomRepository;
import com.hms.repository.RoleRepository;
import com.hms.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service // Indicates that this class is a service provider (contains business functionalities)
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;

    @Autowired
    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public RoomDto postRoom(RoomDto room) {
        if(roomRepository.existsById(room.getRoomId()))
            throw new DuplicateResourceException("Room", "room_id", Long.toString(room.getRoomId()));
        Room newRoom = mapToEntity(room);
        //Role role = roleRepository.findByName("ROLE_EMPLOYEE").get();
        //newRoom.setRole(Collections.singleton(role));
        //roomRepository.save(newRoom);
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
        if(!partialRoom.getAvailability().isEmpty())
            originalRoom.setAvailability(partialRoom.getAvailability());
        if(partialRoom.getCapacity() !=0)
            originalRoom.setCapacity(partialRoom.getCapacity());
        if(!partialRoom.getFeatures().isEmpty())
            originalRoom.setFeatures(partialRoom.getFeatures());
        if(partialRoom.getSize() !=0)
            originalRoom.setSize(partialRoom.getSize());
        if(partialRoom.getPrice() !=0)
            originalRoom.setPrice(partialRoom.getPrice());
        if(!partialRoom.getType().isEmpty())
            originalRoom.setType(partialRoom.getType());
        if(!partialRoom.getStatus().isEmpty())
            originalRoom.setStatus(partialRoom.getStatus());
        if(!partialRoom.getFacilities().isEmpty())
            originalRoom.setFacilities(partialRoom.getFacilities());
        if(!partialRoom.getDetails().isEmpty())
            originalRoom.setDetails(partialRoom.getDetails());

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
                .facilities(room.getFacilities())
                .features(room.getFeatures())
                .size(room.getSize())
                .status(room.getStatus())
                .type(room.getType())
                .price(room.getPrice())
                .capacity(room.getCapacity())
                .availability(room.getAvailability())
                .build();
    }

    private Room mapToEntity(RoomDto roomDto) {
        Room room = new Room();
        room.setDetails(room.getDetails());
        room.setFacilities(room.getFacilities());
        room.setFeatures(room.getFeatures());
        room.setSize(room.getSize());
        room.setStatus(room.getStatus());
        room.setType(room.getType());
        room.setPrice(room.getPrice());
        room.setCapacity(room.getCapacity());
        room.setAvailability(room.getAvailability());
        return room;
    }
}
