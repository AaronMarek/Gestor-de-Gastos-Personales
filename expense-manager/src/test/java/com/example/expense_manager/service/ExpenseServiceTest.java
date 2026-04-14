package com.example.expense_manager.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de ExpenseService")
class ExpenseServiceTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Debe crear gasto correctamente")
    void shouldCreateExpense() {
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando usuario no existe")
    void shouldThrowExceptionWhenUserNotFound() {
    }

    @Test
    @DisplayName("Debe obtener gasto por ID")
    void shouldGetExpenseById() {
    }

    @Test
    @DisplayName("Debe eliminar gasto correctamente")
    void shouldDeleteExpense() {
    }

    @Test
    @DisplayName("Debe verificar ownership correctamente")
    void shouldVerifyOwnership() {
    }
}
