package com.thong.feature.payroll;

import com.thong.feature.employee.EmployeeRepository;
import com.thong.feature.payroll.dto.GeneratePayrollRequest;
import com.thong.feature.payroll.dto.PayrollResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/payrolls")
@RequiredArgsConstructor
public class PayrollController {

    private final PayrollService payrollService;
    private final EmployeeRepository employeeRepository;

    // POST /api/v1/payrolls/generate
    // Body: { "month": "2024-06" }            → all active employees
    // Body: { "employeeId": 5, "month": "2024-06" } → one employee
    @PostMapping("/generate") // done !
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<PayrollResponse>> generatePayroll(
            @Valid @RequestBody GeneratePayrollRequest request) {
        return ResponseEntity.ok(payrollService.generatePayroll(request));
    }

    // GET /api/payrolls?month=2024-06 — admin views all (filter optional)
    @GetMapping
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')") // done !
    public ResponseEntity<List<PayrollResponse>> getAllPayrolls(
            @RequestParam(required = false) String month) {
        return ResponseEntity.ok(payrollService.getAllPayrolls(month));
    }

    // PATCH /api/payrolls/{id}/pay — PATCH because only status field changes
    @PatchMapping("/{id}/pay") // done !
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    public ResponseEntity<PayrollResponse> markAsPaid(@PathVariable Integer id) {
        return ResponseEntity.ok(payrollService.markAsPaid(id));
    }



    // Resolve JWT username → employee ID (used on every secured endpoint)
    private Integer resolveEmployeeId(Authentication authentication) {
        String email = authentication.getName();
        // getName() returns JWT subject (sub)
        log.info("Resolving employee id {}", email);
        return employeeRepository.findByUser_Email(email)
                .orElseThrow(() -> new RuntimeException("No employee profile found"))
                .getId();
    }
    // GET /api/payrolls/my — employee sees own pay slips only
    @GetMapping("/my") // done !
    @PreAuthorize("hasAnyAuthority('SCOPE_EMPLOYEE', 'SCOPE_ADMIN')")
    public ResponseEntity<List<PayrollResponse>> getMyPayrolls(
           Authentication authentication) {
        return ResponseEntity.ok(payrollService.getMyPayrolls(resolveEmployeeId(authentication)));
    }
}