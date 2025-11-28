# Guía Completa para Probar el Microservicio en Postman

## 🔐 Información de Autenticación

### Usuarios Iniciales (creados automáticamente)

Según la configuración de `LoadDatabase.java`, se crean los siguientes usuarios:

| Username | Password | Email | Rol ID | Rol |
|----------|----------|-------|--------|-----|
| `admin` | `admin123` | `admin@qualifygym.com` | 1 | Administrador |
| `usuario1` | `usuario123` | `usuario1@qualifygym.com` | 2 | Usuario |

**Nota**: Las contraseñas están encriptadas con BCrypt, por lo que usaremos HTTP Basic Authentication en Postman.

---

## 🌐 Configuración Base

- **URL Base**: `http://localhost:8081`
- **Puerto**: 8081
- **Formato**: JSON

---

## 📋 Endpoints Públicos (NO requieren autenticación)

Estos endpoints están configurados como `permitAll()` en `SeguridadConfig.java`:

### 1. **GET** - Listar todos los usuarios

```
GET http://localhost:8081/api/v1/usuario/users
```

**En Postman:**
1. Método: `GET`
2. URL: `http://localhost:8081/api/v1/usuario/users`
3. Headers: (ninguno requerido)
4. Body: (vacío)

**Respuesta esperada (200 OK):**
```json
[
  {
    "id": 1,
    "username": "admin",
    "email": "admin@qualifygym.com",
    "rol": {
      "id": 1,
      "nombre": "Administrador"
    }
  },
  {
    "id": 2,
    "username": "usuario1",
    "email": "usuario1@qualifygym.com",
    "rol": {
      "id": 2,
      "nombre": "Usuario"
    }
  }
]
```

---

### 2. **POST** - Login

```
POST http://localhost:8081/api/v1/usuario/login
```

**En Postman:**
1. Método: `POST`
2. URL: `http://localhost:8081/api/v1/usuario/login`
3. Headers:
   - `Content-Type: application/json`
4. Body (raw JSON):
```json
{
  "username": "admin",
  "password": "admin123"
}
```

**Respuesta esperada (200 OK):**
```json
"Login exitoso"
```

**Si las credenciales son incorrectas (401 Unauthorized):**
```json
"Credenciales inválidas"
```

---

### 3. **POST** - Crear Usuario (público según configuración)

```
POST http://localhost:8081/api/v1/usuario/users
```

**En Postman:**
1. Método: `POST`
2. URL: `http://localhost:8081/api/v1/usuario/users`
3. Headers:
   - `Content-Type: application/json`
4. Body (raw JSON):
```json
{
  "username": "nuevo_usuario",
  "password": "password123",
  "email": "nuevo@qualifygym.com",
  "rolId": 2
}
```

**Respuesta esperada (201 Created):**
```json
{
  "id": 3,
  "username": "nuevo_usuario",
  "email": "nuevo@qualifygym.com",
  "rol": {
    "id": 2,
    "nombre": "Usuario"
  }
}
```

---

## 🔒 Endpoints Protegidos (requieren autenticación HTTP Basic)

### Configurar Autenticación HTTP Basic en Postman

Para los endpoints protegidos, necesitas configurar **HTTP Basic Authentication**:

#### Opción 1: Configurar en cada request

1. Ve a la pestaña **Authorization**
2. Selecciona **Type**: `Basic Auth`
3. Username: `admin` (o `usuario1`)
4. Password: `admin123` (o `usuario123`)
5. Postman automáticamente agregará el header `Authorization: Basic <token>`

#### Opción 2: Configurar a nivel de Collection (recomendado)

1. Crea una Collection en Postman
2. Click derecho en la Collection → **Edit**
3. Ve a la pestaña **Authorization**
4. Selecciona **Type**: `Basic Auth`
5. Username: `admin`
6. Password: `admin123`
7. Esto aplicará la autenticación a todos los requests de la collection

---

### 4. **GET** - Obtener Usuario por ID

```
GET http://localhost:8081/api/v1/usuario/users/{id}
```

