package com.example.expense_manager.repository;

import com.example.expense_manager.entity.User;
import com.example.expense_manager.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests para UserRepository
 */
@DataJpaTest
@DisplayName("Tests de UserRepository")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setName("Test User");
        testUser.setPassword("encodedPassword");
        testUser.setRole(UserRole.ROLE_USER);
    }

    @Test
    @DisplayName("Debe guardar un usuario correctamente")
    void shouldSaveUser() {
        User savedUser = userRepository.save(testUser);

        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo("test@example.com");
        assertThat(savedUser.getRole()).isEqualTo(UserRole.ROLE_USER);
        assertThat(savedUser.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Debe encontrar usuario por email")
    void shouldFindUserByEmail() {
        entityManager.persistAndFlush(testUser);

        Optional<User> found = userRepository.findByEmail("test@example.com");

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Test User");
    }

    @Test
    @DisplayName("No debe encontrar usuario con email inexistente")
    void shouldNotFindUserWithNonExistentEmail() {
        Optional<User> found = userRepository.findByEmail("noexiste@example.com");

        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Debe verificar si existe usuario por email")
    void shouldCheckIfUserExistsByEmail() {
        entityManager.persistAndFlush(testUser);

        assertThat(userRepository.existsByEmail("test@example.com")).isTrue();
        assertThat(userRepository.existsByEmail("noexiste@example.com")).isFalse();
    }

    @Test
    @DisplayName("Email debe ser único")
    void shouldEnforceUniqueEmail() {
        entityManager.persistAndFlush(testUser);

        User duplicateUser = new User();
        duplicateUser.setEmail("test@example.com");
        duplicateUser.setName("Otro Usuario");
        duplicateUser.setPassword("password");
        duplicateUser.setRole(UserRole.ROLE_USER);

        assertThat(userRepository.existsByEmail("test@example.com")).isTrue();
    }

    @Test
    @DisplayName("Debe eliminar usuario correctamente")
    void shouldDeleteUser() {
        User saved = entityManager.persistAndFlush(testUser);
        Long userId = saved.getId();

        userRepository.deleteById(userId);
        entityManager.flush();

        assertThat(userRepository.findById(userId)).isEmpty();
    }
}