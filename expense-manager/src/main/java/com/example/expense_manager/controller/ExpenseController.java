package com.example.expense_manager.controller;

import com.example.expense_manager.dto.ExpenseRequest;
import com.example.expense_manager.dto.ExpenseResponse;
import com.example.expense_manager.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador REST para operaciones con Gastos
 */
@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
@Tag(name = "Gastos", description = "API para gestionar gastos personales")
public class ExpenseController {
    
    private final ExpenseService expenseService;
    
    /**
     * POST /api/expenses - Crear un nuevo gasto
     */
    @PostMapping
    @Operation(summary = "Crear gasto", description = "Registra un nuevo gasto para un usuario")
    public ResponseEntity<ExpenseResponse> createExpense(@Valid @RequestBody ExpenseRequest request) {
        ExpenseResponse response = expenseService.createExpense(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    /**
     * GET /api/expenses - Obtener todos los gastos (con paginación)
     */
    @GetMapping
    @Operation(summary = "Listar gastos", description = "Obtiene todos los gastos con paginación")
    public ResponseEntity<Page<ExpenseResponse>> getAllExpenses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {
        
        Sort.Direction sortDirection = direction.equalsIgnoreCase("ASC") ? 
            Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        
        Page<ExpenseResponse> expenses = expenseService.getAllExpenses(pageable);
        return ResponseEntity.ok(expenses);
    }
    
    /**
     * GET /api/expenses/{id} - Obtener un gasto por ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener gasto", description = "Obtiene un gasto específico por su ID")
    public ResponseEntity<ExpenseResponse> getExpenseById(@PathVariable Long id) {
        ExpenseResponse expense = expenseService.getExpenseById(id);
        return ResponseEntity.ok(expense);
    }
    
    /**
     * GET /api/expenses/user/{userId} - Obtener gastos de un usuario
     */
    @GetMapping("/user/{userId}")
    @Operation(summary = "Gastos por usuario", description = "Obtiene todos los gastos de un usuario específico")
    public ResponseEntity<Page<ExpenseResponse>> getExpensesByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "date"));
        Page<ExpenseResponse> expenses = expenseService.getExpensesByUserId(userId, pageable);
        return ResponseEntity.ok(expenses);
    }
    
    /**
     * GET /api/expenses/date-range - Obtener gastos por rango de fechas
     */
    @GetMapping("/date-range")
    @Operation(summary = "Gastos por fechas", description = "Obtiene gastos dentro de un rango de fechas")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        List<ExpenseResponse> expenses = expenseService.getExpensesByDateRange(startDate, endDate);
        return ResponseEntity.ok(expenses);
    }
    
    /**
     * GET /api/expenses/user/{userId}/date-range - Gastos de usuario por fechas
     */
    @GetMapping("/user/{userId}/date-range")
    @Operation(summary = "Gastos de usuario por fechas", 
               description = "Obtiene gastos de un usuario dentro de un rango de fechas")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByUserAndDateRange(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        List<ExpenseResponse> expenses = 
            expenseService.getExpensesByUserAndDateRange(userId, startDate, endDate);
        return ResponseEntity.ok(expenses);
    }
    
    /**
     * PUT /api/expenses/{id} - Actualizar un gasto
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar gasto", description = "Actualiza los datos de un gasto existente")
    public ResponseEntity<ExpenseResponse> updateExpense(
            @PathVariable Long id,
            @Valid @RequestBody ExpenseRequest request) {
        ExpenseResponse updated = expenseService.updateExpense(id, request);
        return ResponseEntity.ok(updated);
    }
    
    /**
     * DELETE /api/expenses/{id} - Eliminar un gasto
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar gasto", description = "Elimina un gasto del sistema")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }
}