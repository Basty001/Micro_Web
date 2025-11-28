# QualifyGym Ordenes Microservice

Microservicio de gestión de órdenes para la aplicación QualifyGym.

## Características

- ✅ Creación de órdenes con items
- ✅ Gestión de estados de órdenes
- ✅ Consulta de órdenes por usuario
- ✅ Consulta de items de órdenes
- ✅ API REST documentada con Swagger

## Requisitos Previos

- Java 21 o superior
- Maven 3.6+
- MySQL 8.0+
- IDE (IntelliJ IDEA, Eclipse, VS Code, etc.)

## Configuración

1. Crear la base de datos en MySQL:
```sql
CREATE DATABASE db_ordenes;
```

2. Configurar las credenciales en `src/main/resources/application.properties`:
```properties
spring.datasource.username=root
spring.datasource.password=tu_password
```

3. El microservicio se ejecutará en el puerto **8085** por defecto.

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

- `GET /api/v1/ordenes` - Obtener todas las órdenes
- `GET /api/v1/ordenes/{id}` - Obtener orden por ID
- `GET /api/v1/ordenes/usuario/{usuarioId}` - Obtener órdenes por usuario
- `GET /api/v1/ordenes/estado/{estado}` - Obtener órdenes por estado
- `GET /api/v1/ordenes/{id}/items` - Obtener items de una orden
- `POST /api/v1/ordenes` - Crear orden
- `PUT /api/v1/ordenes/{id}/estado` - Actualizar estado de orden
- `DELETE /api/v1/ordenes/{id}` - Eliminar orden

## Documentación API

Una vez iniciado el microservicio:
- Swagger UI: `http://localhost:8085/swagger-ui.html`
- API Docs: `http://localhost:8085/v3/api-docs`

## Tecnologías

- **Spring Boot** 3.5.7
- **Spring Security** - Autenticación y autorización
- **Spring Data JPA** - Persistencia de datos
- **MySQL** - Base de datos
- **Lombok** - Reducción de código boilerplate
- **JUnit 5** - Testing
- **Mockito** - Mocking para tests
- **Swagger/OpenAPI** - Documentación de API

