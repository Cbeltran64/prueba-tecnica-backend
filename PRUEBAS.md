# Informe de Pruebas Unitarias

Este documento describe las pruebas realizadas en el sistema, las herramientas utilizadas y los casos cubiertos.

## 🧪 Tipos de pruebas realizadas

* **Pruebas unitarias:** Se probaron los servicios, controladores, manejadores de excepciones y configuración.
* **Pruebas con mocks:** Uso de `Mockito` para simular el comportamiento de dependencias.

## 🛠️ Herramientas utilizadas

* **JUnit 5**: Framework principal para ejecutar pruebas.
* **Mockito**: Utilizado para simular repositorios, servicios y dependencias.
* **JaCoCo**: Para medir la cobertura de código.

## 📦 Casos de prueba cubiertos

### productos-service

* Crear producto (servicio y controlador)
* Obtener producto por ID (existente y no existente)
* Actualizar producto (existente y no existente)
* Eliminar producto inexistente (excepción)
* Listar productos paginados
* Validación de excepción personalizada y validaciones de campos
* Validación de configuración Swagger

### inventario-service

* Consultar inventario por productoId (existente y no existente)
* Actualizar inventario (existente y nuevo registro)
* Validación de excepción personalizada y errores de validación
* Controlador completamente cubierto
* Configuración Swagger validada

## 🧪 Cómo ejecutar las pruebas manualmente

Desde la raíz del proyecto, ejecutar:

```bash
mvn test
```

Esto ejecutará todas las pruebas de ambos servicios.

## ✅ Resultado

* Pruebas ejecutadas exitosamente en ambos microservicios.
* Cobertura global mayor al 65% en cada módulo.

Para más detalles de cobertura, ver [`COVERAGE.md`](COVERAGE.md).
