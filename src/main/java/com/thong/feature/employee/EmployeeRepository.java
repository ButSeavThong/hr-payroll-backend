package com.thong.feature.employee;

import com.thong.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    // Spring Data auto-generates: SELECT * FROM employees WHERE user_id = ?
    Optional<Employee> findByUserId(Integer userId);

    // Used by controller to resolve JWT username -> employee
    Optional<Employee> findByUserUsername(String username);

    // Prevents duplicate employee profiles for the same user account
    boolean existsByUserId(Integer userId);
}