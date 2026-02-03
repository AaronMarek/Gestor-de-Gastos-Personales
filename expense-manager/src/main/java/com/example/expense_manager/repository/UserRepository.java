package com.example.expense_manager.repository;

import com.example.expense_manager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para operaciones con la entidad User
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Busca un usuario por su email
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Verifica si existe un usuario con ese email
     */
    boolean existsByEmail(String email);
}