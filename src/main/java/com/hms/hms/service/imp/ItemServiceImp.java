package com.jed9h3.inventorymanagementsystem.service.imp;

import com.jed9h3.inventorymanagementsystem.dto.ItemDto;
import com.jed9h3.inventorymanagementsystem.entity.Inventory;
import com.jed9h3.inventorymanagementsystem.entity.Item;
import com.jed9h3.inventorymanagementsystem.exception.BadRequestException;
import com.jed9h3.inventorymanagementsystem.exception.NoContentException;
import com.jed9h3.inventorymanagementsystem.exception.NotFoundException;
import com.jed9h3.inventorymanagementsystem.repository.ItemRepository;
import com.jed9h3.inventorymanagementsystem.service.ItemService;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImp implements ItemService {
    private final ItemRepository itemRepository;

    public ItemServiceImp(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public List<ItemDto> getAllItems() {
        List<Item> items = itemRepository.findAll();
        if (items.isEmpty()) {
            throw new NoContentException("No items registered yet");
        }
        return items.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public ItemDto createItem(ItemDto itemDto) {
        Item item = convertToEntity(itemDto);
        item.setInventory(new Inventory(item.getItemId(), item, 0L));
        Item savedItem = itemRepository.save(item);
        return convertToDto(savedItem);
    }

    @Override
    public void deleteAllItems() {
        if (itemRepository.count()==0){
            throw new NoContentException("No items registered yet to delete");
        }else{
            itemRepository.deleteAll();
        }
    }

    @Override
    public ItemDto getItemById(long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new NotFoundException("Item with id "+id+" doesnt exist"));
        return convertToDto(item);
    }

    @Override
    public Item getRawItemById(long id) {
        return itemRepository.findById(id).orElseThrow(() -> new NotFoundException("Item with id "+id+" doesnt exist"));
    }

    @Override
    public ItemDto updateItemById(ItemDto itemDto, long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new NotFoundException("Item with id "+id+" doesnt exist"));
        String nm=itemDto.getItemName();
        BigDecimal prc=itemDto.getPrice();
        if (nm==null || nm.isEmpty() || prc==null){
            throw new BadRequestException("Request missing required attributes");
        }
        item.setItemName(nm);
        item.setPrice(prc);
        Item savedItem = itemRepository.save(item);
        return convertToDto(savedItem);
    }

    @Override
    public ItemDto partiallyUpdateItemById(ItemDto itemDto, long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new NotFoundException("Item with id "+id+" doesnt exist"));
        String nm=itemDto.getItemName();
        BigDecimal prc=itemDto.getPrice();
        if (nm==null && prc==null){
            throw new NoContentException("No attributes to be updated");
        }
        if(nm!=null){
            item.setItemName(itemDto.getItemName());
        }
        if(prc!=null){
            item.setPrice(itemDto.getPrice());
        }
        Item savedItem = itemRepository.save(item);
        return convertToDto(savedItem);
    }

    @Override
    public void deleteItemById(long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new NotFoundException("Item with id "+id+" doesnt exist"));
        itemRepository.delete(item);
    }
    @Override
    public ItemDto convertToDto(Item item) {
        ItemDto result = new ItemDto();
        result.setItemId(item.getItemId());
        result.setItemName(item.getItemName());
        result.setPrice(item.getPrice());
        return result;
    }
    @Override
    public Item convertToEntity(ItemDto itemDto) {
        Item result = new Item();
        result.setItemName(itemDto.getItemName());
        result.setPrice(itemDto.getPrice());
        return result;
    }
}
