package com.hms.service;

import com.hms.dto.BillDto;

public interface BillService {
    BillDto postBill(BillDto employee);
    BillDto[] getBills(Integer pageNumber, Integer pageSize);
    BillDto replaceBill(BillDto employee);
    BillDto modifyBill(BillDto employee);
    void deleteBill(Long employee_id);
    BillDto getBillById(Long employee_id);
}