**En Postman:**
1. Método: `GET`
2. URL: `http://localhost:8081/api/v1/usuario/users/1`
3. Authorization:
   - Type: `Basic Auth`
   - Username: `admin`
   - Password: `admin123`
4. Headers: (ninguno adicional)
5. Body: (vacío)

**Respuesta esperada (200 OK):**
```json
{
  "id": 1,
  "username": "admin",
  "email": "admin@qualifygym.com",
  "rol": {
    "id": 1,
    "nombre": "Administrador"
  }
}
```

**Si el usuario no existe (404 Not Found):**
```json
"Usuario no encontrado"
```

**Si no estás autenticado (401 Unauthorized):**
```
401 Unauthorized
```

---

### 5. **PUT** - Actualizar Usuario

```
PUT http://localhost:8081/api/v1/usuario/users/{id}
```

**En Postman:**
1. Método: `PUT`
2. URL: `http://localhost:8081/api/v1/usuario/users/2`
3. Authorization:
   - Type: `Basic Auth`
   - Username: `admin`
   - Password: `admin123`
4. Headers:
   - `Content-Type: application/json`
5. Body (raw JSON):
```json
{
  "username": "usuario1_actualizado",
  "email": "usuario1_nuevo@qualifygym.com",
  "password": "nueva_password123"
}
```

**Nota**: Puedes omitir campos que no quieras actualizar. El `rolId` es opcional.

**Respuesta esperada (200 OK):**
```json
{
  "id": 2,
  "username": "usuario1_actualizado",
  "email": "usuario1_nuevo@qualifygym.com",
  "rol": {
    "id": 2,
    "nombre": "Usuario"
  }
}
```

---

### 6. **DELETE** - Eliminar Usuario

```
DELETE http://localhost:8081/api/v1/usuario/users/{id}
```

**En Postman:**
1. Método: `DELETE`
2. URL: `http://localhost:8081/api/v1/usuario/users/3`
3. Authorization:
   - Type: `Basic Auth`
   - Username: `admin`
   - Password: `admin123`
4. Headers: (ninguno adicional)
5. Body: (vacío)

**Respuesta esperada (204 No Content):**
```
(Respuesta vacía)
```

**Si hay un error (500 Internal Server Error):**
```json
"Error al eliminar usuario: ..."
```

---

## 🐛 Solución de Problemas

### Error 401 Unauthorized

**Causas posibles:**

1. **Credenciales incorrectas**
   - Verifica que el username y password sean correctos
   - Usa: `admin` / `admin123` o `usuario1` / `usuario123`

2. **Autenticación no configurada**
   - Asegúrate de tener configurado HTTP Basic Auth en Postman
   - Ve a la pestaña Authorization y selecciona "Basic Auth"

3. **El endpoint requiere autenticación**
   - Verifica que el endpoint no esté en la lista de públicos
   - Los únicos públicos son: `/api/v1/usuario/login` y `/api/v1/usuario/users` (GET y POST)

**Solución:**
- Verifica en Postman que la pestaña **Authorization** tenga:
  - Type: `Basic Auth`
  - Username: `admin`
  - Password: `admin123`

---

### Error 404 Not Found

**Causas posibles:**

1. **El servidor no está corriendo**
   - Verifica que el microservicio esté ejecutándose en el puerto 8081
   - Revisa los logs de la aplicación

2. **URL incorrecta**
   - Verifica que la URL sea: `http://localhost:8081/api/v1/usuario/...`

**Solución:**
```bash
# Verifica que el servidor esté corriendo
# Deberías ver en los logs algo como:
# "Started Application in X.XXX seconds"
```

---

### Error 500 Internal Server Error

**Causas posibles:**

1. **Base de datos no configurada**
   - Verifica que MySQL esté corriendo
   - Verifica que la base de datos `UsuarioBD` exista
   - Verifica las credenciales en `application.properties`

2. **Datos inválidos**
   - Verifica que el JSON esté bien formado
   - Verifica que todos los campos requeridos estén presentes

**Solución:**
1. Crea la base de datos:
```sql
CREATE DATABASE UsuarioBD;
```

