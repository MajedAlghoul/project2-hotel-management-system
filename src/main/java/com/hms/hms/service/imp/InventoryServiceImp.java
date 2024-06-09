package com.jed9h3.inventorymanagementsystem.service.imp;

import com.jed9h3.inventorymanagementsystem.dto.InventoryDto;
import com.jed9h3.inventorymanagementsystem.entity.Inventory;
import com.jed9h3.inventorymanagementsystem.exception.BadRequestException;
import com.jed9h3.inventorymanagementsystem.exception.NoContentException;
import com.jed9h3.inventorymanagementsystem.exception.NotFoundException;
import com.jed9h3.inventorymanagementsystem.repository.InventoryRepository;
import com.jed9h3.inventorymanagementsystem.service.InventoryService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class InventoryServiceImp implements InventoryService {
    private final InventoryRepository inventoryRepository;

    public InventoryServiceImp(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public List<InventoryDto> getAllInventories() {
        List<Inventory> inventories = inventoryRepository.findAll();
        if (inventories.isEmpty()) {
            throw new NoContentException("No inventories registered yet");
        }
        return inventories.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public InventoryDto getInventoryById(long id) {
        Inventory inventory = inventoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Inventory with id "+id+" doesnt exist"));
        return convertToDto(inventory);
    }

    @Override
    public InventoryDto updateInventoryById(InventoryDto inventoryDto, long id) {
        Inventory inventory = inventoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Inventory with id "+id+" doesnt exist"));
        Long qua=inventoryDto.getAvailableQuantity();
        if (qua==null){
            throw new BadRequestException("Request missing required attributes");
        }
        inventory.setAvailableQuantity(qua);
        Inventory savedInventory = inventoryRepository.save(inventory);
        return convertToDto(savedInventory);
    }

    @Override
    public InventoryDto partiallyUpdateInventoryById(InventoryDto inventoryDto, long id) {
        Inventory inventory = inventoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Inventory with id "+id+" doesnt exist"));
        Long qua=inventoryDto.getAvailableQuantity();
        if (qua==null){
            throw new NoContentException("No attributes to be updated");
        }
        inventory.setAvailableQuantity(inventoryDto.getAvailableQuantity());
        Inventory savedInventory = inventoryRepository.save(inventory);
        return convertToDto(savedInventory);
    }

    private InventoryDto convertToDto(Inventory inventory) {
        InventoryDto result = new InventoryDto();
        result.setItemId(inventory.getItemId());
        result.setAvailableQuantity(inventory.getAvailableQuantity());
        return result;
    }
}
