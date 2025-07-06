# Spring Security JWT Implementation

Este proyecto implementa autenticación JWT usando Spring Boot 3.3.13 y Spring Security 6.

## Funcionalidades JWT Implementadas

### 1. Clases Principales

- **JwtUtil**: Utilidad para generar, validar y extraer información de tokens JWT
- **JwtTokenValidator**: Filtro para validar tokens JWT en cada request
- **UserDetailsServiceImpl**: Servicio que implementa autenticación con JWT
- **GlobalExceptionHandler**: Manejo global de excepciones

### 2. Endpoints de Autenticación

#### POST /auth/login
Autenticar un usuario existente y obtener un token JWT.

**Request Body:**
```json
{
    "username": "usuario",
    "password": "contraseña"
}
```

**Response:**
```json
{
    "username": "usuario",
    "message": "Usuario logeado correctamente",
    "jwt": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9...",
    "status": true
}
```

#### POST /auth/signup
Registrar un nuevo usuario y obtener un token JWT.

**Request Body:**
```json
{
    "username": "nuevoUsuario",
    "password": "nuevaContraseña",
    "roleRequest": {
        "roleListName": "USER"
    }
}
```

**Response:**
```json
{
    "username": "nuevoUsuario",
    "message": "Usuario creado correctamente",
    "jwt": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9...",
    "status": true
}
```

### 3. Endpoints de Prueba

- **GET /auth/hello** - Endpoint público (no requiere autenticación)
- **GET /auth/hello-secured** - Endpoint protegido (requiere token JWT)
- **GET /auth/others** - Endpoint protegido (requiere token JWT)

### 4. Uso del Token JWT

Para acceder a endpoints protegidos, incluye el token JWT en el header `Authorization`:

```
Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9...
```

### 5. Configuración

Las siguientes propiedades se pueden configurar en `application.properties`:

```properties
# JWT Configuration
security.jwt.secret-key=mySecretKey
security.jwt.user-generator=MySpringSecurityApp
security.jwt.token-expiration-time=86400000  # 24 horas en milisegundos
```

### 6. Estructura del Token JWT

El token incluye los siguientes claims:
- **iss** (issuer): Aplicación que genera el token
- **sub** (subject): Username del usuario
- **authorities**: Roles y permisos del usuario
- **iat** (issued at): Fecha de creación
- **exp** (expiration time): Fecha de expiración
- **jti** (JWT ID): ID único del token
- **nbf** (not before): Fecha desde la cual el token es válido

### 7. Swagger UI

La documentación de la API está disponible en:
- http://localhost:8080/swagger-ui.html

### 8. Cómo probar

1. **Iniciar la aplicación:**
   ```bash
   mvn spring-boot:run
   ```

2. **Crear un usuario (si no existe):**
   ```bash
   curl -X POST http://localhost:8080/auth/signup \
   -H "Content-Type: application/json" \
   -d '{
     "username": "testuser",
     "password": "password123",
     "roleRequest": {
       "roleListName": "USER"
     }
   }'
   ```

3. **Hacer login:**
   ```bash
   curl -X POST http://localhost:8080/auth/login \
   -H "Content-Type: application/json" \
   -d '{
     "username": "testuser",
     "password": "password123"
   }'
   ```

4. **Usar el token para acceder a endpoints protegidos:**
   ```bash
   curl -X GET http://localhost:8080/auth/hello-secured \
   -H "Authorization: Bearer [TOKEN_OBTENIDO]"
   ```

### 9. Seguridad

- Los tokens tienen una expiración configurada (24 horas por defecto)
- Las contraseñas se encriptan usando BCrypt
- La sesión es stateless (sin manejo de sesiones en el servidor)
- Se valida el token en cada request a endpoints protegidos

### 10. Manejo de Errores

El sistema maneja los siguientes tipos de errores:
- Usuario no encontrado (404)
- Credenciales inválidas (401)
- Token JWT inválido o expirado (401)
- Errores de validación (400)
- Errores internos del servidor (500)
