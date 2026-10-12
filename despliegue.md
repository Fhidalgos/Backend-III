# Preparación y despliegue del sistema – EFT Backend III

**Estudiante:** Felipe Hidalgo  
**Asignatura:** Desarrollo Backend III  
**Proyecto:** Modernización del sistema bancario Banco XYZ  
**Evaluación:** Evaluación Final Transversal – Semana 9

## 1. Introducción

Este documento describe la preparación del proyecto para su ejecución mediante contenedores y su posible despliegue futuro en una plataforma de nube como AWS.

Durante el desarrollo de la evaluación se trabajó con una arquitectura de microservicios, utilizando herramientas como Spring Boot, Spring Cloud, MySQL, Apache Kafka y Docker Compose.

La implementación y las pruebas de contenedores se realizaron en un entorno local. En cuanto a AWS, el objetivo fue dejar planteada la preparación técnica necesaria para un futuro despliegue, sin realizar una implementación completa en la nube.

## 2. Objetivo del despliegue

El objetivo es contar con una arquitectura que permita ejecutar los componentes del sistema de manera organizada y que facilite su mantenimiento, configuración y ampliación.

Para esto se consideraron los siguientes aspectos:

- Separación de los servicios según sus responsabilidades.
- Contenedorización de los microservicios.
- Configuración de conexiones mediante variables de entorno.
- Registro y descubrimiento de servicios.
- Comunicación entre servicios mediante eventos.
- Ejecución de múltiples instancias de un microservicio.
- Preparación de una propuesta de infraestructura en AWS.

## 3. Arquitectura del proyecto

El sistema está compuesto por varios componentes que trabajan en conjunto.

| Componente | Función |
|---|---|
| `ms-clientes` | Gestión de información de clientes |
| `ms-cuentas` | Gestión de cuentas bancarias |
| `ms-pagos` | Operaciones relacionadas con pagos |
| `bff-web` | Acceso a funcionalidades destinadas al canal web |
| `bff-movil` | Acceso a funcionalidades destinadas al canal móvil |
| `bff-cajero` | Acceso a funcionalidades destinadas al canal de cajero |
| `auth-server` | Autenticación y autorización |
| `config-server` | Configuración centralizada |
| `eureka-server` | Registro y descubrimiento de servicios |
| `batch-service` | Procesamiento de operaciones por lotes |
| Apache Kafka | Comunicación mediante eventos |
| MySQL | Persistencia de información |

Esta organización permite separar las responsabilidades y facilita que distintos componentes puedan configurarse o ejecutarse de manera independiente.

## 4. Preparación de los microservicios con Docker

Para los tres microservicios principales se prepararon archivos `Dockerfile`.

Cada archivo permite crear una imagen Docker a partir de un JAR generado mediante la compilación del proyecto Spring Boot.

En el entorno utilizado durante la evaluación se trabajó con Java 17 y una imagen base de Eclipse Temurin.

La idea es que cada microservicio pueda ejecutarse dentro de su propio contenedor, manteniendo separado su proceso de ejecución.

### Microservicios contenedorizados

| Microservicio | Puerto interno |
|---|---:|
| `ms-clientes` | 8091 |
| `ms-cuentas` | 8092 |
| `ms-pagos` | 8093 |

La configuración de los contenedores se administra mediante el archivo `docker-compose.yml`.

## 5. Configuración mediante Docker Compose

Docker Compose se utilizó para definir y ejecutar los tres microservicios principales.

El archivo `docker-compose.yml` contiene las instrucciones de construcción, puertos, variables de entorno, comandos de inicio y conexión a la red Docker.

Durante las pruebas locales se utilizó la red externa:

```text
eft-network
```

Esta red permite que los contenedores se comuniquen con otros servicios disponibles en el entorno Docker, como Kafka.

También se utilizó `host.docker.internal` para acceder desde los contenedores a determinados servicios ejecutados directamente en Windows.

### Variables de configuración

Entre las variables utilizadas se encuentran:

| Variable | Propósito |
|---|---|
| `CONFIG_SERVER_URL` | Dirección del servidor de configuración |
| `DB_PASSWORD` | Contraseña de la base de datos |
| `SPRING_DATASOURCE_URL` | Dirección JDBC de MySQL |
| `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE` | Dirección del servidor Eureka |
| `SPRING_KAFKA_BOOTSTRAP_SERVERS` | Dirección de conexión a Kafka |

La contraseña de MySQL se mantiene en un archivo local `.env`, excluido del repositorio mediante `.gitignore`.

Esta separación permite cambiar determinados valores según el entorno, sin modificar directamente el código Java.

## 6. Dependencias necesarias para la ejecución

En la implementación local, los contenedores de los microservicios dependen de otros servicios que deben estar disponibles.

Estos son:

