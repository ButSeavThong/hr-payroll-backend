package com.thong.feature.payroll;

import com.thong.feature.employee.EmployeeRepository;
import com.thong.feature.payroll.dto.GeneratePayrollRequest;
import com.thong.feature.payroll.dto.PayrollResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/payrolls")
@RequiredArgsConstructor
public class PayrollController {

    private final PayrollService payrollService;
    private final EmployeeRepository employeeRepository;

    // POST /api/payrolls/generate
    // Body: { "month": "2024-06" }            → all active employees
    // Body: { "employeeId": 5, "month": "2024-06" } → one employee
    @PostMapping("/generate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PayrollResponse>> generatePayroll(
            @Valid @RequestBody GeneratePayrollRequest request) {
        return ResponseEntity.ok(payrollService.generatePayroll(request));
    }

    // GET /api/payrolls?month=2024-06 — admin views all (filter optional)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PayrollResponse>> getAllPayrolls(
            @RequestParam(required = false) String month) {
        return ResponseEntity.ok(payrollService.getAllPayrolls(month));
    }

    // PATCH /api/payrolls/{id}/pay — PATCH because only status field changes
    @PatchMapping("/{id}/pay")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PayrollResponse> markAsPaid(@PathVariable Integer id) {
        return ResponseEntity.ok(payrollService.markAsPaid(id));
    }

    // GET /api/payrolls/my — employee sees own pay slips only
    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    public ResponseEntity<List<PayrollResponse>> getMyPayrolls(
            @AuthenticationPrincipal UserDetails userDetails) {
        Integer empId = employeeRepository
            .findByUserUsername(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("Employee not found"))
            .getId();
        return ResponseEntity.ok(payrollService.getMyPayrolls(empId));
    }
}