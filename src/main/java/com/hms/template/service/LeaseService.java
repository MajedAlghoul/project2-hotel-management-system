package com.inventorymanagementsystem.service;

import com.inventorymanagementsystem.dto.LeaseDto;

public interface LeaseService {
    LeaseDto postLease(LeaseDto lease);
    LeaseDto[] getLeases(Integer pageNumber, Integer pageSize);
    LeaseDto replaceLease(LeaseDto lease);
    LeaseDto modifyLease(LeaseDto lease);
    void deleteLease(Integer lease_id);
    LeaseDto getLeaseById(Integer lease_id);
}
