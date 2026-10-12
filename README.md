# EFT Desarrollo Backend III – Modernización del Banco XYZ

**Estudiante:** Felipe Hidalgo  
**Asignatura:** Desarrollo Backend III  
**Evaluación:** Evaluación Final Transversal (EFT) – Semana 9  
**Proyecto:** Modernización del sistema bancario Banco XYZ

## 1. Descripción del proyecto

Este proyecto corresponde a la Evaluación Final Transversal de la asignatura Desarrollo Backend III. Su objetivo es modernizar parte del sistema del Banco XYZ mediante una arquitectura basada en microservicios, procesamiento de datos por lotes y tecnologías que permiten mejorar la seguridad, integración y disponibilidad de los servicios.

La solución fue desarrollada principalmente con Java y Spring Boot, utilizando distintas herramientas del ecosistema Spring para separar responsabilidades y facilitar la comunicación entre los componentes del sistema.

También se incorporan Docker Compose y mecanismos de escalabilidad horizontal para ejecutar varias instancias de un mismo microservicio.

## 2. Objetivo general

Desarrollar una solución backend para el Banco XYZ que permita procesar operaciones bancarias, exponer servicios mediante distintas interfaces, proteger el acceso a los recursos y facilitar el despliegue de sus componentes mediante una arquitectura de microservicios.

## 3. Tecnologías utilizadas

| Tecnología | Uso en el proyecto |
|---|---|
| Java 17 | Lenguaje de programación |
| Spring Boot | Desarrollo de aplicaciones y servicios backend |
| Spring Batch | Procesamiento de información por lotes |
| Spring Security | Protección de endpoints |
| OAuth2 | Autorización de acceso a los servicios |
| Spring Cloud Config | Configuración centralizada |
| Eureka Server | Registro y descubrimiento de microservicios |
| Resilience4j | Tolerancia a fallos y recuperación |
| Apache Kafka | Comunicación mediante eventos |
| MySQL | Persistencia de datos |
| Docker y Docker Compose | Contenedorización y ejecución de servicios |
| Maven | Gestión de dependencias y compilación |
| Postman | Pruebas de endpoints y autorización |
| Git y GitHub | Control de versiones y entrega del proyecto |

## 4. Arquitectura del sistema

La solución está organizada en componentes que cumplen distintas funciones.

### Procesamiento por lotes

El componente `batch-service` utiliza Spring Batch para ejecutar procesos relacionados con las operaciones bancarias.

Se implementaron tres procesos principales:

- Procesamiento de transacciones bancarias.
- Cálculo de intereses mensuales.
- Generación de estados de cuenta anuales.

Además, se trabajó con mecanismos de manejo de errores, reintentos, reejecución segura y procesamiento paralelo.

### Microservicios

El sistema cuenta con tres microservicios principales:

| Microservicio | Responsabilidad | Puerto de aplicación |
|---|---|---|
| `ms-clientes` | Gestión y consulta de información de clientes | 8091 |
| `ms-cuentas` | Gestión de cuentas bancarias | 8092 |
| `ms-pagos` | Procesamiento e integración de operaciones de pago | 8093 |

Cada microservicio se desarrolla como una aplicación independiente, con su propia configuración y conexión a la base de datos correspondiente.

### Backend for Frontend (BFF)

Se incorporaron tres componentes BFF para separar el acceso según el tipo de cliente:

- `bff-web`: operaciones destinadas a clientes web.
- `bff-movil`: operaciones destinadas a aplicaciones móviles.
- `bff-cajero`: operaciones destinadas al canal de cajero automático.

Estos componentes utilizan mecanismos de autenticación y autorización para controlar el acceso a los recursos.

### Servicios de infraestructura

El proyecto también incluye:

- `auth-server`: componente relacionado con la autorización OAuth2.
- `config-server`: servidor de configuración centralizada.
- `config-repo`: archivos de configuración de los microservicios.
- `eureka-server`: registro y descubrimiento de servicios.
- `kafka-config`: archivos de configuración utilizados durante la integración con Kafka.

