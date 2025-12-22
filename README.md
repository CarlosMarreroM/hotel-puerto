<div align="justify">

<div align="center">

# <img src=../../../../../images/computer.png width="40"> Code, Learn & Practice  
**Persistencia Polyglot para `hotel-puerto` (H2 + MongoDB) con MapStruct + REST + Swagger + SOAP/CXF + JaCoCo**

<img src=https://wwcdn.weddingwire.com/vendor/65001_70000/67195/thumbnails/1200x1200_1367340572768-hotel-evening-new.jpg width="400">

</div>

---

## 1. Objetivo

Construir la base de un proyecto Spring Boot con **arquitectura en capas**, desacoplando:

- **Dominio** (punto de entrada para REST y SOAP)
- **Servicios** (uno por entidad)
- **Mappers** (MapStruct) para transformar **Dominio ↔ Persistencia**
- **Persistencia**:
  - **H2 + JPA** para datos estructurados
  - **MongoDB** para datos documentales (`GuestPreferences`)
- **Tests unitarios** y **cobertura** con **JaCoCo**

El caso especial es `Guest` (polyglot): parte en H2 (JPA) + parte en Mongo (NoSQL).

---

## 2. Arquitectura

<div align="center">
  <img src=images/arquitectura.png width="250">
</div>

Reglas:

- REST y SOAP **solo llaman al Dominio**.
- Los Servicios trabajan con **modelos de dominio** (no entities).
- Los Mappers se usan **en Servicios**.
- Persistencia se encapsula en repositorios.

---

## 3. Estructura de paquetes (conjunta)

Paquete base: `org.docencia.hotel`

```
org.docencia.hotel
├── HotelApplication
├── config
│   └── CxfConfig
├── web
│   ├── rest
│   │   └── GuestController
│   └── soap
│       ├── GuestSoapService
│       └── GuestSoapServiceImpl
├── domain
│   ├── api
│   │   ├── HotelDomain
│   │   ├── RoomDomain
│   │   ├── BookingDomain
│   │   └── GuestDomain
│   ├── impl
│   │   ├── HotelDomainImpl
│   │   ├── RoomDomainImpl
│   │   ├── BookingDomainImpl
│   │   └── GuestDomainImpl
│   ├── model
│   │   ├── Hotel
│   │   ├── Room
│   │   ├── Booking
│   │   ├── Guest
│   │   └── GuestPreferences
├── service
│   ├── api
│   │   ├── HotelService
│   │   ├── RoomService
│   │   ├── BookingService
│   │   └── GuestService
│   └── impl
│       ├── HotelServiceImpl
│       ├── RoomServiceImpl
│       ├── BookingServiceImpl
│       └── GuestServiceImpl
├── mapper
│   ├── jpa
│   │   ├── HotelMapper
│   │   ├── RoomMapper
│   │   ├── BookingMapper
│   │   └── GuestMapper
│   └── nosql
│       └── GuestPreferencesMapper
└── persistence
│   ├── jpa
│   │   ├── AbstractJpaRepository
│   │   ├── entity
│   │   │   ├── HotelEntity
│   │   │   ├── RoomEntity
│   │   │   ├── BookingEntity
│   │   │   └── GuestEntity
|   |── nosql
|   |   ├── document
|   |   │   └── GuestPreferencesDocument
│   └── repository
│       ├── jpa
│       │   ├── HotelRepository
│       │   ├── RoomRepository
│       │   ├── BookingRepository
│       │   └── GuestJpaRepository
│       └── nosql
│           └── GuestPreferencesRepository
```

---

## 4. Anotaciones REST

En `org.docencia.hotel.web.rest`:

- `@RestController`
- `@RequestMapping("/api/...")`
- `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`
- `@PathVariable`, `@RequestBody`, `@RequestParam`
- `@Valid`
- `ResponseEntity<T>`

Swagger/OpenAPI (springdoc):

- `@Tag`, `@Operation`, `@ApiResponse(s)`

---

## 5. Anotaciones SOAP (CXF / JAX-WS) y targetNamespace

Convención:

- `targetNamespace`: **`http://hotel.docencia.org/ws`**
- `serviceName`: `{Entidad}SoapService`
- `portName`: `{Entidad}SoapPort`

Anotaciones:

- `@WebService(name=..., targetNamespace=...)` (en el interface)
- `@WebService(endpointInterface=..., targetNamespace=..., serviceName=..., portName=...)` (en la implementación)
- `@WebMethod`, `@WebParam`, `@WebResult`

---

## 6. Consolas de BBDD (H2 y Mongo Express)

### 6.1 Consola H2 (incluida en Spring Boot)

En este proyecto se habilita:

- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:hotel_puerto`
- User: `sa`
- Password: *(vacío)*

> Nota: H2 en memoria se reinicia al parar la aplicación.

### 6.2 MongoDB + Mongo Express (Docker Compose)

Se incluye `docker-compose.yml` para levantar:

- MongoDB: `localhost:27017`
- Mongo Express (UI): `http://localhost:8081`

Credenciales (según compose):

- Usuario: `root`
- Password: `root`

---

## 7. Arranque del proyecto

### 7.1 Levantar MongoDB + Mongo Express

```bash
docker compose up -d
docker compose ps
```

### 7.2 Arrancar la app

```bash
mvn clean spring-boot:run
```

---

## 8. Endpoints útiles

### REST

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

### SOAP (CXF)

Se configura `cxf.path=/services`.

- Endpoint guest: `http://localhost:8080/services/guest`
- WSDL guest: `http://localhost:8080/services/guest?wsdl`

---

## 9. Tests y cobertura (JaCoCo)

Ejecutar tests:

```bash
mvn test
```

Informe de cobertura JaCoCo (Maven):

- `target/site/jacoco/index.html`

---

## 10. Librerías incluidas (pom.xml)

- Spring Web (REST)
- Spring Data JPA + H2
- Spring Data MongoDB
- Apache CXF (SOAP/JAX-WS)
- MapStruct (mappers)
- springdoc-openapi (Swagger UI)
- JUnit 5 / Mockito (tests)
- JaCoCo (cobertura)

Referencias:

- MapStruct: https://mapstruct.org/
- JaCoCo: https://www.jacoco.org/jacoco/
- Springdoc OpenAPI: https://springdoc.org/
- Apache CXF: https://cxf.apache.org/

---

## Licencia 📄

Apache 2.0

</div>
