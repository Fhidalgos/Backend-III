# Instrucciones de instalación y ejecución – EFT Backend III

**Estudiante:** Felipe Hidalgo  
**Asignatura:** Desarrollo Backend III  
**Proyecto:** Modernización del sistema bancario Banco XYZ

## 1. Introducción

Este documento explica los requisitos y pasos necesarios para preparar el entorno local y ejecutar los componentes desarrollados en la Evaluación Final Transversal de Backend III.

El sistema está organizado en diferentes aplicaciones Spring Boot, incluyendo procesamiento con Spring Batch, microservicios, servicios BFF, autenticación, configuración centralizada y descubrimiento de servicios.

También se incluyen las instrucciones para ejecutar los microservicios mediante Docker Compose y verificar su funcionamiento.

## 2. Requisitos previos

Para trabajar con el proyecto se necesita contar con las siguientes herramientas:

| Herramienta | Uso |
|---|---|
| Java JDK 17 | Ejecutar las aplicaciones Java |
| Maven o Maven Wrapper | Compilar los proyectos |
| MySQL Server | Almacenar la información |
| Docker Desktop | Ejecutar contenedores |
| Docker Compose | Administrar los microservicios |
| Apache Kafka | Intercambiar eventos entre servicios |
| Postman | Realizar pruebas a los endpoints |
| Git | Descargar y administrar el código |

También es necesario contar con puertos disponibles y los permisos correspondientes para ejecutar los servicios.

## 3. Descargar el proyecto

El repositorio principal se encuentra en:

https://github.com/Fhidalgos/Backend-III

Para descargarlo mediante Git:

```powershell
git clone https://github.com/Fhidalgos/Backend-III.git
```

Una vez publicada la rama de la EFT, se podrá acceder a sus archivos mediante:

```powershell
cd Backend-III
git switch eft-backend-iii
```

La carpeta de trabajo de esta evaluación contiene los diferentes componentes del sistema y el archivo `docker-compose.yml`.

## 4. Preparación de MySQL

Antes de ejecutar los servicios que utilizan persistencia, es necesario iniciar MySQL Server.

Los microservicios utilizan bases de datos separadas:

- `eft_clientes`
- `eft_cuentas`
- `eft_pagos`

En la configuración local de Docker Compose se utiliza el puerto `3306` para conectarse a MySQL.

Es necesario configurar un usuario con permisos sobre las bases de datos correspondientes. Para el entorno de desarrollo se utilizó una conexión local, pero en un entorno compartido o productivo se recomienda utilizar un usuario de aplicación con permisos limitados.

Las credenciales deben mantenerse fuera del repositorio.

## 5. Configuración de las variables de entorno

En la carpeta principal del proyecto se debe crear un archivo llamado `.env`.

Su contenido debe seguir esta estructura:

```dotenv
DB_PASSWORD=CONTRASEÑA_MYSQL_LOCAL
```

Se debe reemplazar `CONTRASEÑA_MYSQL_LOCAL` por la contraseña correspondiente al entorno.

Este archivo es utilizado por Docker Compose para proporcionar la variable de entorno `DB_PASSWORD` a los microservicios.

**Importante:** el archivo `.env` no debe subirse a GitHub, ya que contiene información sensible. Debe quedar incluido en `.gitignore`.

En caso de utilizar otro usuario de MySQL, también será necesario ajustar el nombre de usuario en la configuración correspondiente.

## 6. Preparación de los servicios de infraestructura

Para el funcionamiento completo de los componentes es necesario disponer de los servicios de infraestructura utilizados durante el desarrollo.

### 6.1. Config Server

El componente `config-server` permite obtener configuraciones centralizadas desde `config-repo`.

Durante las pruebas locales se utilizó:

```text
http://localhost:8888
```

Los microservicios ejecutados en Docker utilizan la dirección `host.docker.internal` para acceder al Config Server que se encuentra ejecutándose en Windows.

### 6.2. Eureka Server

El componente `eureka-server` permite registrar y descubrir los microservicios.

Durante las pruebas locales se utilizó:

```text
http://localhost:8761
```

El panel de Eureka permite comprobar si los microservicios aparecen registrados y disponibles.

### 6.3. Apache Kafka

Kafka se utiliza para la comunicación mediante eventos.

Durante las pruebas se utilizó el tópico:

```text
transacciones-completadas
```

Este tópico fue configurado con tres particiones.

En el entorno Docker utilizado durante la evaluación, Kafka estuvo disponible dentro de la red de contenedores mediante:

```text
kafka-eft:9094
```

Para las aplicaciones que se ejecutan directamente en Windows se utilizó la conexión local correspondiente.

La configuración del broker debe estar preparada antes de iniciar los componentes que publican o consumen eventos.

## 7. Compilación de los microservicios

Los microservicios principales son:

- `ms-clientes`
- `ms-cuentas`
- `ms-pagos`

Cada uno debe contar con su archivo JAR compilado antes de construir la imagen Docker.

Desde cada carpeta, utilizando Maven, se puede ejecutar:

```powershell
mvn clean package -DskipTests
```

Este comando genera los archivos compilados en la carpeta `target`.

La opción `-DskipTests` omite las pruebas automatizadas durante la compilación. Para validar el proyecto mediante sus pruebas, se debe ejecutar la compilación sin esa opción.

Si el módulo dispone de Maven Wrapper, también se puede utilizar `.\mvnw.cmd` en Windows.

Los archivos Dockerfile utilizan los JAR generados para crear las imágenes de los microservicios.

## 8. Ejecución con Docker Compose

Antes de utilizar Docker Compose se debe comprobar que:

