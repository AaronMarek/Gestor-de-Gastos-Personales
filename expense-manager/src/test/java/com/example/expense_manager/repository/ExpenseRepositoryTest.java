package com.example.expense_manager.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@DisplayName("Tests de ExpenseRepository")
class ExpenseRepositoryTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Debe guardar un gasto correctamente")
    void shouldSaveExpense() {
    }

    @Test
    @DisplayName("Debe encontrar gastos por usuario con paginación")
    void shouldFindExpensesByUserId() {
    }

    @Test
    @DisplayName("Debe encontrar gastos por rango de fechas")
    void shouldFindExpensesByDateRange() {
    }

    @Test
    @DisplayName("Debe encontrar gastos de usuario por rango de fechas")
    void shouldFindExpensesByUserAndDateRange() {
    }

    @Test
    @DisplayName("Debe eliminar gastos al eliminar usuario (CASCADE)")
    void shouldDeleteExpensesWhenUserIsDeleted() {
    }
}