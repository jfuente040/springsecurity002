
# Spring Boot Security - JWT API

Este proyecto es una aplicación Spring Boot que implementa una API REST segura utilizando Spring Security y JSON Web Tokens (JWT). La aplicación gestiona productos y cuenta con un sistema de autenticación y autorización basado en roles y permisos.

## Características

- **Autenticación con JWT**: Proceso de login que genera un token JWT para el usuario.
- **Registro de Usuarios**: Endpoint para registrar nuevos usuarios con un rol por defecto.
- **Autorización Basada en Roles y Permisos**:
  - **Roles**: ADMIN, USER, MODERATOR, GUEST.
  - **Permisos**: READ_CONTENT, CREATE_CONTENT, UPDATE_CONTENT, DELETE_CONTENT, etc.
- **Gestión de Productos**: Operaciones CRUD (Crear, Leer, Actualizar, Eliminar) sobre una entidad `Product`.
- **Documentación con Swagger**: Interfaz de Swagger UI para explorar y probar los endpoints de la API.
- **Inicialización de Datos**: Creación automática de roles, permisos y usuarios de prueba al iniciar la aplicación.

## Tecnologías Utilizadas

- **Java 21**
- **Spring Boot 3.3**
- **Spring Security**
- **Spring Data JPA**
- **MySQL**
- **Maven**
- **Lombok**
- **java-jwt (Auth0)**
- **Springdoc OpenAPI (Swagger)**

## Estructura del Proyecto

El proyecto sigue una estructura estándar de Spring Boot:

```
src/main/java/com/jfuente040/springsecurity002/
├── config/                # Clases de configuración (Seguridad, JWT, OpenAPI)
├── controller/            # Controladores REST para la API
├── dto/                   # Data Transfer Objects (DTOs) para requests y responses
├── persistence/
│   ├── entity/            # Entidades JPA (User, Role, Permission, Product)
│   └── repository/        # Reposorios de Spring Data JPA
└── service/               # Lógica de negocio (AuthService, ProductService, JwtService)
```

## Cómo Empezar

### Prerrequisitos

- JDK 21 o superior
- Maven 3.6 o superior
- Una instancia de MySQL en ejecución

### Configuración

1.  **Clonar el repositorio**:
    ```bash
    git clone <URL_DEL_REPOSITORIO>
    cd springsecurity002
    ```

2.  **Configurar la base de datos**:
    Abre el archivo `src/main/resources/application.properties` y actualiza las siguientes propiedades con la configuración de tu base de datos MySQL:

    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/springsecurity002db
    spring.datasource.username=tu_usuario
    spring.datasource.password=tu_contraseña
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
    spring.jpa.hibernate.ddl-auto=update
    ```

3.  **Instalar dependencias y ejecutar la aplicación**:
    ```bash
    mvn spring-boot:run
    ```
    La aplicación se iniciará en el puerto `8080`.

## Endpoints de la API

La aplicación expone los siguientes endpoints:

### Autenticación

-   `POST /auth/login`: Autentica a un usuario y devuelve un token JWT.
    **Request Body**:
    ```json
    {
      "username": "admin",
      "password": "admin123"
    }
    ```

-   `POST /auth/register`: Registra un nuevo usuario.
    **Request Body**:
    ```json
    {
      "username": "newuser",
      "password": "password123",
      "email": "newuser@example.com",
      "firstName": "New",
      "lastName": "User"
    }
    ```

### Productos (Requiere autenticación)

-   `GET /api/products`: Obtiene todos los productos.
    -   **Permisos**: `READ_CONTENT` o rol `ADMIN`.

-   `GET /api/products/{id}`: Obtiene un producto por su ID.
    -   **Permisos**: `READ_CONTENT` o rol `ADMIN`.

-   `POST /api/products`: Crea un nuevo producto.
    -   **Permisos**: `CREATE_CONTENT` o rol `ADMIN`.

-   `PUT /api/products/{id}`: Actualiza un producto existente.
    -   **Permisos**: `UPDATE_CONTENT` o rol `ADMIN`.

-   `DELETE /api/products/{id}`: Elimina un producto.
    -   **Permisos**: `DELETE_CONTENT` o rol `ADMIN`.

### Endpoints de Prueba

-   `GET /auth/hello`: Endpoint público.
-   `GET /auth/hello-secured`: Endpoint que requiere autenticación.
-   `GET /token-info`: Devuelve información sobre el token JWT proporcionado en el header `Authorization`.

## Documentación de la API (Swagger)

Puedes acceder a la documentación interactiva de la API a través de Swagger UI en la siguiente URL:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Inicialización de Datos

Al iniciar la aplicación, se crean los siguientes usuarios y roles de forma automática:

-   **Usuarios**:
    -   `admin` (password: `admin123`) - Rol: `ADMIN`
    -   `user` (password: `user123`) - Rol: `USER`
    -   `moderator` (password: `moderator123`) - Rol: `MODERATOR`
    -   `guest` (password: `guest123`) - Rol: `GUEST`

-   **Roles y Permisos**:
    -   `ADMIN`: Todos los permisos.
    -   `USER`: Permisos de lectura.
    -   `MODERATOR`: Permisos de lectura y actualización.
    -   `GUEST`: Permisos de lectura de contenido.
