# clases-particulares-ms

# ClassMate v1.1.0

Plataforma de gestión de clases particulares basada en arquitectura de microservicios desarrollada con Spring Boot.

## Integrantes

- Patricio Espinoza
- José Ramos
- Renato Figueroa

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

Primera ejecución o después de cambios en el código:

```bash
docker compose up -d --build
```

Ejecuciones posteriores:

```bash
docker compose up -d
```

### 5. Verificar servicios

Eureka Server:

```text
http://localhost:8761
```

API Gateway:

```text
http://localhost:8080
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

### Swagger UI

Auth Service:

```text
http://localhost:8081/doc/swagger-ui.html
```

Usuario Service:

```text
http://localhost:8082/doc/swagger-ui.html
```

Profesor Service:

```text
http://localhost:8083/doc/swagger-ui.html
```

Clase Service:

```text
http://localhost:8084/doc/swagger-ui.html
```

Reserva Service:

```text
http://localhost:8085/doc/swagger-ui.html
```

Pago Service:

```text
http://localhost:8086/doc/swagger-ui.html
```

Notificación Service:

```text
http://localhost:8087/doc/swagger-ui.html
```

---

## Testing

### Ejecutar todos los tests

```bash
.\mvnw.cmd test
```

### Ejecutar limpieza y pruebas

```bash
.\mvnw.cmd clean test
```

### Generar reporte de cobertura

```bash
.\mvnw.cmd test
```

Reporte JaCoCo:

```text
target/site/jacoco/index.html
```

## Funcionalidades implementadas

### Auth Service

- Registro de usuarios
- Inicio de sesión
- Generación de JWT
- Seguridad con Spring Security
- Validaciones
- Manejo centralizado de excepciones

### Reserva Service

- Creación de reservas
- Consulta de reservas
- Confirmación de reservas
- Cancelación de reservas
- Eliminación de reservas
- Comunicación con otros microservicios mediante Feign
- Implementación HATEOAS

### Infraestructura

- API Gateway
- Service Discovery con Eureka
- Contenerización con Docker
- Migraciones con Flyway
- Documentación OpenAPI / Swagger
- Testing con Mockito y JUnit
- Cobertura de código con JaCoCo

---

## Problemas comunes

### Error de Lombok en IntelliJ

Instalar el plugin Lombok y habilitar Annotation Processing.

Ruta:

```text
Settings
→ Build, Execution, Deployment
→ Compiler
→ Annotation Processors
→ Enable annotation processing
```

---

### Docker no encuentra archivos JAR

Generar nuevamente los artefactos:

```bash
.\mvnw.cmd clean package
```

Luego ejecutar:

```bash
docker compose up -d --build
```

---

### Los cambios realizados no aparecen

Ejemplos:

- Swagger no muestra cambios recientes.
- HATEOAS no aparece en las respuestas.
- Nuevos endpoints no están disponibles.
- Cambios de configuración no se reflejan.

Esto ocurre porque Docker puede estar utilizando imágenes antiguas.

Reconstruir las imágenes:

```bash
docker compose up -d --build
```

o:

```bash
docker compose build
docker compose up -d
```

El parámetro `--build` fuerza la reconstrucción utilizando el código fuente más reciente.

---

### Swagger no muestra la configuración personalizada

Si Swagger carga correctamente pero no aparecen:

- Título personalizado.
- Descripción personalizada.
- Información de contacto.
- Cambios recientes en OpenAPI.

Probablemente el contenedor esté ejecutando una imagen anterior.

Reconstruir los servicios:

```bash
docker compose up -d --build
```

Verificar también que los artefactos hayan sido generados:

```bash
.\mvnw.cmd clean package
```

---

### Error al levantar contenedores en un equipo nuevo

Verificar que:

- Docker Desktop esté iniciado.
- WSL esté habilitado.
- Los JAR hayan sido generados correctamente mediante Maven.
- Los puertos utilizados no estén ocupados.
- Docker tenga memoria suficiente asignada.

Comprobar contenedores:

```bash
docker ps
```

Ver logs:

```bash
docker logs nombre-del-contenedor
```

---

### Error 500 de Docker Desktop

Puede ocurrir cuando Docker Desktop queda sin memoria disponible.

Síntomas:

```text
request returned 500 Internal Server Error
```

Solución:

1. Reiniciar Docker Desktop.
2. Esperar a que el motor Linux vuelva a iniciar.
3. Ejecutar nuevamente:

```bash
docker compose up -d
```

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

## Buenas prácticas utilizadas

- Arquitectura de microservicios.
- Separación Controller - Service - Repository.
- DTOs para transferencia de datos.
- Manejo centralizado de excepciones.
- Testing unitario con Mockito.
- Cobertura de código con JaCoCo.
- Migraciones versionadas con Flyway.
- Documentación OpenAPI.
- Contenerización con Docker.

---

Desarrollado como proyecto académico para Duoc UC.
