package com.thong.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.YearMonth;

@Entity
@Table(name = "payrolls")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(nullable = false)
    private String month; // example: "2026-02"

    @Column(nullable = false)
    private BigDecimal baseSalary;

    private BigDecimal overtimePay;

    private BigDecimal tax;

    @Column(nullable = false)
    private BigDecimal netSalary;

    @Column(nullable = false)
    private String status; // GENERATED / PAID
}
