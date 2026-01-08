# hotel-puerto

Proyecto **Spring Boot** con arquitectura en capas y persistencia **polyglot**:

- **H2 + JPA** para datos relacionales
- **MongoDB** para datos documentales
- **MapStruct** para mapeo entre capas
- **REST + Swagger (OpenAPI)**
- **SOAP (Apache CXF / JAX-WS)**
- **Tests unitarios + cobertura JaCoCo**

---

## Arquitectura

Reglas:

- REST y SOAP **solo llaman al Dominio**
- El Dominio **no depende** de JPA, Mongo ni frameworks
- Los Servicios trabajan con **modelos de dominio**
- Los Mappers (MapStruct) transforman **Dominio ↔ Persistencia**
- Persistencia encapsulada en repositorios

Persistencia polyglot:
- **H2 (JPA)** → Hotel, Room, Booking, Guest
- **MongoDB** → GuestPreferences

---

## Requisitos

- Java **17**
- Maven **3.9+**
- Docker + Docker Compose (para MongoDB)

---

## Arranque del proyecto

### 1) Levantar MongoDB (Docker)

Desde la raíz del proyecto:

    docker compose up -d
    docker compose ps

Servicios disponibles:

- MongoDB → `localhost:27017`
- Mongo Express (UI) → `http://localhost:8081`
  - Usuario: `root`
  - Password: `root`

Para detener los contenedores:

    docker compose down

> MongoDB persiste los datos en un volumen Docker (`mongo_data`).

---

### 2) Arrancar la aplicación Spring Boot

    mvn clean spring-boot:run

La aplicación se inicia en:

- `http://localhost:8080`

---

## Consolas y URLs útiles

### Swagger / OpenAPI (REST)

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

---

### Consola H2 (Base de datos relacional)

- H2 Console: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:file:./data/hotel_puerto`
- Usuario: `sa`
- Password: *(vacío)*

La base de datos H2 se guarda en local en la carpeta `./data/`  

---

### SOAP (Apache CXF)

Configuración:

- `cxf.path=/services`

Endpoints (ejemplo Guest):

- Endpoint: `http://localhost:8080/services/guest`
- WSDL: `http://localhost:8080/services/guest?wsdl`

---

## Tests y cobertura

Ejecutar tests:

    mvn clean test

Informe de cobertura JaCoCo:

- `target/site/jacoco/index.html`

---

## Manejo de errores (REST)

La API REST utiliza un `@RestControllerAdvice` (`GlobalExceptionHandler`) para devolver respuestas de error consistentes en formato JSON.

- `400 Bad Request` → datos inválidos (`IllegalArgumentException`, `NullPointerException` desde validaciones)
- `409 Conflict` → conflicto de estado (por ejemplo, intentar crear un recurso con un id ya existente)

Formato de error devuelto:

```json
{
  "timestamp": "2026-01-03T21:39:25.686052Z",
  "status": 400,
  "error": "Bad Request",
  "message": "hotel name must not be blank",
  "path": "/api/hotels"
}
```
---

## Base de datos SQLite de referencia

El enunciado incluye una base SQLite `hotel_puerto.db` como **referencia** del modelo de datos.

⚠️ Esta base **NO se usa directamente** en la app (la app usa H2 + Mongo).

---

## Tecnologías

- Spring Boot 3
- Spring Web (REST)
- Spring Data JPA + H2
- Spring Data MongoDB
- MapStruct
- Apache CXF (SOAP / JAX-WS)
- Swagger / OpenAPI (springdoc)
- JUnit 5 + Mockito
- JaCoCo

---

## Mejoras futuras

Este proyecto prioriza la claridad arquitectónica y el aprendizaje.  
Algunas mejoras previstas para una evolución futura son:

- **Añadir endpoint `PATCH`** para permitir actualizaciones parciales de recursos, manteniendo `PUT` como actualización completa.
- **Estandarizar respuestas `404 Not Found`** mediante una excepción dedicada (`NotFoundException`) y su mapeo en `@RestControllerAdvice`, evitando comprobaciones explícitas en los controllers.
- **Unificar la estrategia de validación** del componente `Guard`, evitando el uso de `NullPointerException` como validación de entrada y utilizando únicamente excepciones semánticas (`IllegalArgumentException`, etc.).
- Añadir soporte para filtrado de habitaciones por tipo sin necesidad de indicar hotel (`GET /rooms?type=...`).

---

## Licencia

Proyecto académico / educativo.
