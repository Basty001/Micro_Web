# Documentación de API - QualifyGym Microservicios

**Versión:** 1.0  
**Fecha:** 01/12/2024

---

## Microservicio de Usuarios

**Base URL:** `http://localhost:8081`  
**Swagger UI:** `http://localhost:8081/swagger-ui.html`

| Método HTTP | Ruta del Endpoint | Descripción | Datos de entrada | Respuestas | API PÚBLICA/PRIVADA | Requiere Autenticación | Roles permitidos | Observaciones |
|-------------|-------------------|-------------|------------------|------------|---------------------|------------------------|------------------|----------------|
| GET | `/api/v1/usuario/users` | Lista todos los usuarios | N/A | 200: Éxito, 204: No hay usuarios, 500: Error servidor | Privada | Sí | Administrador | Solo datos básicos de usuarios |
| GET | `/api/v1/usuario/users/{id}` | Obtiene detalles de un usuario | N/A | 200: Éxito, 404: No encontrado, 500: Error servidor | Privada | Sí | Administrador | Información completa del usuario |
| GET | `/api/v1/usuario/users/email/{email}` | Obtiene usuario por email | N/A | 200: Éxito, 404: No encontrado, 500: Error servidor | Privada | Sí | Administrador | Búsqueda por email |
| POST | `/api/v1/usuario/users` | Crea un nuevo usuario | `{username, password, email, phone, rolId, address?}` | 201: Creado, 400: Datos inválidos, 401: No autorizado | Privada | Sí | Administrador | Requiere todos los campos obligatorios |
| PUT | `/api/v1/usuario/users/{id}` | Actualiza información de un usuario | `{username?, password?, email?, phone?, rolId?, address?}` | 200: Éxito, 400: Datos inválidos, 401: No autorizado | Privada | Sí | Administrador | Permite actualización parcial |
| DELETE | `/api/v1/usuario/users/{id}` | Elimina un usuario | N/A | 204: Éxito, 401: No autorizado, 500: Error servidor | Privada | Sí | Administrador | Eliminación permanente |
| POST | `/api/v1/usuario/login` | Inicia sesión y obtiene token JWT | `{email, password}` | 200: Éxito (retorna token), 400: Faltan campos, 401: Credenciales inválidas, 500: Error servidor | Pública | No | N/A | Retorna token JWT y datos del usuario |
| POST | `/api/v1/usuario/register` | Registro público de usuario | `{username, password, email, phone, address?}` | 201: Creado, 400: Datos inválidos o duplicados, 500: Error servidor | Pública | No | N/A | Asigna automáticamente rol "Usuario" |
| GET | `/api/v1/usuario/users/{id}/existe` | Verifica si un usuario existe | N/A | 200: Éxito (true/false), 500: Error servidor | Privada | Sí | Administrador | Retorna boolean |

---

## Microservicio de Productos

**Base URL:** `http://localhost:8083`  
**Swagger UI:** `http://localhost:8083/swagger-ui.html`

| Método HTTP | Ruta del Endpoint | Descripción | Datos de entrada | Respuestas | API PÚBLICA/PRIVADA | Requiere Autenticación | Roles permitidos | Observaciones |
|-------------|-------------------|-------------|------------------|------------|---------------------|------------------------|------------------|----------------|
| GET | `/api/v1/productos` | Lista todos los productos | N/A | 200: Éxito, 204: No hay productos, 500: Error servidor | Pública | No | N/A | Incluye todos los productos disponibles |
| GET | `/api/v1/productos/{id}` | Obtiene detalles de un producto | N/A | 200: Éxito, 404: No encontrado, 500: Error servidor | Pública | No | N/A | Información completa del producto |
| GET | `/api/v1/productos/categoria/{categoria}` | Lista productos por categoría | N/A | 200: Éxito, 204: No hay productos, 500: Error servidor | Pública | No | N/A | Filtra por categoría (accessory/supplement) |
| GET | `/api/v1/productos/buscar?nombre={nombre}` | Busca productos por nombre | Query param: `nombre` | 200: Éxito, 204: No hay productos, 500: Error servidor | Pública | No | N/A | Búsqueda por coincidencia de nombre |
| POST | `/api/v1/productos` | Crea un nuevo producto | `{nombre, descripcion, precio, categoria, imagen?, stock}` | 201: Creado, 400: Datos inválidos, 500: Error servidor | Privada | Sí | Administrador | Requiere todos los campos obligatorios |
| PUT | `/api/v1/productos/{id}` | Actualiza información de un producto | `{nombre?, descripcion?, precio?, categoria?, imagen?, stock?}` | 200: Éxito, 404: No encontrado, 400: Datos inválidos | Privada | Sí | Administrador | Permite actualización parcial |
| DELETE | `/api/v1/productos/{id}` | Elimina un producto | N/A | 204: Éxito, 404: No encontrado, 500: Error servidor | Privada | Sí | Administrador | Eliminación permanente |
| PUT | `/api/v1/productos/{id}/stock` | Actualiza el stock de un producto | `{cantidad}` | 200: Éxito, 404: No encontrado, 400: Datos inválidos | Privada | Sí | Administrador | Actualización específica de stock |

