package com.inventorymanagementsystem.service.impl;

import com.inventorymanagementsystem.dto.InventoryDto;
import com.hms.exception.DuplicateResourceException;
import com.hms.exception.ResourceNotFoundException;
import com.inventorymanagementsystem.model.Inventory;
import com.inventorymanagementsystem.repository.InventoryRepository;
import com.inventorymanagementsystem.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service // Indicates that this class is a service provider (contains business functionalities)
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;

    @Autowired
    public InventoryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public InventoryDto postInventory(InventoryDto inventory) {
        if(inventoryRepository.existsById(inventory.getInventory_id()))
            throw new DuplicateResourceException("Inventory", "inventory_id", inventory.getInventory_id());
        return mapToDto(inventoryRepository.save(mapToEntity(inventory)));
    }

    @Override
    public InventoryDto[] getInventories(Integer pageNumber, Integer pageSize) {
        Object[] inventorysObjects = inventoryRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent().toArray();
        InventoryDto[] inventorysDtos = new InventoryDto[inventorysObjects.length];
        for(int i = 0; i < inventorysObjects.length; i++)
            if (inventorysObjects[i] instanceof Inventory)
                inventorysDtos[i] = mapToDto((Inventory) inventorysObjects[i]);
        if(inventorysDtos.length == 0)
            throw new ResourceNotFoundException("Inventory", "pageNumber and or pageSize", pageNumber + " and or " + pageSize);
        return inventorysDtos;
    }

    @Override
    public InventoryDto replaceInventory(InventoryDto inventory) {
        if(!inventoryRepository.existsById(inventory.getInventory_id()))
            throw new ResourceNotFoundException("Inventory", "inventory_id", String.valueOf(inventory.getInventory_id()));
        return mapToDto(inventoryRepository.save(mapToEntity(inventory)));
    }

    @Override
    public InventoryDto modifyInventory(InventoryDto partialInventory) {
        Inventory originalInventory = inventoryRepository.findById(partialInventory.getInventory_id()).orElseThrow(() -> new ResourceNotFoundException("Inventory", "inventory_id", String.valueOf(partialInventory.getInventory_id())));
        if(partialInventory.getEquipment_id() != 0)
            originalInventory.setEquipment_id(partialInventory.getEquipment_id());
        if(partialInventory.getBranch_id() != 0)
            originalInventory.setBranch_id(partialInventory.getBranch_id());
        if(partialInventory.getSerial_number()!= null && !partialInventory.getSerial_number().isEmpty())
            originalInventory.setSerial_number(partialInventory.getSerial_number());
        inventoryRepository.save(originalInventory);
        return mapToDto(originalInventory);
    }

    @Override
    public void deleteInventory(Integer inventory_id) {
        if(!inventoryRepository.existsById(inventory_id))
            throw new ResourceNotFoundException("Inventory", "inventory_id", String.valueOf(inventory_id));
        inventoryRepository.deleteById(inventory_id);
    }

    @Override
    public InventoryDto getInventoryById(Integer inventory_id) {
        return mapToDto(inventoryRepository.findById(inventory_id).orElseThrow(() -> new ResourceNotFoundException("Inventory", "inventory_id", String.valueOf(inventory_id))));
    }

    private InventoryDto mapToDto(Inventory inventory) {
        return InventoryDto.builder()
                .inventory_id(inventory.getInventory_id())
                .equipment_id(inventory.getEquipment_id())
                .branch_id(inventory.getBranch_id())
                .serial_number(inventory.getSerial_number())
                .build();
    }

    private Inventory mapToEntity(InventoryDto inventoryDto) {
        return Inventory.builder()
                .inventory_id(inventoryDto.getInventory_id())
                .equipment_id(inventoryDto.getEquipment_id())
                .branch_id(inventoryDto.getBranch_id())
                .serial_number(inventoryDto.getSerial_number())
                .build();
    }
}
