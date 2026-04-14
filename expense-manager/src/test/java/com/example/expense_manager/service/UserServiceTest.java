package com.example.expense_manager.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de UserService")
class UserServiceTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Debe crear usuario correctamente")
    void shouldCreateUser() {
    }

    @Test
    @DisplayName("Debe lanzar excepción al crear usuario con email duplicado")
    void shouldThrowExceptionWhenEmailAlreadyExists() {
    }

    @Test
    @DisplayName("Debe obtener todos los usuarios")
    void shouldGetAllUsers() {
    }

    @Test
    @DisplayName("Debe obtener usuario por ID")
    void shouldGetUserById() {
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando usuario no existe")
    void shouldThrowExceptionWhenUserNotFound() {
    }

    @Test
    @DisplayName("Debe actualizar usuario correctamente")
    void shouldUpdateUser() {
    }

    @Test
    @DisplayName("Debe eliminar usuario correctamente")
    void shouldDeleteUser() {
    }

    @Test
    @DisplayName("Debe verificar ownership correctamente")
    void shouldVerifyOwnership() {
    }
}