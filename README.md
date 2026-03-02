# Gestor de Gastos Personales – Backend

API REST desarrollada con **Spring Boot** para la gestión de gastos personales asociados a usuarios.  
El proyecto está pensado como **backend puro**, con una arquitectura clara, sin sobre-ingeniería y siguiendo buenas prácticas de desarrollo profesional.

>  Proyecto en desarrollo – este repositorio refleja una implementación progresiva y bien estructurada.

---

## Tecnologías utilizadas

- **Java 17**
- **Spring Boot**
- **Spring Data JPA / Hibernate**
- **PostgreSQL** (H2 para desarrollo)
- **Lombok**
- **Swagger / OpenAPI**
- **Maven**

---

## Arquitectura

El proyecto sigue una **arquitectura en capas (layered architecture)**:

controller → service → repository → database

---

Separando claramente responsabilidades:

- `controller`: exposición de endpoints REST
- `service`: lógica de negocio
- `repository`: acceso a datos
- `entity`: modelo de dominio (JPA)
- `dto`: objetos de transferencia (request / response)
- `exception`: manejo global de errores
- `config`: configuraciones generales (OpenAPI, etc.)

---

## Objetivo del proyecto

Este proyecto tiene como objetivo:

- Practicar desarrollo backend con Spring Boot

- Aplicar buenas prácticas de arquitectura

- Mantener código claro y entendible

- Servir como proyecto de portfolio profesional
