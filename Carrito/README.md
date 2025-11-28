# QualifyGym Carrito Microservice

Microservicio de gestión de carrito de compras para la aplicación QualifyGym.

## Características

- ✅ Gestión de items en el carrito
- ✅ Agregar productos al carrito
- ✅ Actualizar cantidades
- ✅ Eliminar items
- ✅ Vaciar carrito por usuario
- ✅ API REST documentada con Swagger

## Requisitos Previos

- Java 21 o superior
- Maven 3.6+
- MySQL 8.0+
- IDE (IntelliJ IDEA, Eclipse, VS Code, etc.)

## Configuración

1. Crear la base de datos en MySQL:
```sql
CREATE DATABASE db_carrito;
```

2. Configurar las credenciales en `src/main/resources/application.properties`:
```properties
spring.datasource.username=root
spring.datasource.password=tu_password
```

3. El microservicio se ejecutará en el puerto **8082** por defecto.

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

- `GET /api/v1/carrito/usuario/{usuarioId}` - Obtener carrito por usuario
- `POST /api/v1/carrito/agregar` - Agregar item al carrito
- `PUT /api/v1/carrito/item/{itemId}` - Actualizar cantidad
- `DELETE /api/v1/carrito/item/{itemId}` - Eliminar item
- `DELETE /api/v1/carrito/usuario/{usuarioId}` - Vaciar carrito
- `DELETE /api/v1/carrito/usuario/{usuarioId}/producto/{productoId}` - Eliminar item por producto

## Documentación API

Una vez iniciado el microservicio:
- Swagger UI: `http://localhost:8082/swagger-ui.html`
- API Docs: `http://localhost:8082/v3/api-docs`

## Tecnologías

- **Spring Boot** 3.5.7
- **Spring Security** - Autenticación y autorización
- **Spring Data JPA** - Persistencia de datos
- **MySQL** - Base de datos
- **Lombok** - Reducción de código boilerplate
- **JUnit 5** - Testing
- **Mockito** - Mocking para tests
- **Swagger/OpenAPI** - Documentación de API