- **MySQL:** almacena la información de las bases de datos.
- **Config Server:** entrega las configuraciones centralizadas.
- **Eureka Server:** permite el registro de los microservicios.
- **Apache Kafka:** proporciona la infraestructura de comunicación mediante eventos.

Es importante aclarar que el `docker-compose.yml` utilizado durante la evaluación administra directamente los tres microservicios principales, pero no levanta por sí solo todos los componentes de la arquitectura.

Por este motivo, MySQL, Config Server, Eureka y Kafka deben prepararse previamente.

## 7. Compilación de los microservicios

Antes de construir las imágenes Docker, es necesario generar los archivos JAR de cada microservicio.

Desde la carpeta correspondiente se puede utilizar:

```powershell
mvn clean package -DskipTests
```

La compilación debe realizarse para:

- `ms-clientes`
- `ms-cuentas`
- `ms-pagos`

Los archivos JAR generados se utilizan posteriormente en las instrucciones de los respectivos Dockerfile.

La opción `-DskipTests` permite omitir la ejecución de pruebas durante el empaquetado, pero no reemplaza la validación del funcionamiento de cada servicio.

## 8. Ejecución de Docker Compose

Una vez preparados los servicios externos, las variables de entorno y los archivos JAR, desde la raíz del proyecto se puede ejecutar:

```powershell
docker compose up -d --build
```

Este comando construye las imágenes que sean necesarias y ejecuta los contenedores definidos en Docker Compose.

Para comprobar su estado:

```powershell
docker compose ps -a
```

Durante las pruebas realizadas se logró mantener activos simultáneamente:

- `ms-clientes`
- `ms-cuentas`
- `ms-pagos`

Los tres microservicios también se registraron correctamente en Eureka.

En caso de errores se pueden revisar sus registros:

```powershell
docker compose logs --tail=100
```

## 9. Escalabilidad horizontal

La escalabilidad horizontal consiste en aumentar la cantidad de instancias de un mismo servicio, en lugar de aumentar solamente los recursos de una instancia existente.

Como parte de la evaluación se realizó una prueba de escalabilidad horizontal del microservicio `ms-clientes`.

Inicialmente se ejecutó una sola instancia. Posteriormente se modificó la publicación del puerto en Docker Compose para permitir la ejecución de dos réplicas sin generar un conflicto en el puerto externo.

La configuración utilizada fue:

```yaml
ms-clientes:
  build: ./ms-clientes
  ports:
    - "8091"
```

Con esta configuración, el puerto interno del servicio sigue siendo `8091`, mientras que Docker asigna automáticamente un puerto disponible en el equipo anfitrión para cada réplica.

Para realizar la prueba se ejecutó:

```powershell
docker compose up -d --scale ms-clientes=2
```

Después se comprobó el estado mediante:

```powershell
docker compose ps -a
```

### Resultado obtenido

| Servicio | Cantidad de instancias |
|---|---:|
| `ms-clientes` | 2 |
| `ms-cuentas` | 1 |
| `ms-pagos` | 1 |
| **Total de contenedores** | **4** |

Los cuatro contenedores se encontraron activos durante la comprobación.

También se verificó desde Eureka que las dos instancias de `MS-CLIENTES` estuvieran registradas en estado `UP`.

Esta prueba permitió demostrar la ejecución simultánea de varias réplicas y su reconocimiento mediante Eureka. No se realizó una prueba independiente para medir el balanceo de solicitudes entre ambas instancias.

## 10. Registro y descubrimiento mediante Eureka

Eureka Server se utilizó como mecanismo de registro y descubrimiento de los microservicios.

En el entorno local se accedió a su panel mediante:

```text
http://localhost:8761
```

Inicialmente se verificó la presencia de los tres servicios principales.

Posteriormente, al realizar la prueba de escalabilidad horizontal, se comprobó el registro simultáneo de dos instancias de `MS-CLIENTES`.

Esto permite que el sistema cuente con información sobre las instancias disponibles, aunque el aprovechamiento de esas instancias para distribuir solicitudes depende de la configuración de los componentes consumidores y del mecanismo de balanceo utilizado.

## 11. Integración mediante Apache Kafka

Apache Kafka forma parte de la arquitectura para la comunicación basada en eventos.

Durante las pruebas se utilizó el tópico:

```text
transacciones-completadas
```

El tópico se configuró con tres particiones.

En el entorno Docker local, los microservicios que utilizan Kafka se configuraron para acceder al broker mediante:

```text
kafka-eft:9094
```

Esto permitió separar la dirección de acceso utilizada dentro de Docker de la dirección utilizada por aplicaciones que se ejecutan en Windows.

La configuración de Kafka y la disponibilidad del broker son requisitos necesarios para ejecutar correctamente las funcionalidades que dependen de eventos.

## 12. Consideraciones de seguridad

Para preparar un futuro despliegue es importante evitar que las credenciales queden expuestas en el repositorio.

En el entorno de desarrollo se utilizó una variable `DB_PASSWORD`, definida localmente en `.env`.

