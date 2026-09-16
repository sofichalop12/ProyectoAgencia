# Registro de Cambios y Arquitectura - Agencia de Turismo Backend

Este documento mantiene la memoria técnica y el historial de evolución del sistema desde su migración de Java Swing a Spring Boot REST API.

---

## 🏛️ Arquitectura Actual del Sistema

- **Framework Principal**: Spring Boot 3.2.5 (Java 17)
- **Clase Principal**: `com.agencia.AgenciaBackendApplication`
- **Base de Datos**: H2 In-Memory Database (SQL Relacional)
- **OR/M y Persistencia**: Jakarta Persistence API (JPA) + Hibernate ORM (`InheritanceType.JOINED`)
- **Capa de Datos**: Spring Data JPA Repositories
- **Serialización**: Jackson (con `@JsonIgnore` / `@JsonIgnoreProperties` para evitar recursión circular)
- **Documentación de API**: SpringDoc OpenAPI 3 / Swagger UI (`/swagger-ui.html`)
- **Manejo de Errores**: ControllerAdvice centralizado (`GlobalExceptionHandler`)
- **Configuración Web**: Soporte CORS habilitado (`CorsConfig`)

---

## 📅 Historial de Versiones y Cambios

### [v1.5.0] - 2026-09-16 | Corrección de firmas, Main Class y Registro de Avance
#### ✨ Nuevas Características y Correcciones
- **Validación de Avance en Viajes**: Endpoint `PUT /api/viajes/{id}/avanzar?km=X` probado y funcional. Activa el viaje a `EN_CURSO` si está `PENDIENTE` y recalcula kilómetros dinámicamente.
- **Sincronización de Métodos**: Sincronización de las firmas del controlador (`ViajeController`) con las entidades de dominio (`Viaje.avanzarKm`) y servicios (`AgenciaService`).
- **Ajuste de Empaquetado Maven**: Configuración explícita de `com.agencia.AgenciaBackendApplication` en el `spring-boot-maven-plugin` de `pom.xml`.

---

### [v1.4.0] - 2026-09-16 | Configuración Global de CORS
#### ✨ Nuevas Características
- **CorsConfig (`WebMvcConfigurer`)**: Habilitación de peticiones cross-origin (`/api/**`) para integración con frontends modernos (React, Angular, Vue, etc.).

---

### [v1.3.0] - 2026-09-16 | Documentación Interactiva y Manejo Global de Excepciones
#### ✨ Nuevas Características
- **SpringDoc OpenAPI 3 / Swagger UI**: Consola interactiva accesible en `http://localhost:8080/swagger-ui.html`.
- **GlobalExceptionHandler (`@RestControllerAdvice`)**: Centralización de la gestión de excepciones (`DestinoYaExisteException`, `ValidacionException`).

---

### [v1.1.0] - 2026-09-16 | API REST Completa y Carga Inicial
#### ✨ Nuevas Características
- **Controladores REST**: Endpoints CRUD/GET para `Destino`, `Viaje`, `Transporte` y `Responsable`.
- **DataInitializer**: Precarga automática de datos de prueba en la base H2.

---

### [v1.0.0] - Migración Inicial del Modelo de Dominio
#### ✨ Características Iniciales
- Estructura base Spring Boot con Maven, mapeo JPA y repositorios Spring Data.