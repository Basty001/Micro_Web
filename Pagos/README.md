# QualifyGym Pagos Microservice

Microservicio de gestión de pagos para la aplicación QualifyGym.

## Características

- ✅ Creación de pagos
- ✅ Gestión de estados de pago
- ✅ Consulta de pagos por usuario
- ✅ Consulta de pagos por orden
- ✅ API REST documentada con Swagger

## Requisitos Previos

- Java 21 o superior
- Maven 3.6+
- MySQL 8.0+
- IDE (IntelliJ IDEA, Eclipse, VS Code, etc.)

## Configuración

1. Crear la base de datos en MySQL:
```sql
CREATE DATABASE db_pagos;
```

2. Configurar las credenciales en `src/main/resources/application.properties`:
```properties
spring.datasource.username=root
spring.datasource.password=tu_password
```

3. El microservicio se ejecutará en el puerto **8084** por defecto.

## Instalación y Ejecución

### Usando Maven Wrapper (recomendado)

```bash
# Windows
mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```

### Usando Maven instalado

```bash
mvn spring-boot:run
```

## Endpoints Principales

- `GET /api/v1/pagos` - Obtener todos los pagos
- `GET /api/v1/pagos/{id}` - Obtener pago por ID
- `GET /api/v1/pagos/usuario/{usuarioId}` - Obtener pagos por usuario
- `GET /api/v1/pagos/orden/{ordenId}` - Obtener pagos por orden
- `GET /api/v1/pagos/estado/{estado}` - Obtener pagos por estado
- `POST /api/v1/pagos` - Crear pago
- `PUT /api/v1/pagos/{id}/estado` - Actualizar estado de pago
- `DELETE /api/v1/pagos/{id}` - Eliminar pago

## Documentación API

Una vez iniciado el microservicio:
- Swagger UI: `http://localhost:8084/swagger-ui.html`
- API Docs: `http://localhost:8084/v3/api-docs`

## Tecnologías

- **Spring Boot** 3.5.7
- **Spring Security** - Autenticación y autorización
- **Spring Data JPA** - Persistencia de datos
- **MySQL** - Base de datos
- **Lombok** - Reducción de código boilerplate
- **JUnit 5** - Testing
- **Mockito** - Mocking para tests
- **Swagger/OpenAPI** - Documentación de API

