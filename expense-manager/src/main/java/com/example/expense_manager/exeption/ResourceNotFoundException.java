package com.example.expense_manager.exeption;

/**
 * Excepción personalizada cuando no se encuentra un recurso
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}