package com.example.expense_manager.dto;

import com.example.expense_manager.enums.ExpenseCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO para enviar datos de gasto al cliente
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseResponse {
    private Long id;
    private BigDecimal amount;
    private ExpenseCategory category;
    private LocalDate date;
    private String description;
    private LocalDateTime createdAt;
    private Long userId;
    private String userName;
}