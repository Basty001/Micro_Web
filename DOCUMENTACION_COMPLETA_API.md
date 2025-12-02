# Documentación Completa de API - QualifyGym

## Descripción General

Este documento proporciona una descripción detallada de los endpoints de la API diseñados para gestionar las entidades del sistema QualifyGym (Usuarios, Productos, Carrito, Pagos y Órdenes) dentro de la aplicación. Está dirigido a desarrolladores y administradores de sistemas que interactúan con nuestra arquitectura de microservicios.

## Información General

### Microservicio de Usuarios
• **URL de la API:** `http://localhost:8081`  
• **URL Swagger:** `http://localhost:8081/swagger-ui.html`  
• **Base Path:** `/api/v1/usuario`

### Microservicio de Productos
• **URL de la API:** `http://localhost:8083`  
• **URL Swagger:** `http://localhost:8083/swagger-ui.html`  
• **Base Path:** `/api/v1/productos`

### Microservicio de Carrito
• **URL de la API:** `http://localhost:8082`  
• **URL Swagger:** `http://localhost:8082/swagger-ui.html`  
• **Base Path:** `/api/v1/carrito`

### Microservicio de Pagos
• **URL de la API:** `http://localhost:8084`  
• **URL Swagger:** `http://localhost:8084/swagger-ui.html`  
• **Base Path:** `/api/v1/pagos`

### Microservicio de Órdenes
• **URL de la API:** `http://localhost:8085`  
• **URL Swagger:** `http://localhost:8085/swagger-ui.html`  
• **Base Path:** `/api/v1/ordenes`

---

## Documentación de Endpoints

### Microservicio de Usuarios

#### Endpoints de Autenticación

**POST /api/v1/usuario/login**
- **Descripción:** Inicia sesión y obtiene token JWT para autenticación.
- **Método:** POST
- **Datos de Entrada:** `{email: "usuario@example.com", password: "password123"}`
- **Respuesta:** 
  - 200 (Éxito): Retorna token JWT y datos del usuario
  - 400 (Faltan campos requeridos)
  - 401 (Credenciales inválidas)
  - 500 (Error del servidor)
- **Autenticación:** No (Público)
- **Observaciones:** Este endpoint es público y retorna un token JWT que debe usarse en los headers de las siguientes peticiones.

**POST /api/v1/usuario/register**
- **Descripción:** Registro público de nuevo usuario en el sistema.
- **Método:** POST
- **Datos de Entrada:** `{username: "usuario123", password: "password123", email: "usuario@example.com", phone: "1234567890", address?: "Dirección opcional"}`
- **Respuesta:** 
  - 201 (Usuario creado exitosamente)
  - 400 (Datos inválidos o duplicados)
  - 500 (Error del servidor)
- **Autenticación:** No (Público)
- **Observaciones:** Asigna automáticamente el rol "Usuario" al nuevo registro.

#### Endpoints de Gestión de Usuarios

**GET /api/v1/usuario/users**
- **Descripción:** Lista todos los usuarios registrados en el sistema.
- **Método:** GET
- **Respuesta:** 
  - 200 (Lista de usuarios obtenida exitosamente)
  - 204 (No hay usuarios registrados)
  - 500 (Error del servidor)
- **Autenticación:** Sí (Admin)
- **Observaciones:** Solo datos básicos de usuarios. Requiere rol de Administrador.

**GET /api/v1/usuario/users/{id}**
- **Descripción:** Obtiene la información completa de un usuario específico por su ID.
- **Método:** GET
- **Respuesta:** 
  - 200 (Usuario encontrado exitosamente)
  - 404 (Usuario no encontrado)
  - 500 (Error del servidor)
- **Autenticación:** Sí (Admin)
- **Observaciones:** Retorna información completa incluyendo rol y datos personales.

**GET /api/v1/usuario/users/email/{email}**
- **Descripción:** Obtiene un usuario específico por su dirección de email.
- **Método:** GET
- **Respuesta:** 
  - 200 (Usuario encontrado exitosamente)
  - 404 (Usuario no encontrado)
  - 500 (Error del servidor)
