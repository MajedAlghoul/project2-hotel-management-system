package com.hms.service.impl;

import com.hms.dto.BillDto;
import com.hms.dto.BillDto;
import com.hms.exception.DuplicateResourceException;
import com.hms.exception.NoContentException;
import com.hms.exception.ResourceNotFoundException;
import com.hms.model.Bill;
import com.hms.model.Role;
import com.hms.model.Bill;
import com.hms.repository.BillRepository;
import com.hms.repository.ReservationRepository;
import com.hms.repository.RoleRepository;
import com.hms.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service // Indicates that this class is a service provider (contains business functionalities)
public class BillServiceImpl implements BillService {
    private final BillRepository billRepository;
    private final ReservationRepository reservationRepository;

    @Autowired
    public BillServiceImpl(BillRepository billRepository, ReservationRepository reservationRepository) {
        this.billRepository = billRepository;
        this.reservationRepository = reservationRepository;
    }

    public BillDto postBill(BillDto bill) {
        //if(billRepository.existsById(bill.getId()))
        //    throw new DuplicateResourceException("Bill", "bill_id", String.valueOf(bill.getId()));
        Bill newBill = mapToEntity(bill);
        return mapToDto(billRepository.save(newBill));
    }

    @Override
    public List<BillDto> getBills() {
        List<Bill> bills = billRepository.findAll();
        if (bills.isEmpty()) {
            throw new NoContentException("No bills registered yet");
        }
        return bills.stream().map(this::mapToDto).collect(Collectors.toList());
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
        if(partialBill.getReservation() !=null)
            originalBill.setReservation(reservationRepository.findById(partialBill.getReservation()).get() );
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
                .reservation(bill.getReservation().getId())
                .invoice_total(bill.getInvoice_total())
                .paid(bill.getPaid())
                .due(bill.getDue())
                .id(bill.getId())
                .build();
    }

    private Bill mapToEntity(BillDto billDto) {
        return Bill.builder()
                .reservation(reservationRepository.findById(billDto.getReservation()).get() )
                .invoice_total(billDto.getInvoice_total())
                .paid(billDto.getPaid())
                .due(billDto.getDue())
                .build();
    }
}
