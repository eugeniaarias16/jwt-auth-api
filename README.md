# Spring Security JWT API

Una API REST robusta desarrollada con **Spring Boot** que implementa autenticación y autorización mediante **JWT (JSON Web Tokens)**, con un sistema completo de gestión de usuarios, roles y permisos.

## 🚀 Características Principales

- **Autenticación JWT**: Sistema de tokens stateless para sesiones seguras
- **Autorización granular**: Control de acceso basado en roles y permisos
- **Gestión completa de usuarios**: CRUD con validaciones y seguridad
- **Sistema de roles**: ADMIN y USER con permisos diferenciados
- **Inicialización automática**: Configuración de datos básicos del sistema
- **Arquitectura por capas**: Separación clara de responsabilidades
- **Documentación API**: Integración con Swagger/OpenAPI
- **Logging avanzado**: Trazabilidad completa con SLF4J

## 🏗️ Arquitectura del Proyecto

```
src/main/java/com/eugeniaArias/spring_security_jwt/
├── controller/          # Controladores REST
├── dto/                # Data Transfer Objects
│   ├── request/        # DTOs para requests
│   └── response/       # DTOs para responses
├── entity/             # Entidades JPA
├── exceptions/         # Manejo global de excepciones
├── repository/         # Interfaces de repositorio
├── security/           # Configuración de seguridad
│   ├── config/         # Configuración y filtros
│   └── utils/          # Utilidades JWT y sistema
└── service/            # Lógica de negocio
```

## 🛠️ Tecnologías Utilizadas

- **Java 17+**
- **Spring Boot 3.x**
- **Spring Security 6.x**
- **Spring Data JPA**
- **MySQL 8**
- **JWT (java-jwt)**
- **Lombok**
- **Maven**
- **SLF4J + Logback**

## ⚡ Inicio Rápido

### Prerrequisitos

- Java 17 o superior
- MySQL 8
- Maven 3.6+
- IDE (recomendado: IntelliJ IDEA o VS Code)

### Configuración

1. **Clonar el repositorio**
```bash
git clone <url-del-repositorio>
cd spring-security-jwt
```

2. **Configurar variables de entorno**
```bash
# Base de datos
export BD_URL="jdbc:mysql://localhost:3306/spring_security_jwt"
export BD_USER="tu_usuario"
export BD_PASSWORD="tu_password"

# JWT
export JWT_SECRET="tu_clave_secreta_jwt_muy_segura"
export JWT_ISSUER="SpringSecurityJWTAPI"

# Spring Security
export SS_USERNAME="admin"
export SS_PASSWORD="password"
```

3. **Crear la base de datos**
```sql
CREATE DATABASE spring_security_jwt;
```

4. **Ejecutar la aplicación**
```bash
mvn spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080/api/v1`

## 📋 Endpoints Principales

### Autenticación
- `POST /api/v1/auth/register` - Registro de usuario
- `POST /api/v1/auth/login` - Inicio de sesión

### Usuarios (Requiere autenticación)
- `GET /api/v1/users` - Listar usuarios
- `GET /api/v1/users/{id}` - Obtener usuario por ID
- `POST /api/v1/users/createWithRoles` - Crear usuario con roles (ADMIN)
- `PUT /api/v1/users/updatePassword/{id}` - Actualizar contraseña
- `DELETE /api/v1/users/delete/{id}` - Eliminar usuario (ADMIN)

### Roles (Requiere autenticación)
- `GET /api/v1/roles` - Listar roles
- `POST /api/v1/roles/create/{roleName}` - Crear rol (ADMIN)
- `POST /api/v1/roles/createWithPermissions` - Crear rol con permisos (ADMIN)

### Permisos (Requiere autenticación)
- `GET /api/v1/permissions` - Listar permisos
- `POST /api/v1/permissions/create/{permissionName}` - Crear permiso
- `POST /api/v1/permissions/createBasicPermissions` - Crear permisos básicos (ADMIN)

## 🔐 Sistema de Seguridad

### Roles Predefinidos
- **ADMIN**: Acceso completo (CREATE, READ, UPDATE, DELETE)
- **USER**: Solo lectura (READ)

### Permisos Básicos
- `CREATE` - Crear recursos
- `READ` - Leer recursos
- `UPDATE` - Actualizar recursos
- `DELETE` - Eliminar recursos

### Autenticación JWT
```bash
# Ejemplo de request con token
curl -H "Authorization: Bearer <tu_jwt_token>" \
     http://localhost:8080/api/v1/users
```

## 📝 Ejemplos de Uso

### 1. Registrar un nuevo usuario
```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "johndoe",
    "password": "securePassword123",
    "email": "john@example.com"
  }'
```

### 2. Iniciar sesión
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "johndoe",
    "password": "securePassword123"
  }'
```

### 3. Acceder a recursos protegidos
```bash
curl -H "Authorization: Bearer <token_obtenido>" \
     http://localhost:8080/api/v1/users
```

## 🔧 Configuración Avanzada

### application.properties
```properties
# Configuración principal en src/main/resources/application.properties
server.port=8080
server.servlet.context-path=/api/v1

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true

# JWT
security.jwt.private.key=${JWT_SECRET}
security.jwt.user.generator=${JWT_ISSUER}
```

### Personalización de Roles
El sistema se inicializa automáticamente con roles básicos, pero puedes:
- Crear roles personalizados vía API
- Asignar permisos específicos
- Modificar la lógica en `SystemInitializer.java`

## 🧪 Testing

```bash
# Ejecutar tests
mvn test

# Ejecutar con coverage
mvn test jacoco:report
```

## 📚 Documentación API

Una vez ejecutada la aplicación, accede a:
- **Swagger UI**: `http://localhost:8080/api/v1/swagger-ui/index.html`
- **OpenAPI JSON**: `http://localhost:8080/api/v1/v3/api-docs`

## 🚨 Consideraciones de Seguridad

- **Variables de entorno**: Nunca hardcodees credenciales
- **JWT Secret**: Usa una clave robusta de al menos 256 bits
- **HTTPS**: Implementa SSL/TLS en producción
- **Rate Limiting**: Considera implementar limitación de requests
- **Validación**: Todos los inputs son validados

## 🤝 Contribución

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/nueva-funcionalidad`)
3. Commit tus cambios (`git commit -m 'Añade nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Abre un Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para más detalles.

## 📞 Contacto

**Eugenia Arias**
- GitHub: [@eugeniaArias](https://github.com/eugeniaArias)
- Email: eugenia@example.com

---

## 🔍 Estructura de Respuestas

### Success Response
```json
{
  "id": 1,
  "username": "johndoe",
  "email": "john@example.com",
  "roles": ["USER"],
  "active": true
}
```

### Error Response
```json
{
  "error": "invalid_token",
  "message": "Token has expired"
}
```

**¡Gracias por usar Spring Security JWT API!** 🚀
