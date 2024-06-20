package com.hms.service.impl;

import com.hms.dto.BillDto;
import com.hms.exception.DuplicateResourceException;
import com.hms.exception.ResourceNotFoundException;
import com.hms.model.Bill;
import com.hms.model.Role;
import com.hms.repository.BillRepository;
import com.hms.repository.RoleRepository;
import com.hms.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service // Indicates that this class is a service provider (contains business functionalities)
public class BillServiceImpl implements BillService {
    private final BillRepository billRepository;

    @Autowired
    public BillServiceImpl(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    public BillDto postBill(BillDto bill) {
        if(billRepository.existsById(bill.getId()))
            throw new DuplicateResourceException("Bill", "bill_id", String.valueOf(bill.getId()));
        Bill newBill = mapToEntity(bill);
        return mapToDto(billRepository.save(newBill));
    }

    @Override
    public BillDto[] getBills(Integer pageNumber, Integer pageSize) {
        Object[] billsObjects = billRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent().toArray();
        BillDto[] billsDtos = new BillDto[billsObjects.length];
        for(int i = 0; i < billsObjects.length; i++)
            if (billsObjects[i] instanceof Bill)
                billsDtos[i] = mapToDto((Bill) billsObjects[i]);
        if(billsDtos.length == 0)
            throw new ResourceNotFoundException("Bill", "pageNumber and or pageSize", pageNumber + " and or " + pageSize);
        return billsDtos;
    }

    @Override
    public BillDto replaceBill(BillDto bill) {
        if(!billRepository.existsById(bill.getId()))
            throw new ResourceNotFoundException("Bill", "bill_id", String.valueOf(bill.getId()));
        return mapToDto(billRepository.save(mapToEntity(bill)));
    }

    @Override
    public BillDto modifyBill(BillDto partialBill) {
        Bill originalBill = billRepository.findById(partialBill.getId()).orElseThrow(() -> new ResourceNotFoundException("Bill", "email", String.valueOf(partialBill.getId())));
        if(!partialBill.getReservation().isEmpty())
            originalBill.setReservation(partialBill.getReservation());
        if(partialBill.getInvoice_total() != null)
            originalBill.setInvoice_total(partialBill.getInvoice_total());
        if(partialBill.getPaid() != null)
            originalBill.setPaid(partialBill.getPaid());
        if(partialBill.getDue() != null)
            originalBill.setDue(partialBill.getDue());
        billRepository.save(originalBill);
        return mapToDto(originalBill);
    }


    @Override
    public void deleteBill(Long bill_id) {
        if(!billRepository.existsById(bill_id))
            throw new ResourceNotFoundException("Bill", "bill_id", String.valueOf(bill_id));
        billRepository.deleteById(bill_id);
    }

    @Override
    public BillDto getBillById(Long bill_id) {
        return mapToDto(billRepository.findById(bill_id).orElseThrow(() -> new ResourceNotFoundException("Bill", "bill_id", String.valueOf(bill_id))));
    }

    private BillDto mapToDto(Bill bill) {
        return BillDto.builder()
                .reservation(bill.getReservation())
                .invoice_total(bill.getInvoice_total())
                .paid(bill.getPaid())
                .due(bill.getDue())
                .build();
    }

    private Bill mapToEntity(BillDto billDto) {
        return Bill.builder()
                .reservation(billDto.getReservation())
                .invoice_total(billDto.getInvoice_total())
                .paid(billDto.getPaid())
                .due(billDto.getDue())
                .build();
    }
}
