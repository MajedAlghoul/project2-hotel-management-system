package com.hms.service.impl;

import com.hms.dto.RoomDto;
import com.hms.dto.RoomDto;
import com.hms.exception.DuplicateResourceException;
import com.hms.exception.NoContentException;
import com.hms.exception.ResourceNotFoundException;
import com.hms.model.Room;
import com.hms.model.Role;
import com.hms.model.Room;
import com.hms.repository.RoomRepository;
import com.hms.repository.RoleRepository;
import com.hms.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service // Indicates that this class is a service provider (contains business functionalities)
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;

    @Autowired
    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public RoomDto postRoom(RoomDto room) {
        //if(roomRepository.existsById(room.getRoomId()))
        //    throw new DuplicateResourceException("Room", "room_id", Long.toString(room.getRoomId()));
        Room newRoom = mapToEntity(room);
        //Role role = roleRepository.findByName("ROLE_EMPLOYEE").get();
        //newRoom.setRole(Collections.singleton(role));
        //roomRepository.save(newRoom);
        return mapToDto(roomRepository.save(newRoom));
    }

    @Override
    public List<RoomDto> getRooms() {
        List<Room> rooms = roomRepository.findAll();
        if (rooms.isEmpty()) {
            throw new NoContentException("No rooms registered yet");
        }
        return rooms.stream().map(this::mapToDto).collect(Collectors.toList());
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
                .RoomId(room.getId())
                .build();
    }

    private Room mapToEntity(RoomDto roomDto) {
        Room room = new Room();
        room.setDetails(roomDto.getDetails());
        room.setFacilities(roomDto.getFacilities());
        room.setFeatures(roomDto.getFeatures());
        room.setSize(roomDto.getSize());
        room.setStatus(roomDto.getStatus());
        room.setType(roomDto.getType());
        room.setPrice(roomDto.getPrice());
        room.setCapacity(roomDto.getCapacity());
        room.setAvailability(roomDto.getAvailability());
        return room;
    }
}
