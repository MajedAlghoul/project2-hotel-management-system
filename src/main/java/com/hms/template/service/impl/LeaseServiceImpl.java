package com.inventorymanagementsystem.service.impl;

import com.inventorymanagementsystem.dto.LeaseDto;
import com.hms.exception.DuplicateResourceException;
import com.hms.exception.ResourceNotFoundException;
import com.inventorymanagementsystem.model.Lease;
import com.inventorymanagementsystem.repository.LeaseRepository;
import com.inventorymanagementsystem.service.LeaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service // Indicates that this class is a service provider (contains business functionalities)
public class LeaseServiceImpl implements LeaseService {
    private final LeaseRepository leaseRepository;

    @Autowired
    public LeaseServiceImpl(LeaseRepository leaseRepository) {
        this.leaseRepository = leaseRepository;
    }

    public LeaseDto postLease(LeaseDto lease) {
        if(leaseRepository.existsById(lease.getLease_id()))
            throw new DuplicateResourceException("Lease", "lease_id", lease.getLease_id());
        return mapToDto(leaseRepository.save(mapToEntity(lease)));
    }

    @Override
    public LeaseDto[] getLeases(Integer pageNumber, Integer pageSize) {
        Object[] leasesObjects = leaseRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent().toArray();
        LeaseDto[] leasesDtos = new LeaseDto[leasesObjects.length];
        for(int i = 0; i < leasesObjects.length; i++)
            if (leasesObjects[i] instanceof Lease)
                leasesDtos[i] = mapToDto((Lease) leasesObjects[i]);
        if(leasesDtos.length == 0)
            throw new ResourceNotFoundException("Lease", "pageNumber and or pageSize", pageNumber + " and or " + pageSize);
        return leasesDtos;
    }

    @Override
    public LeaseDto replaceLease(LeaseDto lease) {
        if(!leaseRepository.existsById(lease.getLease_id()))
            throw new ResourceNotFoundException("Lease", "lease_id", String.valueOf(lease.getLease_id()));
        return mapToDto(leaseRepository.save(mapToEntity(lease)));
    }

    @Override
    public LeaseDto modifyLease(LeaseDto partialLease) {
        Lease originalLease = leaseRepository.findById(partialLease.getLease_id()).orElseThrow(() -> new ResourceNotFoundException("Lease", "lease_id", String.valueOf(partialLease.getLease_id())));
        if(partialLease.getEmployee_id() != 0)
            originalLease.setEmployee_id(partialLease.getEmployee_id());
        if(partialLease.getInventory_id() != 0)
            originalLease.setInventory_id(partialLease.getInventory_id());
        if(partialLease.getStart()!= null && !partialLease.getStart().isEmpty())
            originalLease.setStartDate(partialLease.getStart());
        if(partialLease.getEnd()!= null && !partialLease.getEnd().isEmpty())
            originalLease.setEndDate(partialLease.getEnd());
        leaseRepository.save(originalLease);
        return mapToDto(originalLease);
    }

    @Override
    public void deleteLease(Integer lease_id) {
        if(!leaseRepository.existsById(lease_id))
            throw new ResourceNotFoundException("Lease", "lease_id", String.valueOf(lease_id));
        leaseRepository.deleteById(lease_id);
    }

    @Override
    public LeaseDto getLeaseById(Integer lease_id) {
        return mapToDto(leaseRepository.findById(lease_id).orElseThrow(() -> new ResourceNotFoundException("Lease", "lease_id", String.valueOf(lease_id))));
    }

    private LeaseDto mapToDto(Lease lease) {
        return LeaseDto.builder()
                .lease_id(lease.getLease_id())
                .employee_id(lease.getEmployee_id())
                .inventory_id(lease.getInventory_id())
                .start(lease.getStartDate())
                .end(lease.getEndDate())
                .build();
    }

    private Lease mapToEntity(LeaseDto leaseDto) {
        return Lease.builder()
                .lease_id(leaseDto.getLease_id())
                .employee_id(leaseDto.getEmployee_id())
                .inventory_id(leaseDto.getInventory_id())
                .startDate(leaseDto.getStart())
                .endDate(leaseDto.getEnd())
                .build();
    }
}
