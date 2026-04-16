package com.example.expense_manager.service;

import com.example.expense_manager.dto.UserRequest;
import com.example.expense_manager.dto.UserResponse;
import com.example.expense_manager.entity.User;
import com.example.expense_manager.enums.UserRole;
import com.example.expense_manager.exeption.ResourceNotFoundException;
import com.example.expense_manager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para UserService
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de UserService")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private UserRequest userRequestDto;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setName("Test User");
        testUser.setPassword("password");
        testUser.setRole(UserRole.ROLE_USER);

        userRequestDto = new UserRequest();
        userRequestDto.setEmail("new@example.com");
        userRequestDto.setName("New User");
    }

    @Test
    @DisplayName("Debe crear usuario correctamente")
    void shouldCreateUser() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        UserResponse result = userService.createUser(userRequestDto);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(testUser.getEmail());

        verify(userRepository).existsByEmail(anyString());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción al crear usuario con email duplicado")
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        assertThatThrownBy(() -> userService.createUser(userRequestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Ya existe un usuario con ese email");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Debe obtener todos los usuarios")
    void shouldGetAllUsers() {
        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("user2@example.com");
        user2.setName("User 2");
        user2.setRole(UserRole.ROLE_USER);

        when(userRepository.findAll()).thenReturn(Arrays.asList(testUser, user2));

        List<UserResponse> result = userService.getAllUsers();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getEmail()).isEqualTo("test@example.com");
        assertThat(result.get(1).getEmail()).isEqualTo("user2@example.com");
    }

    @Test
    @DisplayName("Debe obtener usuario por ID")
    void shouldGetUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        UserResponse result = userService.getUserById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando usuario no existe")
    void shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Usuario no encontrado con ID: 999");
    }

    @Test
    @DisplayName("Debe actualizar usuario correctamente")
    void shouldUpdateUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        UserRequest updateDto = new UserRequest();
        updateDto.setEmail("updated@example.com");
        updateDto.setName("Updated Name");

        UserResponse result = userService.updateUser(1L, updateDto);

        assertThat(result).isNotNull();
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Debe eliminar usuario correctamente")
    void shouldDeleteUser() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Debe verificar ownership correctamente")
    void shouldVerifyOwnership() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        assertThat(userService.isOwner(1L, "test@example.com")).isTrue();
        assertThat(userService.isOwner(1L, "other@example.com")).isFalse();
    }
}