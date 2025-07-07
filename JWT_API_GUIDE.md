# API JWT Authentication - Guía de uso

## Descripción
Tu proyecto Spring Security con JWT ha sido configurado exitosamente. Ahora puedes usar autenticación basada en tokens JWT con clave HMAC256.

## Configuración JWT
- **Algoritmo**: HMAC256
- **Clave secreta**: `miClaveSecretaSuperSeguraParaJWT2025!`
- **Expiración**: 24 horas (86400000 ms)

## Endpoints disponibles

### 1. Registro de usuario (POST /auth/register)
**URL**: `http://localhost:8080/auth/register`

**Body (JSON)**:
```json
{
    "username": "nuevoUsuario",
    "password": "miPassword123",
    "email": "usuario@example.com",
    "firstName": "Juan",
    "lastName": "Pérez"
}
```

**Respuesta exitosa**:
```json
{
    "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9...",
    "username": "nuevoUsuario",
    "message": "Registro exitoso"
}
```

### 2. Login (POST /auth/login)
**URL**: `http://localhost:8080/auth/login`

**Body (JSON)**:
```json
{
    "username": "nuevoUsuario",
    "password": "miPassword123"
}
```

**Respuesta exitosa**:
```json
{
    "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9...",
    "username": "nuevoUsuario",
    "message": "Login exitoso"
}
```

### 3. Endpoint público (GET /auth/hello)
**URL**: `http://localhost:8080/auth/hello`
- No requiere autenticación
- Respuesta: `"Hello from TestAuthController"`

### 4. Endpoint protegido (GET /auth/hello-secured)
**URL**: `http://localhost:8080/auth/hello-secured`

**Headers requeridos**:
```
Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9...
```

**Respuesta exitosa**:
```
Hello from TestAuthController secured. Usuario: nuevoUsuario, Authorities: [USER]
```

### 5. Otro endpoint protegido (GET /auth/others)
**URL**: `http://localhost:8080/auth/others`

**Headers requeridos**:
```
Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9...
```

**Respuesta exitosa**:
```
Hello from TestAuthController others. Usuario: nuevoUsuario
```

## Cómo probar con curl

### 1. Registrar un nuevo usuario:
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123",
    "email": "test@example.com",
    "firstName": "Test",
    "lastName": "User"
  }'
```

### 2. Hacer login:
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

### 3. Usar el token en endpoints protegidos:
```bash
# Reemplaza YOUR_JWT_TOKEN con el token obtenido del login
curl -X GET http://localhost:8080/auth/hello-secured \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## Cómo probar con Postman

1. **Registro**: 
   - Método: POST
   - URL: `http://localhost:8080/auth/register`
   - Body: raw JSON con los datos del usuario

2. **Login**:
   - Método: POST
   - URL: `http://localhost:8080/auth/login`
   - Body: raw JSON con username y password

3. **Endpoints protegidos**:
   - Método: GET
   - URL: `http://localhost:8080/auth/hello-secured`
   - Headers: `Authorization: Bearer <token>`

## Swagger UI
Puedes acceder a la documentación interactiva en:
`http://localhost:8080/swagger-ui.html`

## Usuarios de prueba
El sistema crea automáticamente algunos usuarios de prueba con diferentes roles (ADMIN, USER, MODERATOR, GUEST).

## Notas importantes
- Los tokens JWT expiran en 24 horas
- Los nuevos usuarios registrados obtienen automáticamente el rol USER
- La aplicación usa gestión de sesiones sin estado (stateless)
- Los passwords se almacenan encriptados con BCrypt
