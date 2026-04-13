package com.example.expense_manager.repository;

import com.example.expense_manager.entity.Expense;
import com.example.expense_manager.entity.User;
import com.example.expense_manager.enums.ExpenseCategory;
import com.example.expense_manager.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests para ExpenseRepository
 */
@DataJpaTest
@DisplayName("Tests de ExpenseRepository")
class ExpenseRepositoryTest {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User testUser;
    private Expense testExpense;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setName("Test User");
        testUser.setPassword("password");
        testUser.setRole(UserRole.ROLE_USER);
        testUser = entityManager.persistAndFlush(testUser);

        testExpense = new Expense();
        testExpense.setAmount(new BigDecimal("50.00"));
        testExpense.setCategory(ExpenseCategory.COMIDA);
        testExpense.setDate(LocalDate.of(2024, 1, 15));
        testExpense.setDescription("Almuerzo");
        testExpense.setUser(testUser);
    }

    @Test
    @DisplayName("Debe guardar un gasto correctamente")
    void shouldSaveExpense() {
        Expense saved = expenseRepository.save(testExpense);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getAmount()).isEqualByComparingTo("50.00");
        assertThat(saved.getCategory()).isEqualTo(ExpenseCategory.COMIDA);
        assertThat(saved.getUser().getId()).isEqualTo(testUser.getId());
    }

    @Test
    @DisplayName("Debe encontrar gastos por usuario con paginación")
    void shouldFindExpensesByUserId() {
        entityManager.persistAndFlush(testExpense);

        Expense expense2 = new Expense();
        expense2.setAmount(new BigDecimal("30.00"));
        expense2.setCategory(ExpenseCategory.TRANSPORTE);
        expense2.setDate(LocalDate.of(2024, 1, 16));
        expense2.setDescription("Taxi");
        expense2.setUser(testUser);
        entityManager.persistAndFlush(expense2);

        Page<Expense> expenses = expenseRepository.findByUserId(
                testUser.getId(),
                PageRequest.of(0, 10)
        );

        assertThat(expenses.getContent()).hasSize(2);
        assertThat(expenses.getTotalElements()).isEqualTo(2);
    }

    @Test
    @DisplayName("Debe encontrar gastos por rango de fechas")
    void shouldFindExpensesByDateRange() {
        entityManager.persistAndFlush(testExpense);

        Expense expense2 = new Expense();
        expense2.setAmount(new BigDecimal("25.00"));
        expense2.setCategory(ExpenseCategory.OCIO);
        expense2.setDate(LocalDate.of(2024, 1, 20));
        expense2.setDescription("Cine");
        expense2.setUser(testUser);
        entityManager.persistAndFlush(expense2);

        Expense expense3 = new Expense();
        expense3.setAmount(new BigDecimal("15.00"));
        expense3.setCategory(ExpenseCategory.COMIDA);
        expense3.setDate(LocalDate.of(2024, 2, 5));
        expense3.setDescription("Café");
        expense3.setUser(testUser);
        entityManager.persistAndFlush(expense3);

        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 31);

        List<Expense> expenses = expenseRepository.findByDateBetween(startDate, endDate);

        assertThat(expenses).hasSize(2);
        assertThat(expenses).extracting(Expense::getDescription)
                .containsExactlyInAnyOrder("Almuerzo", "Cine");
    }

    @Test
    @DisplayName("Debe encontrar gastos de usuario por rango de fechas")
    void shouldFindExpensesByUserAndDateRange() {
        User otherUser = new User();
        otherUser.setEmail("other@example.com");
        otherUser.setName("Other User");
        otherUser.setPassword("password");
        otherUser.setRole(UserRole.ROLE_USER);
        otherUser = entityManager.persistAndFlush(otherUser);

        entityManager.persistAndFlush(testExpense);

        Expense otherExpense = new Expense();
        otherExpense.setAmount(new BigDecimal("100.00"));
        otherExpense.setCategory(ExpenseCategory.COMIDA);
        otherExpense.setDate(LocalDate.of(2024, 1, 15));
        otherExpense.setDescription("Restaurante");
        otherExpense.setUser(otherUser);
        entityManager.persistAndFlush(otherExpense);

        List<Expense> expenses = expenseRepository.findByUserIdAndDateBetween(
                testUser.getId(),
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 31)
        );

        assertThat(expenses).hasSize(1);
        assertThat(expenses.get(0).getUser().getId()).isEqualTo(testUser.getId());
    }

    @Test
    @DisplayName("Debe eliminar gastos al eliminar usuario (CASCADE)")
    void shouldDeleteExpensesWhenUserIsDeleted() {
        Expense saved = entityManager.persistAndFlush(testExpense);
        Long expenseId = saved.getId();
        Long userId = testUser.getId();

        entityManager.remove(testUser);
        entityManager.flush();

        assertThat(expenseRepository.findById(expenseId)).isEmpty();
    }
}