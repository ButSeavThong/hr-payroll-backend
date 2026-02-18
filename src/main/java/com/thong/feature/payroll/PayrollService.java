package com.thong.feature.payroll;

import com.thong.feature.payroll.dto.GeneratePayrollRequest;
import com.thong.feature.payroll.dto.PayrollResponse;
import java.util.List;

public interface PayrollService {
    List<PayrollResponse> generatePayroll(GeneratePayrollRequest request);
    List<PayrollResponse> getAllPayrolls(String month);
    PayrollResponse markAsPaid(Integer payrollId);
    List<PayrollResponse> getMyPayrolls(Integer employeeId);
}