package com.example.expense_manager.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta después del login/registro
 * Contiene el token JWT y los datos del usuario
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String email;
    private String name;
    private String role;
    
    public AuthResponse(String token, Long id, String email, String name, String role) {
        this.token = token;
        this.id = id;
        this.email = email;
        this.name = name;
        this.role = role;
    }
}