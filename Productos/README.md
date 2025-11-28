# QualifyGym Productos Microservice

Microservicio de gestión de productos para la aplicación QualifyGym.

## Características

- ✅ CRUD completo de productos
- ✅ Búsqueda por categoría
- ✅ Búsqueda por nombre
- ✅ Gestión de stock
- ✅ API REST documentada con Swagger

## Requisitos Previos

- Java 21 o superior
- Maven 3.6+
- MySQL 8.0+
- IDE (IntelliJ IDEA, Eclipse, VS Code, etc.)

## Configuración

1. Crear la base de datos en MySQL:
```sql
CREATE DATABASE db_productos;
```

2. Configurar las credenciales en `src/main/resources/application.properties`:
```properties
spring.datasource.username=root
spring.datasource.password=tu_password
```

3. El microservicio se ejecutará en el puerto **8083** por defecto.

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

- `GET /api/v1/productos` - Obtener todos los productos
- `GET /api/v1/productos/{id}` - Obtener producto por ID
- `GET /api/v1/productos/categoria/{categoria}` - Obtener productos por categoría
- `GET /api/v1/productos/buscar?nombre={nombre}` - Buscar productos por nombre
- `POST /api/v1/productos` - Crear producto
- `PUT /api/v1/productos/{id}` - Actualizar producto
- `DELETE /api/v1/productos/{id}` - Eliminar producto
- `PUT /api/v1/productos/{id}/stock` - Actualizar stock

## Documentación API

Una vez iniciado el microservicio:
- Swagger UI: `http://localhost:8083/swagger-ui.html`
- API Docs: `http://localhost:8083/v3/api-docs`

## Tecnologías

- **Spring Boot** 3.5.7
- **Spring Security** - Autenticación y autorización
- **Spring Data JPA** - Persistencia de datos
- **MySQL** - Base de datos
- **Lombok** - Reducción de código boilerplate
- **JUnit 5** - Testing
- **Mockito** - Mocking para tests
- **Swagger/OpenAPI** - Documentación de API