El archivo `.gitignore` debe excluir este archivo y otros elementos que contengan información sensible.

Para un entorno productivo también se recomienda:

- Utilizar usuarios de base de datos con permisos limitados.
- Administrar las credenciales mediante un servicio de secretos.
- Restringir los puertos expuestos.
- Configurar conexiones seguras mediante HTTPS/TLS.
- Proteger los endpoints mediante los mecanismos de autenticación y autorización correspondientes.
- Mantener un control de acceso adecuado a los recursos de infraestructura.

Estas recomendaciones corresponden a medidas propuestas para un entorno futuro, no a configuraciones productivas verificadas durante la evaluación.

## 13. Propuesta de infraestructura en AWS

El proyecto quedó preparado a nivel local para considerar un futuro despliegue en AWS.

La contenedorización de los microservicios y la separación de configuraciones facilitan una posible migración a infraestructura en la nube.

Para un despliegue posterior se podría considerar la siguiente arquitectura:

| Servicio AWS | Uso propuesto |
|---|---|
| Amazon ECR | Almacenamiento de imágenes Docker |
| Amazon ECS | Ejecución y administración de contenedores |
| Amazon RDS for MySQL | Base de datos administrada |
| Amazon MSK | Servicio administrado de Apache Kafka |
| Application Load Balancer | Distribución de tráfico hacia los servicios expuestos |
| AWS Secrets Manager | Administración de contraseñas y secretos |
| Amazon CloudWatch | Centralización de registros y métricas |
| Amazon VPC | Administración de la red y controles de acceso |

Estos servicios forman parte de una **propuesta técnica**, no de recursos que se hayan creado durante la evaluación.

### Adaptaciones necesarias

Para llevar la solución a AWS sería necesario:

1. Publicar las imágenes Docker en un registro de imágenes.
2. Definir los servicios y las tareas necesarias para ejecutar los contenedores.
3. Configurar redes, reglas de acceso y puertos.
4. Ajustar las direcciones de MySQL, Kafka, Config Server y Eureka al nuevo entorno.
5. Administrar las credenciales mediante mecanismos seguros.
6. Configurar el balanceo de tráfico y las políticas de escalado cuando corresponda.
7. Incorporar monitoreo, registros y mecanismos de recuperación.
8. Ejecutar pruebas de integración, disponibilidad y seguridad antes de habilitar el entorno.

Estas actividades quedan identificadas como pasos para una implementación futura.

## 14. Diferencia entre implementación local y despliegue en la nube

Es importante distinguir los resultados obtenidos de las actividades propuestas.

| Actividad | Situación |
|---|---|
| Creación de Dockerfile para los tres microservicios | Realizada |
| Construcción de imágenes Docker | Realizada |
| Ejecución de microservicios con Docker Compose | Verificada |
| Integración local con MySQL y Config Server | Verificada durante las pruebas |
| Registro de servicios en Eureka | Verificado |
| Configuración e integración local de Kafka | Probada |
| Ejecución de dos réplicas de `ms-clientes` | Verificada |
| Registro de ambas réplicas en Eureka | Verificado |
| Diseño propuesto para AWS | Documentado |
| Despliegue efectivo en AWS | No realizado |
| Balanceo productivo y autoescalado en AWS | No implementados |

La preparación realizada permite contar con una base para un futuro despliegue, pero todavía sería necesario adaptar y validar la solución dentro de la infraestructura de AWS.

## 15. Evidencias de despliegue

Durante el desarrollo se recopilaron capturas que permiten respaldar las pruebas de Docker Compose y escalabilidad horizontal.

Entre las evidencias relacionadas se encuentran:

- **Evidencia 45:** tres microservicios funcionando mediante Docker Compose.
- **Evidencia 46:** registro de los tres microservicios en Eureka.
- **Evidencia 47:** cuatro contenedores activos, incluyendo dos instancias de `ms-clientes`.
- **Evidencia 48:** registro de ambas réplicas de clientes en Eureka.

Estas evidencias forman parte del respaldo técnico del trabajo y pueden incorporarse al informe final de la evaluación.

## 16. Conclusión

La preparación del despliegue permitió aplicar herramientas de contenedorización y comprobar la ejecución de varios microservicios de manera simultánea.

Durante las pruebas fue necesario corregir configuraciones relacionadas con la conexión a MySQL, el acceso a Config Server y la comunicación entre los componentes del sistema.

Finalmente, se logró ejecutar los tres microservicios principales mediante Docker Compose y realizar una prueba de escalabilidad horizontal con dos instancias del servicio de clientes, ambas reconocidas por Eureka.

Con estos resultados, el proyecto cuenta con una preparación local que puede servir como punto de partida para una futura implementación en AWS, considerando que todavía serían necesarias tareas adicionales de infraestructura, seguridad, balanceo y validación en la nube.