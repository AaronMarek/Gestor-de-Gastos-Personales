package com.example.expense_manager.service;

import com.example.expense_manager.dto.ExpenseRequest;
import com.example.expense_manager.dto.ExpenseResponse;
import com.example.expense_manager.entity.Expense;
import com.example.expense_manager.entity.User;
import com.example.expense_manager.enums.ExpenseCategory;
import com.example.expense_manager.enums.UserRole;
import com.example.expense_manager.exeption.ResourceNotFoundException;
import com.example.expense_manager.repository.ExpenseRepository;
import com.example.expense_manager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para ExpenseService
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de ExpenseService")
class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ExpenseService expenseService;

    private User testUser;
    private Expense testExpense;
    private ExpenseRequest requestDto;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setName("Test User");
        testUser.setRole(UserRole.ROLE_USER);

        testExpense = new Expense();
        testExpense.setId(1L);
        testExpense.setAmount(new BigDecimal("50.00"));
        testExpense.setCategory(ExpenseCategory.COMIDA);
        testExpense.setDate(LocalDate.of(2024, 1, 15));
        testExpense.setDescription("Almuerzo");
        testExpense.setUser(testUser);

        requestDto = new ExpenseRequest();
        requestDto.setAmount(new BigDecimal("50.00"));
        requestDto.setCategory(ExpenseCategory.COMIDA);
        requestDto.setDate(LocalDate.of(2024, 1, 15));
        requestDto.setDescription("Almuerzo");
        requestDto.setUserId(1L);
    }

    @Test
    @DisplayName("Debe crear gasto correctamente")
    void shouldCreateExpense() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(expenseRepository.save(any(Expense.class))).thenReturn(testExpense);

        ExpenseResponse result = expenseService.createExpense(requestDto);

        assertThat(result).isNotNull();
        assertThat(result.getAmount()).isEqualByComparingTo("50.00");
        assertThat(result.getCategory()).isEqualTo(ExpenseCategory.COMIDA);

        verify(userRepository).findById(1L);
        verify(expenseRepository).save(any(Expense.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando usuario no existe")
    void shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        requestDto.setUserId(999L);

        assertThatThrownBy(() -> expenseService.createExpense(requestDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Usuario no encontrado");
    }

    @Test
    @DisplayName("Debe obtener gasto por ID")
    void shouldGetExpenseById() {
        when(expenseRepository.findById(1L)).thenReturn(Optional.of(testExpense));

        ExpenseResponse result = expenseService.getExpenseById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getUserName()).isEqualTo("Test User");
    }

    @Test
    @DisplayName("Debe eliminar gasto correctamente")
    void shouldDeleteExpense() {
        when(expenseRepository.existsById(1L)).thenReturn(true);
        doNothing().when(expenseRepository).deleteById(1L);

        expenseService.deleteExpense(1L);

        verify(expenseRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Debe verificar ownership correctamente")
    void shouldVerifyOwnership() {
        when(expenseRepository.findById(1L)).thenReturn(Optional.of(testExpense));

        assertThat(expenseService.isOwner(1L, "test@example.com")).isTrue();
        assertThat(expenseService.isOwner(1L, "other@example.com")).isFalse();
    }
}