---

## Microservicio de Carrito

**Base URL:** `http://localhost:8082`  
**Swagger UI:** `http://localhost:8082/swagger-ui.html`

| Método HTTP | Ruta del Endpoint | Descripción | Datos de entrada | Respuestas | API PÚBLICA/PRIVADA | Requiere Autenticación | Roles permitidos | Observaciones |
|-------------|-------------------|-------------|------------------|------------|---------------------|------------------------|------------------|----------------|
| GET | `/api/v1/carrito/usuario/{usuarioId}` | Obtiene el carrito de un usuario | N/A | 200: Éxito, 204: Carrito vacío, 500: Error servidor | Privada | Sí | Usuario | Retorna todos los items del carrito |
| POST | `/api/v1/carrito/agregar` | Agrega un producto al carrito | `{usuarioId, productoId, cantidad, precioUnitario}` | 201: Creado, 400: Datos inválidos, 500: Error servidor | Privada | Sí | Usuario | Agrega o actualiza cantidad si ya existe |
| PUT | `/api/v1/carrito/item/{itemId}` | Actualiza la cantidad de un item | `{cantidad}` | 200: Éxito, 404: Item no encontrado, 400: Datos inválidos | Privada | Sí | Usuario | Actualiza solo la cantidad |
| DELETE | `/api/v1/carrito/item/{itemId}` | Elimina un item específico del carrito | N/A | 204: Éxito, 404: Item no encontrado, 500: Error servidor | Privada | Sí | Usuario | Eliminación por ID de item |
| DELETE | `/api/v1/carrito/usuario/{usuarioId}` | Vacía todo el carrito de un usuario | N/A | 204: Éxito, 500: Error servidor | Privada | Sí | Usuario | Elimina todos los items del carrito |
| DELETE | `/api/v1/carrito/usuario/{usuarioId}/producto/{productoId}` | Elimina item por usuario y producto | N/A | 204: Éxito, 500: Error servidor | Privada | Sí | Usuario | Eliminación por usuario y producto |

---

## Microservicio de Pagos

**Base URL:** `http://localhost:8084`  
**Swagger UI:** `http://localhost:8084/swagger-ui.html`

| Método HTTP | Ruta del Endpoint | Descripción | Datos de entrada | Respuestas | API PÚBLICA/PRIVADA | Requiere Autenticación | Roles permitidos | Observaciones |
|-------------|-------------------|-------------|------------------|------------|---------------------|------------------------|------------------|----------------|
| GET | `/api/v1/pagos` | Lista todos los pagos | N/A | 200: Éxito, 204: No hay pagos, 500: Error servidor | Privada | Sí | Administrador | Lista completa de pagos |
| GET | `/api/v1/pagos/{id}` | Obtiene detalles de un pago | N/A | 200: Éxito, 404: No encontrado, 500: Error servidor | Privada | Sí | Administrador, Usuario | Usuario solo puede ver sus propios pagos |
| GET | `/api/v1/pagos/usuario/{usuarioId}` | Obtiene pagos de un usuario | N/A | 200: Éxito, 204: No hay pagos, 500: Error servidor | Privada | Sí | Administrador, Usuario | Usuario solo puede ver sus propios pagos |
| GET | `/api/v1/pagos/orden/{ordenId}` | Obtiene pagos de una orden | N/A | 200: Éxito, 204: No hay pagos, 500: Error servidor | Privada | Sí | Administrador, Usuario | Pagos asociados a una orden |
| GET | `/api/v1/pagos/estado/{estado}` | Obtiene pagos por estado | N/A | 200: Éxito, 204: No hay pagos, 500: Error servidor | Privada | Sí | Administrador | Filtra por estado (pendiente, completado, fallido) |
| POST | `/api/v1/pagos` | Crea un nuevo pago | `{ordenId, usuarioId, monto, metodoPago, informacionAdicional?}` | 201: Creado, 400: Datos inválidos, 500: Error servidor | Privada | Sí | Usuario | Crea un pago asociado a una orden |
| PUT | `/api/v1/pagos/{id}/estado` | Actualiza el estado de un pago | `{estado}` | 200: Éxito, 404: No encontrado, 400: Datos inválidos | Privada | Sí | Administrador | Actualiza estado (pendiente, completado, fallido) |
| DELETE | `/api/v1/pagos/{id}` | Elimina un pago | N/A | 204: Éxito, 404: No encontrado, 500: Error servidor | Privada | Sí | Administrador | Eliminación permanente (solo para administradores) |