## 5. Estructura del proyecto

```text
EFT_Backend_III_Felipe_Hidalgo/
│
├── auth-server/
├── batch-service/
├── bff-cajero/
├── bff-movil/
├── bff-web/
├── config-repo/
├── config-server/
├── eureka-server/
├── kafka-config/
├── ms-clientes/
├── ms-cuentas/
├── ms-pagos/
│
├── docker-compose.yml
├── README.md
├── instrucciones.md
└── despliegue.md
```

Los archivos de configuración local con contraseñas, los resultados de compilación y los respaldos de Kafka no forman parte del código que debe publicarse en GitHub.

## 6. Procesamiento con Spring Batch

Se desarrollaron tres Jobs para representar procesos habituales de una institución bancaria.

**Procesamiento de transacciones:** permite trabajar con registros de operaciones bancarias, aplicando las validaciones y reglas del proceso por lotes.

**Cálculo de intereses mensuales:** procesa la información necesaria para generar los cálculos de intereses correspondientes.

**Estados de cuenta anuales:** procesa información de operaciones para generar los registros asociados a los estados de cuenta.

Durante las pruebas también se verificaron características como:

- Manejo de registros inválidos mediante mecanismos de omisión (*skip*).
- Reintentos de procesamiento (*retry*).
- Reejecución segura para evitar duplicaciones.
- Procesamiento paralelo con varios hilos.
- Registro de resultados y comprobación mediante consultas a MySQL.

## 7. Seguridad

Para controlar el acceso a los servicios se utilizan Spring Security y OAuth2.

La seguridad permite distinguir entre solicitudes sin autorización, solicitudes autorizadas y operaciones para las cuales un usuario no cuenta con los permisos necesarios.

Durante las pruebas realizadas con Postman se verificaron respuestas como:

- **200 OK:** solicitud autorizada y procesada correctamente.
- **201 Created:** creación correcta de un recurso.
- **400 Bad Request:** datos que no cumplen las validaciones.
- **401 Unauthorized:** solicitud sin autenticación válida.
- **403 Forbidden:** solicitud con permisos insuficientes.

También se probaron permisos de lectura y escritura según la operación solicitada.

## 8. Configuración y descubrimiento de servicios

Se utiliza **Spring Cloud Config Server** para administrar configuraciones de manera centralizada.

Por otra parte, **Eureka Server** permite que los microservicios se registren y puedan ser identificados dentro de la arquitectura.

En las pruebas realizadas se comprobó el registro de `MS-CLIENTES`, `MS-CUENTAS` y `MS-PAGOS`.

## 9. Resiliencia y comunicación mediante eventos

### Resilience4j

Se incorporaron mecanismos de resiliencia para gestionar fallos de comunicación entre servicios.

Durante las pruebas se verificó el comportamiento del sistema ante fallos, la respuesta alternativa mediante *fallback* y la recuperación posterior.

### Apache Kafka

Kafka se utiliza para integrar servicios mediante eventos.

Durante las pruebas se trabajó con el tópico `transacciones-completadas`, configurado con tres particiones, y se comprobó la interacción entre componentes productores y consumidores.

## 10. Docker Compose y escalabilidad horizontal

Los microservicios `ms-clientes`, `ms-cuentas` y `ms-pagos` cuentan con archivos Dockerfile para su ejecución mediante contenedores.

El archivo `docker-compose.yml` permite administrar su despliegue y conectarlos a la red Docker correspondiente.

Durante las pruebas se logró mantener activos los tres microservicios y verificar su registro en Eureka.

También se realizó una prueba de **escalabilidad horizontal**, ejecutando dos instancias de `ms-clientes` simultáneamente.

El resultado fue:

| Servicio | Instancias activas |
|---|---:|
| `ms-clientes` | 2 |
| `ms-cuentas` | 1 |
| `ms-pagos` | 1 |

Eureka reconoció las dos instancias activas de clientes, lo que permitió verificar el registro de las réplicas. Esta prueba demuestra la replicación del microservicio, aunque no constituye por sí sola una prueba de balanceo de carga.