- **Autenticación:** Sí (Admin)
- **Observaciones:** Búsqueda alternativa por email en lugar de ID.

**POST /api/v1/usuario/users**
- **Descripción:** Crea un nuevo usuario en el sistema.
- **Método:** POST
- **Datos de Entrada:** `{username: "usuario123", password: "password123", email: "usuario@example.com", phone: "1234567890", rolId: 1, address?: "Dirección opcional"}`
- **Respuesta:** 
  - 201 (Usuario creado exitosamente)
  - 400 (Datos inválidos o faltantes)
  - 401 (No autorizado - requiere rol Administrador)
- **Autenticación:** Sí (Admin)
- **Observaciones:** Requiere todos los campos obligatorios. Solo administradores pueden crear usuarios.

**PUT /api/v1/usuario/users/{id}**
- **Descripción:** Actualiza la información de un usuario existente.
- **Método:** PUT
- **Datos de Entrada:** `{username?: "nuevoUsuario", password?: "nuevaPassword", email?: "nuevo@email.com", phone?: "9876543210", rolId?: 2, address?: "Nueva dirección"}`
- **Respuesta:** 
  - 200 (Usuario actualizado exitosamente)
  - 400 (Datos inválidos o usuario no encontrado)
  - 401 (No autorizado - requiere rol Administrador)
- **Autenticación:** Sí (Admin)
- **Observaciones:** Permite actualización parcial de campos. Solo administradores pueden actualizar usuarios.

**DELETE /api/v1/usuario/users/{id}**
- **Descripción:** Elimina un usuario del sistema.
- **Método:** DELETE
- **Respuesta:** 
  - 204 (Usuario eliminado exitosamente)
  - 401 (No autorizado)
  - 500 (Error al eliminar usuario)
- **Autenticación:** Sí (Admin)
- **Observaciones:** Eliminación permanente. Solo administradores pueden eliminar usuarios.

**GET /api/v1/usuario/users/{id}/existe**
- **Descripción:** Verifica si un usuario existe en el sistema.
- **Método:** GET
- **Respuesta:** 
  - 200 (Éxito - retorna true o false)
  - 500 (Error del servidor)
- **Autenticación:** Sí (Admin)
- **Observaciones:** Útil para validaciones antes de operaciones críticas.

---

### Microservicio de Productos

#### Endpoints de Consulta

**GET /api/v1/productos**
- **Descripción:** Lista todos los productos disponibles en el catálogo.
- **Método:** GET
- **Respuesta:** 
  - 200 (Lista de productos obtenida exitosamente)
  - 204 (No hay productos)
  - 500 (Error del servidor)
- **Autenticación:** No (Público)
- **Observaciones:** Incluye todos los productos disponibles con información completa.

**GET /api/v1/productos/{id}**
- **Descripción:** Obtiene los detalles completos de un producto específico.
- **Método:** GET
- **Respuesta:** 
  - 200 (Producto encontrado)
  - 404 (Producto no encontrado)
  - 500 (Error del servidor)
- **Autenticación:** No (Público)
- **Observaciones:** Retorna información detallada incluyendo precio, stock, categoría e imagen.

**GET /api/v1/productos/categoria/{categoria}**
- **Descripción:** Lista productos filtrados por categoría.
- **Método:** GET
- **Respuesta:** 
  - 200 (Lista de productos obtenida exitosamente)
  - 204 (No hay productos en esta categoría)
  - 500 (Error del servidor)
- **Autenticación:** No (Público)
- **Observaciones:** Categorías disponibles: "accessory" y "supplement".

**GET /api/v1/productos/buscar?nombre={nombre}**
- **Descripción:** Busca productos que coincidan con el nombre proporcionado.
- **Método:** GET
- **Parámetros:** Query parameter `nombre` (ejemplo: `?nombre=proteina`)
- **Respuesta:** 
  - 200 (Lista de productos encontrados)
  - 204 (No se encontraron productos)
  - 500 (Error del servidor)