---

## Microservicio de Órdenes

**Base URL:** `http://localhost:8085`  
**Swagger UI:** `http://localhost:8085/swagger-ui.html`

| Método HTTP | Ruta del Endpoint | Descripción | Datos de entrada | Respuestas | API PÚBLICA/PRIVADA | Requiere Autenticación | Roles permitidos | Observaciones |
|-------------|-------------------|-------------|------------------|------------|---------------------|------------------------|------------------|----------------|
| GET | `/api/v1/ordenes` | Lista todas las órdenes | N/A | 200: Éxito, 204: No hay órdenes, 500: Error servidor | Privada | Sí | Administrador | Lista completa de órdenes |
| GET | `/api/v1/ordenes/{id}` | Obtiene detalles de una orden | N/A | 200: Éxito, 404: No encontrada, 500: Error servidor | Privada | Sí | Administrador, Usuario | Usuario solo puede ver sus propias órdenes |
| GET | `/api/v1/ordenes/usuario/{usuarioId}` | Obtiene órdenes de un usuario | N/A | 200: Éxito, 204: No hay órdenes, 500: Error servidor | Privada | Sí | Administrador, Usuario | Usuario solo puede ver sus propias órdenes |
| GET | `/api/v1/ordenes/estado/{estado}` | Obtiene órdenes por estado | N/A | 200: Éxito, 204: No hay órdenes, 500: Error servidor | Privada | Sí | Administrador | Filtra por estado (pendiente, procesando, completada, cancelada) |
| GET | `/api/v1/ordenes/{id}/items` | Obtiene items de una orden | N/A | 200: Éxito, 204: No hay items, 500: Error servidor | Privada | Sí | Administrador, Usuario | Lista de productos en la orden |
| POST | `/api/v1/ordenes` | Crea una nueva orden | `{usuarioId, total, direccionEnvio?, notas?, items: [{productoId, cantidad, precioUnitario}]}` | 201: Creado, 400: Datos inválidos, 500: Error servidor | Privada | Sí | Usuario | Crea orden con items del carrito |
| PUT | `/api/v1/ordenes/{id}/estado` | Actualiza el estado de una orden | `{estado}` | 200: Éxito, 404: No encontrada, 400: Datos inválidos | Privada | Sí | Administrador | Actualiza estado (pendiente, procesando, completada, cancelada) |
| DELETE | `/api/v1/ordenes/{id}` | Elimina una orden | N/A | 204: Éxito, 404: No encontrada, 500: Error servidor | Privada | Sí | Administrador | Eliminación permanente (solo para administradores) |

---

## Notas Importantes

### Autenticación
- **Token JWT:** Los endpoints privados requieren un token JWT en el header `Authorization: Bearer {token}`
- **Obtención de token:** Se obtiene mediante el endpoint `/api/v1/usuario/login`
- **Expiración:** El token tiene una validez de 24 horas por defecto

### Roles del Sistema
- **Administrador:** Acceso completo a todos los endpoints
- **Usuario:** Acceso limitado a sus propios recursos (carrito, órdenes, pagos propios)

### Estados
- **Pagos:** `pendiente`, `completado`, `fallido`
- **Órdenes:** `pendiente`, `procesando`, `completada`, `cancelada`
- **Productos (categorías):** `accessory`, `supplement`

### Códigos de Respuesta HTTP
- **200:** Éxito
- **201:** Creado exitosamente
- **204:** Sin contenido (operación exitosa sin retorno)
- **400:** Datos inválidos o faltantes
- **401:** No autorizado (falta token o token inválido)
- **404:** Recurso no encontrado
- **500:** Error interno del servidor

### Configuración Actual
⚠️ **IMPORTANTE:** Actualmente todos los endpoints están configurados como públicos (`permitAll()`) para desarrollo. En producción, se debe implementar la validación de tokens JWT en cada microservicio.

---

## Ejemplos de Uso

### Login (Público)
```bash
POST http://localhost:8081/api/v1/usuario/login
Content-Type: application/json

{
  "email": "usuario@example.com",
  "password": "password123"
}
```

### Crear Producto (Privado - Requiere token)
```bash
POST http://localhost:8083/api/v1/productos
Authorization: Bearer {token}
Content-Type: application/json

{
  "nombre": "Proteína Whey",
  "descripcion": "Proteína de suero de leche",
  "precio": 49.99,
  "categoria": "supplement",
  "stock": 100,
  "imagen": "https://example.com/imagen.jpg"
}
```

### Agregar al Carrito (Privado - Requiere token)
```bash
POST http://localhost:8082/api/v1/carrito/agregar
Authorization: Bearer {token}
Content-Type: application/json

{
  "usuarioId": 1,
  "productoId": 5,
  "cantidad": 2,
  "precioUnitario": 49.99
}
```

