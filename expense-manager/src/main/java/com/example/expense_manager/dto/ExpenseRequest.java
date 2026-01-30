package com.example.expense_manager.dto;

import com.example.expense_manager.enums.ExpenseCategory;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO para recibir datos al crear/actualizar gastos
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseRequest {
    
    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    private BigDecimal amount;
    
    @NotNull(message = "La categoría es obligatoria")
    private ExpenseCategory category;
    
    @NotNull(message = "La fecha es obligatoria")
    @PastOrPresent(message = "La fecha no puede ser futura")
    private LocalDate date;
    
    @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
    private String description;
    
    @NotNull(message = "El ID del usuario es obligatorio")
    private Long userId;
}