2. Verifica `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/UsuarioBD
spring.datasource.username=root
spring.datasource.password=tu_password
```

---

## 📝 Ejemplo Completo de Collection de Postman

Puedes importar esta collection JSON en Postman:

```json
{
  "info": {
    "name": "QualifyGym Usuario Microservice",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "auth": {
    "type": "basic",
    "basic": [
      {
        "key": "username",
        "value": "admin",
        "type": "string"
      },
      {
        "key": "password",
        "value": "admin123",
        "type": "string"
      }
    ]
  },
  "item": [
    {
      "name": "GET - Listar usuarios (público)",
      "request": {
        "auth": {
          "type": "noauth"
        },
        "method": "GET",
        "header": [],
        "url": {
          "raw": "http://localhost:8081/api/v1/usuario/users",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8081",
          "path": ["api", "v1", "usuario", "users"]
        }
      }
    },
    {
      "name": "POST - Login (público)",
      "request": {
        "auth": {
          "type": "noauth"
        },
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"username\": \"admin\",\n  \"password\": \"admin123\"\n}"
        },
        "url": {
          "raw": "http://localhost:8081/api/v1/usuario/login",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8081",
          "path": ["api", "v1", "usuario", "login"]
        }
      }
    },
    {
      "name": "POST - Crear usuario (público)",
      "request": {
        "auth": {
          "type": "noauth"
        },
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"username\": \"nuevo_usuario\",\n  \"password\": \"password123\",\n  \"email\": \"nuevo@qualifygym.com\",\n  \"rolId\": 2\n}"
        },
        "url": {
          "raw": "http://localhost:8081/api/v1/usuario/users",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8081",
          "path": ["api", "v1", "usuario", "users"]
        }
      }
    },
    {
      "name": "GET - Obtener usuario por ID",
      "request": {
        "method": "GET",
        "header": [],
        "url": {
          "raw": "http://localhost:8081/api/v1/usuario/users/1",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8081",
          "path": ["api", "v1", "usuario", "users", "1"]
        }
      }
    },
    {
      "name": "PUT - Actualizar usuario",
      "request": {
        "method": "PUT",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"username\": \"usuario_actualizado\",\n  \"email\": \"actualizado@qualifygym.com\"\n}"
        },
        "url": {
          "raw": "http://localhost:8081/api/v1/usuario/users/2",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8081",
          "path": ["api", "v1", "usuario", "users", "2"]
        }
      }
    },
    {
      "name": "DELETE - Eliminar usuario",
      "request": {
        "method": "DELETE",
        "header": [],
        "url": {
          "raw": "http://localhost:8081/api/v1/usuario/users/3",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8081",
          "path": ["api", "v1", "usuario", "users", "3"]
        }
      }
    }
  ]
}
```

---

## ✅ Checklist de Verificación

Antes de probar en Postman, asegúrate de:

- [ ] MySQL está corriendo
- [ ] Base de datos `UsuarioBD` está creada
- [ ] El microservicio está corriendo en el puerto 8081
- [ ] En Postman, estás usando la URL correcta: `http://localhost:8081`
- [ ] Para endpoints protegidos, has configurado HTTP Basic Auth
- [ ] Los usuarios iniciales se han creado (verifica en los logs)

---

## 🚀 Orden Recomendado para Probar

1. **GET /api/v1/usuario/users** (sin auth) - Verificar que el servidor funciona
2. **POST /api/v1/usuario/login** (sin auth) - Probar login
3. **POST /api/v1/usuario/users** (sin auth) - Crear un nuevo usuario
4. **GET /api/v1/usuario/users/{id}** (con auth) - Obtener usuario específico
5. **PUT /api/v1/usuario/users/{id}** (con auth) - Actualizar usuario
6. **DELETE /api/v1/usuario/users/{id}** (con auth) - Eliminar usuario

---

## 📚 Recursos Adicionales

- Swagger UI: `http://localhost:8081/swagger-ui.html` (requiere rol Administrador)
- API Docs: `http://localhost:8081/v3/api-docs` (requiere rol Administrador)