- **Autenticación:** No (Público)
- **Observaciones:** Búsqueda por coincidencia parcial del nombre.

#### Endpoints de Gestión

**POST /api/v1/productos**
- **Descripción:** Crea un nuevo producto en el catálogo.
- **Método:** POST
- **Datos de Entrada:** `{nombre: "Proteína Whey", descripcion: "Proteína de suero de leche de alta calidad", precio: 49.99, categoria: "supplement", imagen?: "https://example.com/imagen.jpg", stock: 100}`
- **Respuesta:** 
  - 201 (Producto creado exitosamente)
  - 400 (Datos inválidos)
  - 500 (Error del servidor)
- **Autenticación:** Sí (Admin)
- **Observaciones:** Requiere todos los campos obligatorios. Solo administradores pueden crear productos.

**PUT /api/v1/productos/{id}**
- **Descripción:** Actualiza la información de un producto existente.
- **Método:** PUT
- **Datos de Entrada:** `{nombre?: "Nuevo Nombre", descripcion?: "Nueva descripción", precio?: 59.99, categoria?: "accessory", imagen?: "https://example.com/nueva-imagen.jpg", stock?: 150}`
- **Respuesta:** 
  - 200 (Producto actualizado exitosamente)
  - 404 (Producto no encontrado)
  - 400 (Datos inválidos)
- **Autenticación:** Sí (Admin)
- **Observaciones:** Permite actualización parcial de campos. Solo administradores pueden actualizar productos.

**DELETE /api/v1/productos/{id}**
- **Descripción:** Elimina un producto del catálogo.
- **Método:** DELETE
- **Respuesta:** 
  - 204 (Producto eliminado exitosamente)
  - 404 (Producto no encontrado)
  - 500 (Error del servidor)
- **Autenticación:** Sí (Admin)
- **Observaciones:** Eliminación permanente. Solo administradores pueden eliminar productos.

**PUT /api/v1/productos/{id}/stock**
- **Descripción:** Actualiza específicamente el stock de un producto.
- **Método:** PUT
- **Datos de Entrada:** `{cantidad: 200}`
- **Respuesta:** 
  - 200 (Stock actualizado exitosamente)
  - 404 (Producto no encontrado)
  - 400 (Datos inválidos)
- **Autenticación:** Sí (Admin)
- **Observaciones:** Endpoint especializado para actualización rápida de inventario.

---

### Microservicio de Carrito

**GET /api/v1/carrito/usuario/{usuarioId}**
- **Descripción:** Obtiene todos los items del carrito de un usuario específico.
- **Método:** GET
- **Respuesta:** 
  - 200 (Carrito obtenido exitosamente)
  - 204 (Carrito vacío)
  - 500 (Error del servidor)
- **Autenticación:** Sí (Usuario)
- **Observaciones:** Retorna lista de items con información del producto, cantidad y precio unitario.

**POST /api/v1/carrito/agregar**
- **Descripción:** Agrega un producto al carrito del usuario o actualiza la cantidad si ya existe.
- **Método:** POST
- **Datos de Entrada:** `{usuarioId: 1, productoId: 5, cantidad: 2, precioUnitario: 49.99}`
- **Respuesta:** 
  - 201 (Item agregado exitosamente)
  - 400 (Datos inválidos)
  - 500 (Error del servidor)
- **Autenticación:** Sí (Usuario)
- **Observaciones:** Si el producto ya está en el carrito, se actualiza la cantidad en lugar de crear un duplicado.

**PUT /api/v1/carrito/item/{itemId}**
- **Descripción:** Actualiza la cantidad de un item específico en el carrito.
- **Método:** PUT
- **Datos de Entrada:** `{cantidad: 5}`
- **Respuesta:** 
  - 200 (Cantidad actualizada exitosamente)
  - 404 (Item no encontrado)
  - 400 (Datos inválidos)
- **Autenticación:** Sí (Usuario)
- **Observaciones:** Solo actualiza la cantidad, no modifica otros campos del item.

