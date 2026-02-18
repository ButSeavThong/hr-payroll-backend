package com.thong.feature.payroll;

import com.thong.domain.Employee;
import com.thong.domain.Payroll;
import com.thong.feature.attendance.AttendanceRepository;
import com.thong.feature.employee.EmployeeRepository;
import com.thong.feature.payroll.dto.GeneratePayrollRequest;
import com.thong.feature.payroll.dto.PayrollResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PayrollServiceImpl implements PayrollService {

    private final PayrollRepository payrollRepository;
    private final EmployeeRepository employeeRepository;
    private final AttendanceRepository attendanceRepository;
    private final PayrollMapper payrollMapper;

    private static final BigDecimal TAX_RATE             = new BigDecimal("0.10");
    private static final BigDecimal STANDARD_MONTHLY_HOURS = new BigDecimal("160");
    private static final BigDecimal OVERTIME_MULTIPLIER  = new BigDecimal("1.5");

    @Override
    @Transactional
    public List<PayrollResponse> generatePayroll(GeneratePayrollRequest request) {
        List<Employee> employees;

        if (request.employeeId() != null) {
            // Generate for one specific employee
            employees = List.of(
                employeeRepository.findById(request.employeeId())
                    .orElseThrow(() -> new RuntimeException("Employee not found"))
            );
        } else {
            // Generate for ALL active employees
            employees = employeeRepository.findAll()
                .stream()
                .filter(e -> Boolean.TRUE.equals(e.getIsActive()))
                .toList();
        }

        List<PayrollResponse> results = new ArrayList<>();

        for (Employee emp : employees) {
            // Skip if payroll already generated for this employee+month
            if (payrollRepository.existsByEmployeeIdAndMonth(emp.getId(), request.month())) {
                continue;
            }
            Payroll payroll = calculatePayroll(emp, request.month());
            results.add(payrollMapper.toResponse(payrollRepository.save(payroll)));
        }

        return results;
    }

    /**
     * Core payroll calculation:
     *
     * 1. hourlyRate   = baseSalary / 160
     * 2. overtimePay  = overtimeHours × hourlyRate × 1.5
     * 3. gross        = baseSalary + overtimePay
     * 4. tax          = gross × 10%
     * 5. netSalary    = gross - tax
     *
     * All BigDecimal operations use HALF_UP rounding to 2 decimal places.
     */
    private Payroll calculatePayroll(Employee employee, String month) {
        YearMonth yearMonth = YearMonth.parse(month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate   = yearMonth.atEndOfMonth();

        var attendances = attendanceRepository
            .findByEmployeeIdAndDateBetween(employee.getId(), startDate, endDate);

        double totalOvertimeHours = attendances.stream()
            .mapToDouble(a -> a.getOvertimeHours() != null ? a.getOvertimeHours() : 0.0)
            .sum();

        BigDecimal baseSalary = employee.getBaseSalary();

        BigDecimal hourlyRate = baseSalary
            .divide(STANDARD_MONTHLY_HOURS, 4, RoundingMode.HALF_UP);

        BigDecimal overtimePay = hourlyRate
            .multiply(BigDecimal.valueOf(totalOvertimeHours))
            .multiply(OVERTIME_MULTIPLIER)
            .setScale(2, RoundingMode.HALF_UP);

        BigDecimal gross = baseSalary.add(overtimePay);

        BigDecimal tax = gross
            .multiply(TAX_RATE)
            .setScale(2, RoundingMode.HALF_UP);

        BigDecimal netSalary = gross
            .subtract(tax)
            .setScale(2, RoundingMode.HALF_UP);

        return Payroll.builder()
            .employee(employee)
            .month(month)
            .baseSalary(baseSalary)
            .overtimePay(overtimePay)
            .tax(tax)
            .netSalary(netSalary)
            .status("GENERATED")
            .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PayrollResponse> getAllPayrolls(String month) {
        List<Payroll> payrolls = (month != null && !month.isBlank())
            ? payrollRepository.findByMonth(month)
            : payrollRepository.findAll();
        return payrolls.stream().map(payrollMapper::toResponse).toList();
    }

    @Override
    @Transactional
    public PayrollResponse markAsPaid(Integer payrollId) {
        var payroll = payrollRepository.findById(payrollId)
            .orElseThrow(() -> new RuntimeException("Payroll not found"));

        if ("PAID".equals(payroll.getStatus())) {
            throw new IllegalStateException("Payroll is already marked as PAID");
        }

        payroll.setStatus("PAID");
        return payrollMapper.toResponse(payrollRepository.save(payroll));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PayrollResponse> getMyPayrolls(Integer employeeId) {
        return payrollRepository.findByEmployeeId(employeeId)
            .stream()
            .map(payrollMapper::toResponse)
            .toList();
    }
}