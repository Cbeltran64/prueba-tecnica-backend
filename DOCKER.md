# Guía de Uso de Docker y Docker Compose

Este documento describe cómo levantar el sistema utilizando contenedores Docker para facilitar su despliegue.

## 🐳 Requisitos Previos

* Docker instalado
* Docker Compose instalado

## 📦 Estructura esperada

```
├── docker-compose.yml
├── productos-service/
│   └── Dockerfile
├── inventario-service/
│   └── Dockerfile
```

## ⚙️ Comando para ejecutar el sistema

Desde la raíz del proyecto, ejecutar:

```bash
docker-compose up --build
```

Este comando:

* Construye las imágenes para `productos-service` e `inventario-service`
* Levanta los contenedores junto con la base de datos PostgreSQL
* Expone los puertos necesarios para consumir los servicios

## 🌐 Acceso a los servicios

| Servicio                 | URL                                                                            |
|--------------------------|--------------------------------------------------------------------------------|
| Swagger Productos        | [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) |
| Swagger Inventario       | [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html) |
| Base de datos (Postgres) | Host: `localhost`, Puerto: `5432`                                              |

## ⚠️ Variables de Entorno

Asegúrate de que las variables de conexión a la base de datos estén definidas en los archivos `application.properties` o
manejadas por `docker-compose.yml`, por ejemplo:

```yaml
environment:
  - SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/mibasededatos
  - SPRING_DATASOURCE_USERNAME=postgres
  - SPRING_DATASOURCE_PASSWORD=admin
```

## 🧹 Detener los contenedores

```bash
docker-compose down
```

## ✅ Verificación

Al ejecutar correctamente `docker-compose up`, deberías ver logs de ambos servicios y acceso disponible a sus endpoints
mediante navegador.
