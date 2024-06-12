package com.inventorymanagementsystem.service.impl;

import com.inventorymanagementsystem.dto.EquipmentDto;
import com.hms.exception.DuplicateResourceException;
import com.hms.exception.ResourceNotFoundException;
import com.inventorymanagementsystem.model.Equipment;
import com.inventorymanagementsystem.repository.EquipmentRepository;
import com.inventorymanagementsystem.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service // Indicates that this class is a service provider (contains business functionalities)
public class EquipmentServiceImpl implements EquipmentService {
    private final EquipmentRepository equipmentRepository;

    @Autowired
    public EquipmentServiceImpl(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    public EquipmentDto postEquipment(EquipmentDto equipment) {
        if(equipmentRepository.existsById(equipment.getEquipment_id()))
            throw new DuplicateResourceException("Equipment", "equipment_id", equipment.getEquipment_id());
        return mapToDto(equipmentRepository.save(mapToEntity(equipment)));
    }

    @Override
    public EquipmentDto[] getEquipments(Integer pageNumber, Integer pageSize) {
        Object[] equipmentsObjects = equipmentRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent().toArray();
        EquipmentDto[] equipmentsDtos = new EquipmentDto[equipmentsObjects.length];
        for(int i = 0; i < equipmentsObjects.length; i++)
            if (equipmentsObjects[i] instanceof Equipment)
                equipmentsDtos[i] = mapToDto((Equipment) equipmentsObjects[i]);
        if(equipmentsDtos.length == 0)
            throw new ResourceNotFoundException("Equipment", "pageNumber and or pageSize", pageNumber + " and or " + pageSize);
        return equipmentsDtos;
    }

    @Override
    public EquipmentDto replaceEquipment(EquipmentDto equipment) {
        if(!equipmentRepository.existsById(equipment.getEquipment_id()))
            throw new ResourceNotFoundException("Equipment", "equipment_id", String.valueOf(equipment.getEquipment_id()));
        return mapToDto(equipmentRepository.save(mapToEntity(equipment)));
    }

    @Override
    public EquipmentDto modifyEquipment(EquipmentDto partialEquipment) {
        Equipment originalEquipment = equipmentRepository.findById(partialEquipment.getEquipment_id()).orElseThrow(() -> new ResourceNotFoundException("Equipment", "equipment_id", String.valueOf(partialEquipment.getEquipment_id())));
        if(partialEquipment.getName()!= null && !partialEquipment.getName().isEmpty())
            originalEquipment.setName(partialEquipment.getName());
        if(partialEquipment.getModel_number()!= null && !partialEquipment.getModel_number().isEmpty())
            originalEquipment.setModel_number(partialEquipment.getModel_number());
        if(partialEquipment.getCondition()!= null && !partialEquipment.getCondition().isEmpty())
            originalEquipment.setCondition(partialEquipment.getCondition());
        if(partialEquipment.getDescription()!= null && !partialEquipment.getDescription().isEmpty())
            originalEquipment.setDescription(partialEquipment.getDescription());
        equipmentRepository.save(originalEquipment);
        return mapToDto(originalEquipment);
    }

    @Override
    public void deleteEquipment(Integer equipment_id) {
        if(!equipmentRepository.existsById(equipment_id))
            throw new ResourceNotFoundException("Equipment", "equipment_id", String.valueOf(equipment_id));
        equipmentRepository.deleteById(equipment_id);
    }

    @Override
    public EquipmentDto getEquipmentById(Integer equipment_id) {
        return mapToDto(equipmentRepository.findById(equipment_id).orElseThrow(() -> new ResourceNotFoundException("Equipment", "equipment_id", String.valueOf(equipment_id))));
    }

    private EquipmentDto mapToDto(Equipment equipment) {
        return EquipmentDto.builder()
                .equipment_id(equipment.getEquipment_id())
                .name(equipment.getName())
                .model_number(equipment.getModel_number())
                .condition(equipment.getCondition())
                .description(equipment.getDescription())
                .build();
    }

    private Equipment mapToEntity(EquipmentDto equipmentDto) {
        return Equipment.builder()
                .equipment_id(equipmentDto.getEquipment_id())
                .name(equipmentDto.getName())
                .model_number(equipmentDto.getModel_number())
                .condition(equipmentDto.getCondition())
                .description(equipmentDto.getDescription())
                .build();
    }
}
