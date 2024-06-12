package com.inventorymanagementsystem.service;

import com.inventorymanagementsystem.dto.EquipmentDto;

public interface EquipmentService {
    EquipmentDto postEquipment(EquipmentDto equipment);
    EquipmentDto[] getEquipments(Integer pageNumber, Integer pageSize);
    EquipmentDto replaceEquipment(EquipmentDto equipment);
    EquipmentDto modifyEquipment(EquipmentDto equipment);
    void deleteEquipment(Integer equipment_id);
    EquipmentDto getEquipmentById(Integer equipment_id);
}
