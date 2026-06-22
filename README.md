# clases-particulares-ms
# ClassMate v1.1.0

Plataforma de gestión de clases particulares basada en arquitectura de microservicios desarrollada con Spring Boot.

## Integrantes

- Patricio Espinoza
- José XXXXX
- Renato XXXXX

## Tecnologías utilizadas

- Java 21
- Spring Boot 3
- Spring Security
- Spring Data JPA
- OpenFeign
- Eureka Server
- API Gateway
- Docker & Docker Compose
- MySQL 8
- Flyway
- Swagger / OpenAPI
- JUnit 5
- Mockito
- JaCoCo
- HATEOAS
- Lombok

---

## Arquitectura

El sistema está compuesto por los siguientes microservicios:

- Auth Service
- Usuario Service
- Profesor Service
- Clase Service
- Reserva Service
- Pago Service
- Notificación Service
- Eureka Server
- API Gateway

---

## Requisitos

Antes de ejecutar el proyecto, asegúrese de tener instalado:

- Java 21
- Docker Desktop
- Git
- IntelliJ IDEA (opcional)
- Plugin Lombok (si utiliza IntelliJ)

### IntelliJ IDEA

Habilitar:

```text
Settings
→ Build, Execution, Deployment
→ Compiler
→ Annotation Processors
→ Enable annotation processing
```

---

## Instalación

### 1. Clonar repositorio

```bash
git clone https://github.com/PatricioEspinozaG/clases-particulares-ms.git
cd clases-particulares-ms
```

### 2. Cambiar a rama estable

```bash
git checkout main
```

### 3. Generar artefactos

```bash
.\mvnw.cmd clean package
```

### 4. Levantar contenedores

```bash
docker compose up -d
```

---

## Acceso a la aplicación

### API Gateway

```text
http://localhost:8080
```

Desde esta página se puede acceder a:

- Eureka Server
- Swagger UI de los microservicios
- Información general del sistema

### Eureka Server

```text
http://localhost:8761
```

---

## Testing

Ejecutar todos los tests:

```bash
.\mvnw.cmd test
```

Generar reporte de cobertura:

```bash
.\mvnw.cmd test
```

Reporte JaCoCo:

```text
target/site/jacoco/index.html
```

---

## Funcionalidades implementadas

### Auth Service

- Registro de usuarios
- Inicio de sesión
- Generación de JWT
- Seguridad con Spring Security

### Reserva Service

- Creación de reservas
- Consulta de reservas
- Actualización de reservas
- Eliminación de reservas
- Implementación HATEOAS

### Infraestructura

- API Gateway
- Service Discovery con Eureka
- Contenerización con Docker
- Migraciones con Flyway
- Documentación OpenAPI / Swagger

---

## Problemas comunes

### Error de Lombok en IntelliJ

Instalar plugin Lombok y habilitar Annotation Processing.

### Docker no encuentra archivos JAR

Generar nuevamente los artefactos:

```bash
.\mvnw.cmd clean package
```

Luego:

```bash
docker compose up -d
```

### Error al levantar contenedores en un equipo nuevo

Verificar que:

- Docker Desktop esté iniciado.
- WSL esté habilitado.
- Los JAR hayan sido generados correctamente mediante Maven.

---

## Versionado

### v1.0.0

Primera entrega del proyecto.

### v1.1.0

- Integración completa de microservicios.
- Docker Compose.
- Eureka Server.
- API Gateway.
- Swagger/OpenAPI.
- Testing con JUnit y Mockito.
- Cobertura con JaCoCo.
- Implementación HATEOAS.
- Mejoras de estabilidad y despliegue.

---
Desarrollado como proyecto académico para Duoc UC.