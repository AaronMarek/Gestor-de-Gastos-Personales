package com.example.expense_manager.repository;

import com.example.expense_manager.entity.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repositorio para operaciones con la entidad Expense
 */
@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    
    /**
     * Obtiene todos los gastos de un usuario (con paginación)
     */
    Page<Expense> findByUserId(Long userId, Pageable pageable);
    
    /**
     * Obtiene gastos de un usuario entre dos fechas
     */
    List<Expense> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Obtiene gastos por rango de fechas (para todos los usuarios)
     */
    List<Expense> findByDateBetween(LocalDate startDate, LocalDate endDate);
}