## 11. Requisitos para ejecutar el proyecto

Para ejecutar el proyecto en un entorno local se requiere:

- Java 17.
- Maven o Maven Wrapper, según el módulo.
- MySQL Server.
- Docker Desktop con Docker Compose.
- Servicios de configuración y descubrimiento disponibles.
- Apache Kafka configurado.
- Variables de entorno necesarias para las conexiones y autenticación.

Las contraseñas no deben quedar escritas directamente en el repositorio.

Para los microservicios ejecutados mediante Docker Compose se utiliza la variable de entorno `DB_PASSWORD`, definida localmente en un archivo `.env` que no debe publicarse.

## 12. Ejecución mediante Docker Compose

Antes de iniciar los contenedores, es necesario contar con las imágenes o los archivos JAR compilados, además de los servicios externos que utiliza la arquitectura.

Desde la carpeta principal del proyecto se puede ejecutar:

```powershell
docker compose up -d --build
```

Para comprobar los contenedores:

```powershell
docker compose ps -a
```

Para ejecutar dos instancias del microservicio de clientes, con la configuración de puertos dinámicos incluida en Docker Compose:

```powershell
docker compose up -d --scale ms-clientes=2
```

Para verificar nuevamente los contenedores:

```powershell
docker compose ps -a
```

La configuración, las dependencias y el orden de ejecución se explicarán con mayor detalle en los archivos `instrucciones.md` y `despliegue.md`.

## 13. Pruebas realizadas

Durante el desarrollo se utilizaron herramientas como Postman, PowerShell, MySQL y Docker Desktop para comprobar el funcionamiento de los componentes.

Entre las pruebas realizadas se encuentran:

- Ejecución de los tres procesos de Spring Batch.
- Validación de resultados almacenados en MySQL.
- Pruebas de reintentos y reejecución segura.
- Comprobación de procesamiento paralelo.
- Acceso autorizado y no autorizado a los BFF.
- Validación de permisos mediante OAuth2.
- Creación y consulta de registros de clientes y cuentas.
- Verificación de mecanismos de resiliencia.
- Comunicación mediante Kafka.
- Registro de microservicios en Eureka.
- Ejecución de contenedores mediante Docker Compose.
- Escalabilidad horizontal de `ms-clientes`.

Los resultados de las pruebas se encuentran respaldados mediante evidencias utilizadas en el informe técnico de la evaluación.

## 14. Propuesta de despliegue en la nube

Como parte de la solución se considera un despliegue en AWS, utilizando servicios de infraestructura en la nube para alojar los componentes y facilitar su administración.

La propuesta contempla aspectos como contenedorización, configuración de red, seguridad, persistencia, disponibilidad y escalabilidad.

Esta sección corresponde a una propuesta de despliegue y no implica que todos los componentes hayan sido implementados efectivamente en AWS.

Los detalles de la propuesta se documentarán en `despliegue.md`.

## 15. Documentación complementaria

Este repositorio incluye o incorporará los siguientes documentos:

- `README.md`: descripción general y organización del proyecto.
- `instrucciones.md`: requisitos y procedimientos para preparar y ejecutar el sistema.
- `despliegue.md`: configuración de Docker, escalabilidad y propuesta de despliegue.
- Informe técnico de la EFT: desarrollo, pruebas, resultados y evidencias.

## 16. Conclusión

El desarrollo de esta evaluación permitió aplicar diferentes herramientas utilizadas en arquitecturas backend modernas, integrando procesamiento por lotes, microservicios, seguridad, mensajería y contenedores.

Uno de los principales aprendizajes fue comprender cómo se relacionan los distintos componentes y cómo los errores de configuración pueden afectar a varios servicios al mismo tiempo.

Finalmente, las pruebas realizadas permitieron comprobar el funcionamiento de los procesos principales y la ejecución simultánea de múltiples instancias de un microservicio, acercando la solución a una arquitectura más modular y preparada para seguir creciendo.