**DELETE /api/v1/carrito/item/{itemId}**
- **Descripción:** Elimina un item específico del carrito por su ID.
- **Método:** DELETE
- **Respuesta:** 
  - 204 (Item eliminado exitosamente)
  - 404 (Item no encontrado)
  - 500 (Error del servidor)
- **Autenticación:** Sí (Usuario)
- **Observaciones:** Eliminación de un item específico del carrito.

**DELETE /api/v1/carrito/usuario/{usuarioId}**
- **Descripción:** Vacía completamente el carrito de un usuario.
- **Método:** DELETE
- **Respuesta:** 
  - 204 (Carrito vaciado exitosamente)
  - 500 (Error del servidor)
- **Autenticación:** Sí (Usuario)
- **Observaciones:** Elimina todos los items del carrito de un usuario. Útil después de completar una compra.

**DELETE /api/v1/carrito/usuario/{usuarioId}/producto/{productoId}**
- **Descripción:** Elimina un item del carrito identificado por usuario y producto.
- **Método:** DELETE
- **Respuesta:** 
  - 204 (Item eliminado exitosamente)
  - 500 (Error del servidor)
- **Autenticación:** Sí (Usuario)
- **Observaciones:** Alternativa para eliminar items sin conocer el ID del item del carrito.

---

### Microservicio de Pagos

**GET /api/v1/pagos**
- **Descripción:** Lista todos los pagos registrados en el sistema.
- **Método:** GET
- **Respuesta:** 
  - 200 (Lista de pagos obtenida exitosamente)
  - 204 (No hay pagos)
  - 500 (Error del servidor)
- **Autenticación:** Sí (Admin)
- **Observaciones:** Solo administradores pueden ver todos los pagos del sistema.

**GET /api/v1/pagos/{id}**
- **Descripción:** Obtiene los detalles de un pago específico.
- **Método:** GET
- **Respuesta:** 
  - 200 (Pago encontrado)
  - 404 (Pago no encontrado)
  - 500 (Error del servidor)
- **Autenticación:** Sí (Admin, Usuario)
- **Observaciones:** Los usuarios solo pueden ver sus propios pagos.

**GET /api/v1/pagos/usuario/{usuarioId}**
- **Descripción:** Obtiene todos los pagos realizados por un usuario específico.
- **Método:** GET
- **Respuesta:** 
  - 200 (Lista de pagos obtenida exitosamente)
  - 204 (No hay pagos)
  - 500 (Error del servidor)
- **Autenticación:** Sí (Admin, Usuario)
- **Observaciones:** Los usuarios solo pueden ver sus propios pagos.

**GET /api/v1/pagos/orden/{ordenId}**
- **Descripción:** Obtiene todos los pagos asociados a una orden específica.
- **Método:** GET
- **Respuesta:** 
  - 200 (Lista de pagos obtenida exitosamente)
  - 204 (No hay pagos)
  - 500 (Error del servidor)
- **Autenticación:** Sí (Admin, Usuario)
- **Observaciones:** Útil para rastrear pagos relacionados con una orden.

**GET /api/v1/pagos/estado/{estado}**
- **Descripción:** Filtra pagos por su estado (pendiente, completado, fallido).
- **Método:** GET
- **Respuesta:** 
  - 200 (Lista de pagos obtenida exitosamente)
  - 204 (No hay pagos con ese estado)
  - 500 (Error del servidor)
- **Autenticación:** Sí (Admin)
- **Observaciones:** Solo administradores pueden filtrar por estado.

**POST /api/v1/pagos**
- **Descripción:** Crea un nuevo registro de pago asociado a una orden.
- **Método:** POST
- **Datos de Entrada:** `{ordenId: 10, usuarioId: 1, monto: 149.97, metodoPago: "tarjeta", informacionAdicional?: "Visa terminada en 1234"}`
- **Respuesta:** 
  - 201 (Pago creado exitosamente)
  - 400 (Datos inválidos)
  - 500 (Error del servidor)
- **Autenticación:** Sí (Usuario)
- **Observaciones:** Crea un pago asociado a una orden existente.

