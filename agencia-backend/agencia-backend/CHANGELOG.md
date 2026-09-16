# Registro de Cambios y Arquitectura - Agencia de Turismo Backend

Este documento mantiene la memoria técnica y el historial de evolución del sistema desde su migración de Java Swing a Spring Boot REST API.

---

## Arquitectura Actual del Sistema

- **Framework Principal**: Spring Boot 3.x (Java 17/21)
- **Base de Datos**: H2 In-Memory Database (SQL Relacional)
- **OR/M y Persistencia**: Jakarta Persistence API (JPA) + Hibernate ORM
- **Capa de Datos**: Spring Data JPA Repositories
- **Serialización**: Jackson (con `@JsonIgnore` / `@JsonIgnoreProperties` para prevenir recursión circular)

---

## Historial de Versiones y Cambios

### [v1.1.0] - 2026-09-16 | API REST Completa y Carga Inicial
#### Nuevas Características
- **Controladores REST**: Se agregaron todos los endpoints CRUD/GET para los recursos del sistema:
  - `DestinoController` (`/api/destinos`)
  - `ViajeController` (`/api/viajes`)
  - `TransporteController` (`/api/transportes`)
  - `ResponsableController` (`/api/responsables`)
- **Semilla de Datos (DataInitializer)**: Implementación de `CommandLineRunner` para cargar destinos, responsables, vehículos y viajes de prueba automáticamente en la base H2 al arrancar el servidor.

#### Correcciones y Ajustes Técnicos
- **Relaciones Bidireccionales**: Solucionado el error de bucle JSON infinito (*Infinite Recursion*) añadiendo `@JsonIgnore` en `Transporte.listaViajes` y `@JsonIgnoreProperties` en los atributos de `Viaje`.
- **Mapeo JPA**: Corregido el atributo `mappedBy = "transporteAsignado"` en `Transporte.java` para coincidir con la declaración en `Viaje.java`.
- **Normalización DTO/Entidad**: Estandarización de nombres de atributos (`nombre`, `cantKm`) en `Destino.java` para compatibilidad completa con deserialización JSON mediante Jackson.
- **Constructores Overloaded**: Ajuste de sobrecarga de constructores en `CortaDistancia` y `LargaDistancia` para mantener compatibilidad con `Agencia.java` y soporte JPA.

---

### [v1.0.0] - Migración Inicial del Modelo de Dominio
#### Características Iniciales
- Creación del proyecto Spring Boot con Maven.
- Mapeo de entidades JPA con herencia `InheritanceType.JOINED`:
  - `Viaje` (Clase base abstracta) -> `CortaDistancia`, `LargaDistancia`
  - `Transporte` (Clase base abstracta) -> `Auto`, `Combi`, `ColectivoSemiCama`, `ColectivoCocheCama`
  - `Destino` y `ResponsableABordo`
- Repositorios iniciales extendiendo `JpaRepository`.
- Implementación de `AgenciaService` para la lógica de negocio centralizada.

---

## Próximos Pasos Proyectados
- [ ] Configuración CORS para acceso desde frontends web/móviles.
- [ ] Documentación interactiva de la API mediante Swagger / OpenAPI.
- [ ] Manejo global de excepciones mediante `@ControllerAdvice`.

### 2026-09-16 | Manejo Global de Excepciones
#### Nuevas Características
- **GlobalExceptionHandler (`@RestControllerAdvice`)**: Captura centralizada de excepciones de negocio (`DestinoYaExisteException`, `ValidacionException`) y errores no controlados.
- **Estructura de Errores Consistente**: Respuestas JSON estandarizadas con `timestamp`, `status`, `error` y `mensaje` claro para el cliente.

### 2026-09-16 | Documentación Interactiva con Swagger / OpenAPI
#### Nuevas Características
- **SpringDoc OpenAPI 3**: Integración de interfaz Swagger UI para exploración y prueba interactiva de la API REST.
- **Ruta de Acceso**: `/swagger-ui.html` para la consola gráfica y `/v3/api-docs` para las especificaciones en formato JSON.