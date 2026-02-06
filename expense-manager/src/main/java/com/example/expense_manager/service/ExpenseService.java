package com.example.expense_manager.service;

import com.example.expense_manager.dto.ExpenseRequest;
import com.example.expense_manager.dto.ExpenseResponse;
import com.example.expense_manager.entity.Expense;
import com.example.expense_manager.entity.User;
import com.example.expense_manager.exeption.ResourceNotFoundException;
import com.example.expense_manager.repository.ExpenseRepository;
import com.example.expense_manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio con la lógica de negocio para Gastos
 */
@Service
@RequiredArgsConstructor
public class ExpenseService {
    
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    
    /**
     * Crea un nuevo gasto
     */
    @Transactional
    public ExpenseResponse createExpense(ExpenseRequest request) {
        User user = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + request.getUserId()));
        
        Expense expense = new Expense();
        expense.setAmount(request.getAmount());
        expense.setCategory(request.getCategory());
        expense.setDate(request.getDate());
        expense.setDescription(request.getDescription());
        expense.setUser(user);
        
        Expense savedExpense = expenseRepository.save(expense);
        return convertToDto(savedExpense);
    }
    
    /**
     * Obtiene todos los gastos (con paginación)
     */
    @Transactional(readOnly = true)
    public Page<ExpenseResponse> getAllExpenses(Pageable pageable) {
        return expenseRepository.findAll(pageable)
            .map(this::convertToDto);
    }
    
    /**
     * Obtiene un gasto por ID
     */
    @Transactional(readOnly = true)
    public ExpenseResponse getExpenseById(Long id) {
        Expense expense = expenseRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Gasto no encontrado con ID: " + id));
        return convertToDto(expense);
    }
    
    /**
     * Obtiene todos los gastos de un usuario específico (con paginación)
     */
    @Transactional(readOnly = true)
    public Page<ExpenseResponse> getExpensesByUserId(Long userId, Pageable pageable) {
        // Verificar que el usuario existe
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Usuario no encontrado con ID: " + userId);
        }
        
        return expenseRepository.findByUserId(userId, pageable)
            .map(this::convertToDto);
    }
    
    /**
     * Obtiene gastos por rango de fechas
     */
    @Transactional(readOnly = true)
    public List<ExpenseResponse> getExpensesByDateRange(LocalDate startDate, LocalDate endDate) {
        return expenseRepository.findByDateBetween(startDate, endDate)
            .stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }
    
    /**
     * Obtiene gastos de un usuario por rango de fechas
     */
    @Transactional(readOnly = true)
    public List<ExpenseResponse> getExpensesByUserAndDateRange(
            Long userId, LocalDate startDate, LocalDate endDate) {
        
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Usuario no encontrado con ID: " + userId);
        }
        
        return expenseRepository.findByUserIdAndDateBetween(userId, startDate, endDate)
            .stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }
    
    /**
     * Actualiza un gasto existente
     */
    @Transactional
    public ExpenseResponse updateExpense(Long id, ExpenseRequest request) {
        Expense expense = expenseRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Gasto no encontrado con ID: " + id));
        
        // Si se cambia el usuario, verificar que existe
        if (!expense.getUser().getId().equals(request.getUserId())) {
            User newUser = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + request.getUserId()));
            expense.setUser(newUser);
        }
        
        expense.setAmount(request.getAmount());
        expense.setCategory(request.getCategory());
        expense.setDate(request.getDate());
        expense.setDescription(request.getDescription());
        
        Expense updatedExpense = expenseRepository.save(expense);
        return convertToDto(updatedExpense);
    }
    
    /**
     * Elimina un gasto
     */
    @Transactional
    public void deleteExpense(Long id) {
        if (!expenseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Gasto no encontrado con ID: " + id);
        }
        expenseRepository.deleteById(id);
    }
    
    /**
     * Convierte Expense a ExpenseResponse
     */
    private ExpenseResponse convertToDto(Expense expense) {
        ExpenseResponse dto = new ExpenseResponse();
        dto.setId(expense.getId());
        dto.setAmount(expense.getAmount());
        dto.setCategory(expense.getCategory());
        dto.setDate(expense.getDate());
        dto.setDescription(expense.getDescription());
        dto.setCreatedAt(expense.getCreatedAt());
        dto.setUserId(expense.getUser().getId());
        dto.setUserName(expense.getUser().getName());
        return dto;
    }
}