package com.example.expense_manager.service;

import com.example.expense_manager.dto.AuthResponse;
import com.example.expense_manager.dto.LoginRequest;
import com.example.expense_manager.dto.RegisterRequest;
import com.example.expense_manager.entity.User;
import com.example.expense_manager.enums.UserRole;
import com.example.expense_manager.repository.UserRepository;
import com.example.expense_manager.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio para manejar autenticación y registro
 */
@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    
    /**
     * Registra un nuevo usuario en el sistema
     */
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Verificar si el email ya existe
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
        
        // Crear nuevo usuario
        User user = new User();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.ROLE_USER);
        
        // Guardar en la base de datos
        User savedUser = userRepository.save(user);
        
        // Generar token JWT
        String token = tokenProvider.generateTokenFromEmail(savedUser.getEmail());
        
        // Retornar respuesta con token y datos del usuario
        return new AuthResponse(
            token,
            savedUser.getId(),
            savedUser.getEmail(),
            savedUser.getName(),
            savedUser.getRole().name()
        );
    }
    
    /**
     * Autentica un usuario y genera un token JWT
     */
    public AuthResponse login(LoginRequest request) {
        // Autenticar usuario
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            )
        );
        
        // Establecer la autenticación en el contexto de seguridad
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // Generar token JWT
        String token = tokenProvider.generateToken(authentication);
        
        // Obtener datos del usuario
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        
        // Retornar respuesta con token y datos del usuario
        return new AuthResponse(
            token,
            user.getId(),
            user.getEmail(),
            user.getName(),
            user.getRole().name()
        );
    }
}