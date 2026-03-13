package com.example.expense_manager.controller;

import com.example.expense_manager.dto.ExpenseRequest;
import com.example.expense_manager.dto.ExpenseResponse;
import com.example.expense_manager.entity.User;
import com.example.expense_manager.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
@SecurityRequirement(name = "bearerAuth")
public class ExpenseController {
    
    private final ExpenseService expenseService;
    
    /**
     * POST /api/expenses - Crear un nuevo gasto
     */
    @PostMapping
    @Operation(summary = "Crear gasto", description = "Registra un nuevo gasto para un usuario")
    public ResponseEntity<ExpenseResponse> createExpense(
            @Valid @RequestBody ExpenseRequest request,
            @AuthenticationPrincipal User currentUser) {
        // El usuario solo puede crear gastos para sí mismo
        request.setUserId(currentUser.getId());
        ExpenseResponse response = expenseService.createExpense(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    /**
     * GET /api/expenses - Obtener todos los gastos (solo ADMIN)
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar todos los gastos (ADMIN)", description = "Obtiene todos los gastos con paginación (solo administradores)")
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
     * GET /api/expenses/my - Obtener MIS gastos (usuario autenticado)
     */
    @GetMapping("/my")
    @Operation(summary = "Mis gastos", description = "Obtiene todos los gastos del usuario autenticado")
    public ResponseEntity<Page<ExpenseResponse>> getMyExpenses(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "date"));
        Page<ExpenseResponse> expenses = expenseService.getExpensesByUserId(currentUser.getId(), pageable);
        return ResponseEntity.ok(expenses);
    }
    
    /**
     * GET /api/expenses/{id} - Obtener un gasto por ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @expenseService.isOwner(#id, authentication.principal.username)")
    @Operation(summary = "Obtener gasto", description = "Obtiene un gasto específico por su ID")
    public ResponseEntity<ExpenseResponse> getExpenseById(@PathVariable Long id) {
        ExpenseResponse expense = expenseService.getExpenseById(id);
        return ResponseEntity.ok(expense);
    }
    
    /**
     * GET /api/expenses/user/{userId} - Obtener gastos de un usuario (solo ADMIN)
     */
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Gastos por usuario (ADMIN)", description = "Obtiene todos los gastos de un usuario específico (solo administradores)")
    public ResponseEntity<Page<ExpenseResponse>> getExpensesByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "date"));
        Page<ExpenseResponse> expenses = expenseService.getExpensesByUserId(userId, pageable);
        return ResponseEntity.ok(expenses);
    }
    
    /**
     * GET /api/expenses/my/date-range - Mis gastos por rango de fechas
     */
    @GetMapping("/my/date-range")
    @Operation(summary = "Mis gastos por fechas", description = "Obtiene gastos del usuario autenticado dentro de un rango de fechas")
    public ResponseEntity<List<ExpenseResponse>> getMyExpensesByDateRange(
            @AuthenticationPrincipal User currentUser,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        List<ExpenseResponse> expenses = 
            expenseService.getExpensesByUserAndDateRange(currentUser.getId(), startDate, endDate);
        return ResponseEntity.ok(expenses);
    }
    
    /**
     * GET /api/expenses/date-range - Gastos por rango de fechas (solo ADMIN)
     */
    @GetMapping("/date-range")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Gastos por fechas (ADMIN)", description = "Obtiene gastos dentro de un rango de fechas (solo administradores)")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        List<ExpenseResponse> expenses = expenseService.getExpensesByDateRange(startDate, endDate);
        return ResponseEntity.ok(expenses);
    }
    
    /**
     * GET /api/expenses/user/{userId}/date-range - Gastos de usuario por fechas (solo ADMIN)
     */
    @GetMapping("/user/{userId}/date-range")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Gastos de usuario por fechas (ADMIN)", 
               description = "Obtiene gastos de un usuario dentro de un rango de fechas (solo administradores)")
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
    @PreAuthorize("hasRole('ADMIN') or @expenseService.isOwner(#id, authentication.principal.username)")
    @Operation(summary = "Actualizar gasto", description = "Actualiza los datos de un gasto existente")
    public ResponseEntity<ExpenseResponse> updateExpense(
            @PathVariable Long id,
            @Valid @RequestBody ExpenseRequest request,
            @AuthenticationPrincipal User currentUser) {
        
        // Si no es admin, forzar que el userId sea el del usuario autenticado
        if (!currentUser.getAuthorities().stream()
                .noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            request.setUserId(currentUser.getId());
        }
        
        ExpenseResponse updated = expenseService.updateExpense(id, request);
        return ResponseEntity.ok(updated);
    }
    
    /**
     * DELETE /api/expenses/{id} - Eliminar un gasto
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @expenseService.isOwner(#id, authentication.principal.username)")
    @Operation(summary = "Eliminar gasto", description = "Elimina un gasto del sistema")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }
}