**PUT /api/v1/pagos/{id}/estado**
- **Descripción:** Actualiza el estado de un pago (pendiente, completado, fallido).
- **Método:** PUT
- **Datos de Entrada:** `{estado: "completado"}`
- **Respuesta:** 
  - 200 (Estado actualizado exitosamente)
  - 404 (Pago no encontrado)
  - 400 (Datos inválidos)
- **Autenticación:** Sí (Admin)
- **Observaciones:** Solo administradores pueden actualizar el estado de los pagos.

**DELETE /api/v1/pagos/{id}**
- **Descripción:** Elimina un registro de pago del sistema.
- **Método:** DELETE
- **Respuesta:** 
  - 204 (Pago eliminado exitosamente)
  - 404 (Pago no encontrado)
  - 500 (Error del servidor)
- **Autenticación:** Sí (Admin)
- **Observaciones:** Eliminación permanente. Solo para administradores.

---

### Microservicio de Órdenes

**GET /api/v1/ordenes**
- **Descripción:** Lista todas las órdenes registradas en el sistema.
- **Método:** GET
- **Respuesta:** 
  - 200 (Lista de órdenes obtenida exitosamente)
  - 204 (No hay órdenes)
  - 500 (Error del servidor)
- **Autenticación:** Sí (Admin)
- **Observaciones:** Solo administradores pueden ver todas las órdenes.

**GET /api/v1/ordenes/{id}**
- **Descripción:** Obtiene los detalles completos de una orden específica.
- **Método:** GET
- **Respuesta:** 
  - 200 (Orden encontrada)
  - 404 (Orden no encontrada)
  - 500 (Error del servidor)
- **Autenticación:** Sí (Admin, Usuario)
- **Observaciones:** Los usuarios solo pueden ver sus propias órdenes.

**GET /api/v1/ordenes/usuario/{usuarioId}**
- **Descripción:** Obtiene todas las órdenes realizadas por un usuario específico.
- **Método:** GET
- **Respuesta:** 
  - 200 (Lista de órdenes obtenida exitosamente)
  - 204 (No hay órdenes)
  - 500 (Error del servidor)
- **Autenticación:** Sí (Admin, Usuario)
- **Observaciones:** Los usuarios solo pueden ver sus propias órdenes.

**GET /api/v1/ordenes/estado/{estado}**
- **Descripción:** Filtra órdenes por su estado (pendiente, procesando, completada, cancelada).
- **Método:** GET
- **Respuesta:** 
  - 200 (Lista de órdenes obtenida exitosamente)
  - 204 (No hay órdenes con ese estado)
  - 500 (Error del servidor)
- **Autenticación:** Sí (Admin)
- **Observaciones:** Solo administradores pueden filtrar por estado.

**GET /api/v1/ordenes/{id}/items**
- **Descripción:** Obtiene todos los items (productos) incluidos en una orden específica.
- **Método:** GET
- **Respuesta:** 
  - 200 (Lista de items obtenida exitosamente)
  - 204 (No hay items)
  - 500 (Error del servidor)
- **Autenticación:** Sí (Admin, Usuario)
- **Observaciones:** Retorna detalles de cada producto en la orden con cantidades y precios.

**POST /api/v1/ordenes**
- **Descripción:** Crea una nueva orden con sus items asociados.
- **Método:** POST
- **Datos de Entrada:** 
```json
{
  "usuarioId": 1,
  "total": 149.97,
  "direccionEnvio": "Calle Principal 123",
  "notas": "Entregar por la mañana",
  "items": [
    {
      "productoId": 5,
      "cantidad": 2,
      "precioUnitario": 49.99
    },
    {
      "productoId": 3,
      "cantidad": 1,
      "precioUnitario": 50.00
    }
  ]
}
```
- **Respuesta:** 
  - 201 (Orden creada exitosamente)
  - 400 (Datos inválidos)
  - 500 (Error del servidor)
- **Autenticación:** Sí (Usuario)
- **Observaciones:** Crea una orden completa con múltiples items. Normalmente se crea desde el carrito del usuario.

