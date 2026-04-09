package com.example.expense_manager.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@DisplayName("Tests de UserRepository")
class UserRepositoryTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Debe guardar un usuario correctamente")
    void shouldSaveUser() {
    }

    @Test
    @DisplayName("Debe encontrar usuario por email")
    void shouldFindUserByEmail() {
    }

    @Test
    @DisplayName("No debe encontrar usuario con email inexistente")
    void shouldNotFindUserWithNonExistentEmail() {
    }

    @Test
    @DisplayName("Debe verificar si existe usuario por email")
    void shouldCheckIfUserExistsByEmail() {
    }

    @Test
    @DisplayName("Email debe ser único")
    void shouldEnforceUniqueEmail() {
    }

    @Test
    @DisplayName("Debe eliminar usuario correctamente")
    void shouldDeleteUser() {
    }
}