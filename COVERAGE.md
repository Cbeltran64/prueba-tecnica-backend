# Informe de Cobertura de Pruebas (JaCoCo)

Este documento presenta los resultados de cobertura obtenidos mediante JaCoCo para los microservicios del proyecto.

## 📊 Resumen de Cobertura

| Microservicio      | Instrucciones Cubiertas | Total Instrucciones | Cobertura (%) |
|--------------------|-------------------------|---------------------|---------------|
| productos-service  | 331                     | 494                 | 67%           |
| inventario-service | 142                     | 208                 | 68%           |

## 📁 Detalle por Paquete

### productos-service

| Paquete                                            | Cobertura |
|----------------------------------------------------|-----------|
| com.microservicios.productos\_service.controller   | 27%       |
| com.microservicios.productos\_service.security     | 81%       |
| com.microservicios.productos\_service.service.impl | 93%       |
| com.microservicios.productos\_service.mapper       | 91%       |
| com.microservicios.productos\_service.exception    | 100%      |
| com.microservicios.productos\_service.config       | 100%      |

### inventario-service

| Paquete                                             | Cobertura |
|-----------------------------------------------------|-----------|
| com.microservicios.inventario\_service.controller   | 100%      |
| com.microservicios.inventario\_service.service.impl | 86%       |
| com.microservicios.inventario\_service.mapper       | 53%       |
| com.microservicios.inventario\_service.exception    | 100%      |
| com.microservicios.inventario\_service.config       | 0%        |

## 📌 Observaciones

* Se priorizó la cobertura de lógica de negocio y controladores.
* Las clases DTOs y configuraciones simples no se forzaron a testear dado que no contienen lógica.
* Se utilizó `Mockito` para pruebas unitarias desacopladas y cobertura confiable.

## 🧪 Herramienta utilizada

* JaCoCo (Java Code Coverage Library)

Para más detalles, ver los informes generados en `target/site/jacoco/index.html` dentro de cada servicio.
