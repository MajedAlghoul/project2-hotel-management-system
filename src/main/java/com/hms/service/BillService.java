package com.hms.service;

import com.hms.dto.BillDto;

import java.util.List;

public interface BillService {
    BillDto postBill(BillDto employee);
    List<BillDto> getBills();
    BillDto replaceBill(BillDto employee);
    BillDto modifyBill(BillDto employee);
    void deleteBill(Long employee_id);
    BillDto getBillById(Long employee_id);
}
