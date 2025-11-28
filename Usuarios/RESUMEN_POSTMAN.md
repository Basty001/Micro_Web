# 🚀 Resumen Rápido - Probar en Postman

## ⚠️ ERROR 401 - Solución Rápida

El error **401 Unauthorized** significa que **necesitas autenticarte** para ese endpoint.

---

## 📝 PASOS RÁPIDOS PARA PROBAR TODO

### 1️⃣ **Endpoints PÚBLICOS (NO necesitan autenticación)**

#### ✅ GET - Listar Usuarios
- **Método**: `GET`
- **URL**: `http://localhost:8081/api/v1/usuario/users`
- **Authorization**: ❌ NINGUNA (sin autenticación)
- **Body**: Vacío

#### ✅ POST - Login
- **Método**: `POST`
- **URL**: `http://localhost:8081/api/v1/usuario/login`
- **Authorization**: ❌ NINGUNA
- **Headers**: `Content-Type: application/json`
- **Body** (raw JSON):
```json
{
  "username": "admin",
  "password": "admin123"
}
```

#### ✅ POST - Crear Usuario
- **Método**: `POST`
- **URL**: `http://localhost:8081/api/v1/usuario/users`
- **Authorization**: ❌ NINGUNA
- **Headers**: `Content-Type: application/json`
- **Body** (raw JSON):
```json
{
  "username": "nuevo_usuario",
  "password": "password123",
  "email": "nuevo@qualifygym.com",
  "rolId": 2
}
```

---

### 2️⃣ **Endpoints PROTEGIDOS (SÍ necesitan autenticación)**

**⚠️ IMPORTANTE**: Para estos endpoints, DEBES configurar **HTTP Basic Authentication** en Postman.

#### 🔐 Cómo Configurar HTTP Basic Auth en Postman:

1. En Postman, ve a la pestaña **"Authorization"**
2. Selecciona **Type**: `Basic Auth`
3. Username: `admin`
4. Password: `admin123`
5. ¡Listo! Postman automáticamente agrega el header `Authorization`

---

#### ✅ GET - Obtener Usuario por ID
- **Método**: `GET`
- **URL**: `http://localhost:8081/api/v1/usuario/users/1`
- **Authorization**: ✅ **SÍ - HTTP Basic Auth**
  - Username: `admin`
  - Password: `admin123`
- **Body**: Vacío

#### ✅ PUT - Actualizar Usuario
- **Método**: `PUT`
- **URL**: `http://localhost:8081/api/v1/usuario/users/2`
- **Authorization**: ✅ **SÍ - HTTP Basic Auth**
  - Username: `admin`
  - Password: `admin123`
- **Headers**: `Content-Type: application/json`
- **Body** (raw JSON):
```json
{
  "username": "usuario_actualizado",
  "email": "actualizado@qualifygym.com"
}
```

#### ✅ DELETE - Eliminar Usuario
- **Método**: `DELETE`
- **URL**: `http://localhost:8081/api/v1/usuario/users/3`
- **Authorization**: ✅ **SÍ - HTTP Basic Auth**
  - Username: `admin`
  - Password: `admin123`
- **Body**: Vacío

---

## 🔑 Credenciales de Usuarios Iniciales

| Username | Password | Rol ID | Rol |
|----------|----------|--------|-----|
| `admin` | `admin123` | 1 | Administrador |
| `usuario1` | `usuario123` | 2 | Usuario |

---

## ✅ Checklist Antes de Probar

- [ ] ✅ MySQL está corriendo
- [ ] ✅ Base de datos `UsuarioBD` está creada
- [ ] ✅ El microservicio está corriendo (puerto 8081)
- [ ] ✅ Para endpoints protegidos: HTTP Basic Auth configurado en Postman

---

## 🎯 Orden Recomendado para Probar

1. **GET /api/v1/usuario/users** (sin auth) → Verificar que funciona
2. **POST /api/v1/usuario/login** (sin auth) → Probar login
3. **POST /api/v1/usuario/users** (sin auth) → Crear usuario
4. **GET /api/v1/usuario/users/1** (con auth) → Probar endpoint protegido
5. **PUT /api/v1/usuario/users/2** (con auth) → Actualizar usuario
6. **DELETE /api/v1/usuario/users/3** (con auth) → Eliminar usuario

---

## 🐛 Si sigues obteniendo 401:

1. **Verifica que estás usando la pestaña "Authorization"** en Postman
2. **Selecciona "Basic Auth"** como tipo
3. **Username**: `admin` (sin espacios)
4. **Password**: `admin123` (sin espacios)
5. **Guarda el request** y vuelve a enviarlo

---

## 📸 Visual: Cómo Configurar Basic Auth en Postman

```
┌─────────────────────────────────────────┐
│  Postman Request                        │
├─────────────────────────────────────────┤
│                                         │
│  [Params] [Authorization] [Headers] ... │
│           ↑ Click aquí                  │
│                                         │
│  Type: [Basic Auth ▼]                   │
│                                         │
│  Username: [admin          ]            │
│  Password: [admin123       ]            │
│                                         │
│  Postman genera automáticamente:        │
│  Authorization: Basic YWRtaW46YWRt...   │
│                                         │
└─────────────────────────────────────────┘
```

---

## 💡 Tip: Usar Collection de Postman

1. Crea una **Collection** en Postman
2. Configura la autenticación **a nivel de Collection**:
   - Click derecho en la Collection → **Edit**
   - Pestaña **Authorization**
   - Type: `Basic Auth`
   - Username: `admin`
   - Password: `admin123`
3. Todos los requests de la collection heredarán esta autenticación
4. Para los requests públicos, puedes sobrescribir la auth individualmente seleccionando "No Auth"

---

## 📞 Ejemplo de URLs Completas

- Público (sin auth): `http://localhost:8081/api/v1/usuario/users`
- Login: `http://localhost:8081/api/v1/usuario/login`
- Protegido (con auth): `http://localhost:8081/api/v1/usuario/users/1`
- Protegido (actualizar): `http://localhost:8081/api/v1/usuario/users/2`
- Protegido (eliminar): `http://localhost:8081/api/v1/usuario/users/3`