**PUT /api/v1/ordenes/{id}/estado**
- **Descripción:** Actualiza el estado de una orden (pendiente, procesando, completada, cancelada).
- **Método:** PUT
- **Datos de Entrada:** `{estado: "procesando"}`
- **Respuesta:** 
  - 200 (Estado actualizado exitosamente)
  - 404 (Orden no encontrada)
  - 400 (Datos inválidos)
- **Autenticación:** Sí (Admin)
- **Observaciones:** Solo administradores pueden actualizar el estado de las órdenes.

**DELETE /api/v1/ordenes/{id}**
- **Descripción:** Elimina una orden y todos sus items asociados.
- **Método:** DELETE
- **Respuesta:** 
  - 204 (Orden eliminada exitosamente)
  - 404 (Orden no encontrada)
  - 500 (Error del servidor)
- **Autenticación:** Sí (Admin)
- **Observaciones:** Eliminación permanente. Solo para administradores.

---

## Integración con el Frontend

### Verificación de Entrega

Esta sección describe cómo el frontend interactúa con los endpoints de los microservicios, proporcionando ejemplos de uso y verificaciones de respuestas de la API.

#### Flujo de Autenticación

1. **Login del Usuario:**
   - El frontend envía credenciales al endpoint `/api/v1/usuario/login`
   - El microservicio valida las credenciales y retorna un token JWT
   - El frontend almacena el token en `localStorage` y lo incluye en todas las peticiones posteriores

2. **Uso del Token:**
   - Todas las peticiones a endpoints privados incluyen el header: `Authorization: Bearer {token}`
   - El token tiene una validez de 24 horas
   - Al expirar, el usuario debe iniciar sesión nuevamente

#### Flujo de Compra Completo

1. **Visualización de Productos:**
   - El frontend consulta `/api/v1/productos` para mostrar el catálogo
   - Los usuarios pueden filtrar por categoría usando `/api/v1/productos/categoria/{categoria}`
   - La búsqueda se realiza mediante `/api/v1/productos/buscar?nombre={nombre}`

2. **Gestión del Carrito:**
   - Al agregar un producto, se llama a `/api/v1/carrito/agregar`
   - El carrito se actualiza en tiempo real consultando `/api/v1/carrito/usuario/{usuarioId}`
   - Los usuarios pueden modificar cantidades con `/api/v1/carrito/item/{itemId}`

3. **Proceso de Checkout:**
   - Se crea una orden mediante `/api/v1/ordenes` con los items del carrito
   - Se procesa el pago llamando a `/api/v1/pagos`
   - Se actualiza el stock de productos
   - Se vacía el carrito usando `/api/v1/carrito/usuario/{usuarioId}`

4. **Seguimiento de Órdenes:**
   - Los usuarios consultan sus órdenes con `/api/v1/ordenes/usuario/{usuarioId}`
   - Los administradores pueden ver todas las órdenes y cambiar su estado

### Ejemplo Visual

#### Pantalla de Login
- **Solicitud:** POST `/api/v1/usuario/login`
- **Datos enviados:** `{email: "usuario@example.com", password: "password123"}`
- **Respuesta exitosa:** 
  - Código: 200
  - Body: `{token: "eyJhbGc...", message: "Login exitoso", usuario: {...}}`
- **Interfaz:** El usuario es redirigido al panel principal y el token se almacena

#### Pantalla de Catálogo de Productos
- **Solicitud:** GET `/api/v1/productos`
- **Respuesta exitosa:**
  - Código: 200
  - Body: `[{id: 1, nombre: "Proteína Whey", precio: 49.99, ...}, ...]`
- **Interfaz:** Se muestra una grilla de productos con imágenes, precios y botones de "Agregar al carrito"

#### Pantalla de Carrito
- **Solicitud:** GET `/api/v1/carrito/usuario/{usuarioId}`
- **Respuesta exitosa:**
  - Código: 200
  - Body: `[{id: 1, producto: {...}, cantidad: 2, precioUnitario: 49.99}, ...]`