- Docker Desktop esté funcionando.
- MySQL esté disponible.
- Config Server esté ejecutándose.
- Eureka Server esté ejecutándose.
- Kafka esté disponible y accesible.
- El archivo `.env` esté configurado.
- Los JAR de los microservicios estén compilados.
- La red Docker `eft-network` exista.

Si la red no existe, se puede crear mediante:

```powershell
docker network create eft-network
```

Desde la carpeta principal del proyecto, ejecutar:

```powershell
docker compose up -d --build
```

Este comando construye las imágenes necesarias y levanta los contenedores configurados.

Para revisar su estado:

```powershell
docker compose ps -a
```

Durante las pruebas se comprobó el funcionamiento simultáneo de:

- `ms-clientes`
- `ms-cuentas`
- `ms-pagos`

Para revisar los registros de ejecución:

```powershell
docker compose logs --tail=100
```

Si un contenedor se detiene, estos registros permiten revisar posibles problemas de configuración, conexión o autenticación.

## 9. Verificación en Eureka

Una vez iniciados los microservicios, ingresar desde el navegador a:

```text
http://localhost:8761
```

En la sección de instancias registradas se deben revisar los servicios:

```text
MS-CLIENTES
MS-CUENTAS
MS-PAGOS
```

Durante la evaluación se comprobó que los tres servicios podían registrarse correctamente en Eureka.

## 10. Prueba de escalabilidad horizontal

El proyecto incluye una configuración de Docker Compose que permite ejecutar más de una instancia del microservicio de clientes.

Para realizar esta prueba:

```powershell
docker compose up -d --scale ms-clientes=2
```

Después, comprobar los contenedores:

```powershell
docker compose ps -a
```

El resultado esperado es encontrar cuatro contenedores:

- Dos instancias de `ms-clientes`.
- Una instancia de `ms-cuentas`.
- Una instancia de `ms-pagos`.

Como el puerto externo de `ms-clientes` se asigna automáticamente, cada réplica puede utilizar un puerto diferente en el equipo anfitrión.

Finalmente, se puede revisar Eureka para comprobar el registro de las dos instancias.

En las pruebas realizadas se logró visualizar `MS-CLIENTES` con dos instancias activas.

## 11. Ejecución de los demás componentes

Además de los tres microservicios, el proyecto contiene otros componentes independientes:

- `auth-server`
- `batch-service`
- `bff-web`
- `bff-movil`
- `bff-cajero`

Estos componentes deben ejecutarse desde sus respectivas carpetas, con las dependencias y variables de configuración necesarias.

En los módulos Spring Boot que utilizan Maven se puede ejecutar:

```powershell
mvn spring-boot:run
```

También puede utilizarse Maven Wrapper cuando esté disponible.

El servicio de autenticación y los servicios de infraestructura necesarios deben encontrarse operativos antes de realizar pruebas que dependan de ellos.

Los puertos y parámetros particulares deben consultarse en los archivos de configuración de cada módulo.

## 12. Pruebas mediante Postman

Postman permite enviar solicitudes HTTP a los diferentes endpoints del sistema.

Durante la evaluación se realizaron pruebas de:

- Autenticación y autorización.
- Acceso a los BFF web, móvil y cajero.
- Operaciones de clientes y cuentas.
- Validación de datos.
- Respuestas ante solicitudes sin autorización.
- Comportamiento ante fallos de servicios.
- Integración de componentes.

Para los endpoints protegidos se debe incluir un token válido según la configuración de seguridad del sistema.

Es importante verificar tanto el código HTTP de respuesta como el contenido devuelto por cada operación.

## 13. Ejecución de Spring Batch

El componente `batch-service` contiene los procesos por lotes desarrollados para la evaluación.

Entre ellos se encuentran:

1. Procesamiento de transacciones.
2. Cálculo de intereses mensuales.
3. Generación de estados de cuenta anuales.

Cada Job debe ejecutarse con los parámetros y datos de entrada que correspondan a su implementación.

Las pruebas del proyecto incluyeron mecanismos de reintento, omisión de registros inválidos, reejecución segura y procesamiento paralelo.

Los resultados se pueden revisar mediante los registros de ejecución y las tablas correspondientes en MySQL.

## 14. Solución de problemas frecuentes

### Los contenedores se detienen

Revisar su estado:

```powershell
docker compose ps -a
```

Consultar los registros:

```powershell
docker compose logs --tail=100
```

Comprobar especialmente las conexiones a MySQL, Config Server y Kafka.

### MySQL rechaza las credenciales

Verificar que la contraseña declarada en `.env` coincida con la configurada en MySQL.

También es necesario comprobar que el usuario tenga permisos sobre las bases de datos correspondientes.

### Los microservicios no aparecen en Eureka

Comprobar que Eureka esté disponible en el puerto `8761` y que los microservicios se encuentren funcionando.

Si se ejecutan desde Docker, revisar que la dirección configurada utilice el acceso adecuado al servidor anfitrión.

### Kafka no está disponible

Comprobar que el broker esté funcionando, que el tópico correspondiente exista y que los servicios puedan acceder a la dirección configurada.

## 15. Consideraciones finales

La ejecución del proyecto depende de contar con los servicios de infraestructura correctamente configurados.

Durante la evaluación se comprobó el funcionamiento local de los microservicios mediante Docker Compose, su registro en Eureka y la replicación horizontal del servicio de clientes.

El sistema quedó preparado para considerar un futuro despliegue en la nube. La evaluación no contempla afirmar que el sistema completo haya sido desplegado efectivamente en AWS.

Los aspectos relacionados con contenedores, infraestructura, escalabilidad y preparación para un despliegue futuro se describen en `despliegue.md`.