package com.example.expense_manager.entity;

import com.example.expense_manager.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

/**
 * Entidad que representa un Usuario en el sistema
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    // Un usuario puede tener muchos gastos
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Expense> expenses = new ArrayList<>();
    
    // Se ejecuta antes de guardar en la BD
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        // Si no se especifica un rol, asignar ROLE_USER por defecto
        if (role == null) {
            role = UserRole.ROLE_USER;
        }
    }

       
    // ===== Implementación de UserDetails =====
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Retorna los roles/permisos del usuario
        return List.of(new SimpleGrantedAuthority(role.name()));
    }
    
    @Override
    public String getUsername() {
        // Spring Security usa "username", pero nosotros usamos email
        return email;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    @Override
    public boolean isEnabled() {
        return true;
    }

}