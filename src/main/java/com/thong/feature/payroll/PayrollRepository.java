package com.thong.feature.payroll;

import com.thong.domain.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PayrollRepository extends JpaRepository<Payroll, Integer> {

    boolean existsByEmployeeIdAndMonth(Integer employeeId, String month);

    List<Payroll> findByEmployeeId(Integer employeeId);

    List<Payroll> findByMonth(String month);

    Optional<Payroll> findByEmployeeIdAndMonth(Integer employeeId, String month);
}