- **Interfaz:** Se muestra lista de items con opciones para modificar cantidad o eliminar

#### Pantalla de Checkout
- **Solicitud:** POST `/api/v1/ordenes`
- **Datos enviados:** `{usuarioId: 1, total: 149.97, items: [...]}`
- **Respuesta exitosa:**
  - Código: 201
  - Body: `{id: 10, usuarioId: 1, total: 149.97, estado: "pendiente", ...}`
- **Interfaz:** Se muestra confirmación de orden y se redirige al panel de usuario

#### Pantalla de Panel de Usuario
- **Solicitud:** GET `/api/v1/ordenes/usuario/{usuarioId}`
- **Respuesta exitosa:**
  - Código: 200
  - Body: `[{id: 10, total: 149.97, estado: "completada", fechaCreacion: "2024-12-01", ...}, ...]`
- **Interfaz:** Se muestra historial de compras con detalles de cada orden

#### Pantalla de Panel de Administrador
- **Solicitudes múltiples:**
  - GET `/api/v1/usuario/users` - Lista de usuarios
  - GET `/api/v1/productos` - Lista de productos
  - GET `/api/v1/ordenes` - Lista de órdenes
  - GET `/api/v1/pagos` - Lista de pagos
- **Interfaz:** Dashboard con estadísticas y opciones para gestionar todas las entidades del sistema

### Manejo de Errores

#### Error de Autenticación (401)
- **Causa:** Token inválido o expirado
- **Comportamiento:** El frontend redirige al usuario a la página de login
- **Mensaje:** "Tu sesión ha expirado. Por favor, inicia sesión nuevamente."

#### Error de Validación (400)
- **Causa:** Datos inválidos o faltantes
- **Comportamiento:** Se muestran mensajes de error específicos en el formulario
- **Ejemplo:** "El campo 'email' es requerido" o "El formato del precio es inválido"

#### Error de Recurso No Encontrado (404)
- **Causa:** El recurso solicitado no existe
- **Comportamiento:** Se muestra un mensaje informativo
- **Ejemplo:** "Producto no encontrado" o "Usuario no encontrado"

#### Error del Servidor (500)
- **Causa:** Error interno en el microservicio
- **Comportamiento:** Se muestra un mensaje genérico de error
- **Mensaje:** "Ha ocurrido un error. Por favor, intenta nuevamente más tarde."

---

## Notas Técnicas

### Autenticación JWT
- **Algoritmo:** HS256 (HMAC-SHA256)
- **Expiración:** 86400000 ms (24 horas)
- **Header requerido:** `Authorization: Bearer {token}`
- **Obtención:** Endpoint `/api/v1/usuario/login`

### CORS
- Todos los microservicios están configurados para aceptar peticiones desde cualquier origen (`*`)
- Métodos permitidos: GET, POST, PUT, DELETE, OPTIONS
- Headers permitidos: Todos (`*`)

### Formato de Datos
- **Content-Type:** `application/json`
- **Encoding:** UTF-8
- **Fechas:** Formato ISO 8601 (ejemplo: "2024-12-01T10:30:00")

### Estados de Entidades
- **Pagos:** `pendiente`, `completado`, `fallido`
- **Órdenes:** `pendiente`, `procesando`, `completada`, `cancelada`
- **Productos (categorías):** `accessory`, `supplement`
- **Usuarios (roles):** `Administrador`, `Moderador`, `Usuario`

---

## Configuración de Desarrollo

⚠️ **IMPORTANTE:** Actualmente todos los endpoints están configurados como públicos (`permitAll()`) para facilitar el desarrollo. En producción, se debe implementar la validación de tokens JWT en cada microservicio mediante filtros de Spring Security.

### Próximos Pasos para Producción
1. Implementar filtros JWT en cada microservicio
2. Configurar validación de roles en endpoints específicos
3. Restringir endpoints públicos solo a los necesarios (login, register, consulta de productos)
4. Implementar rate limiting para prevenir abuso
5. Configurar HTTPS para todas las comunicaciones

