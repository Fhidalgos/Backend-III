# Propuesta Técnica

## Sistema de transferencias bancarias basado en microservicios

### 1. Introducción

La presente propuesta técnica describe la arquitectura implementada para un sistema de transferencias bancarias basado en microservicios.

La solución busca mejorar la seguridad, disponibilidad y desacoplamiento entre los servicios mediante el uso de Spring Boot, Spring Cloud, OAuth2, JWT, Apache Kafka, Resilience4j y Docker.

El sistema permite recibir solicitudes de transferencia, procesarlas de forma asíncrona y generar una notificación una vez finalizado el procesamiento.

### 2. Objetivo de la propuesta

El objetivo es implementar una solución basada en microservicios que permita procesar transferencias bancarias de forma segura, resiliente y desacoplada.

Para lograrlo se propone:

- Utilizar OAuth2 y JWT para proteger el acceso al servicio de transferencias.
- Utilizar Apache Kafka para la comunicación asíncrona entre los microservicios.
- Incorporar Resilience4j para manejar fallas en el servicio de notificaciones.
- Utilizar Eureka Server para el registro y descubrimiento de servicios.
- Centralizar la configuración mediante Spring Cloud Config Server.
- Dockerizar los componentes y administrarlos mediante Docker Compose.

### 3. Arquitectura propuesta

La solución se basa en una arquitectura de microservicios orientada a eventos.

Cada componente cumple una función específica dentro del sistema, permitiendo mantener responsabilidades separadas y reducir el acoplamiento entre los servicios.

El flujo principal es el siguiente:

```text
Cliente / Postman
       |
       v
Auth Server
   OAuth2 + JWT
       |
       v
ms-transferencias
       |
       v
Apache Kafka
transferencias-solicitadas
       |
       v
ms-cuentas
       |
       v
Apache Kafka
transferencias-resultados
       |
       v
ms-notificaciones
```

Los principales componentes son:

- **Auth Server:** genera los tokens OAuth2 y JWT utilizados para acceder al servicio de transferencias.

- **ms-transferencias:** recibe las solicitudes realizadas por el cliente, valida el acceso mediante JWT y publica el evento `TRANSFERENCIA_SOLICITADA` en Kafka.

- **ms-cuentas:** consume los eventos enviados desde `ms-transferencias`, procesa la transferencia y publica el resultado mediante el evento `TRANSFERENCIA_PROCESADA`.

- **ms-notificaciones:** consume el resultado de la transferencia y genera la notificación correspondiente. En este microservicio también se implementa Resilience4j para manejar fallas controladas.

- **Apache Kafka:** permite la comunicación asíncrona entre los microservicios utilizando los tópicos `transferencias-solicitadas` y `transferencias-resultados`.

- **Eureka Server:** permite registrar y descubrir los microservicios disponibles.

- **Config Server:** centraliza las configuraciones utilizadas por los diferentes servicios.

- **Docker Compose:** permite levantar y administrar todos los componentes de la arquitectura desde un único archivo de configuración.

### 4. Seguridad con OAuth2 y JWT

La seguridad del sistema se implementa mediante OAuth2 y JWT.

El `Auth Server` es responsable de generar los tokens de acceso utilizando el flujo `client_credentials`.

Para solicitar un token se utiliza el cliente:

```text
backend-client
```

El token puede incluir los siguientes permisos:

```text
transferencias.read
transferencias.write
```

El microservicio `ms-transferencias` funciona como Resource Server y valida los JWT recibidos antes de permitir el acceso al endpoint de transferencias.

El endpoint protegido es:

```text
POST /api/transferencias
```

Para crear una transferencia, el token debe contener el permiso:

```text
SCOPE_transferencias.write
```

Durante las pruebas se comprobó el comportamiento esperado:

```text
Solicitud sin token válido → 401 Unauthorized
Solicitud con token válido → 202 Accepted
```

De esta forma, el acceso al servicio de transferencias queda restringido a clientes autenticados y con los permisos correspondientes.

### 5. Comunicación asíncrona con Apache Kafka

La comunicación entre los microservicios se realiza de forma asíncrona mediante Apache Kafka.

Se utilizan dos tópicos principales:

```text
transferencias-solicitadas
transferencias-resultados
```

El flujo de comunicación es el siguiente:

```text
ms-transferencias
       |
       | publica TRANSFERENCIA_SOLICITADA
       v
transferencias-solicitadas
       |
       v
ms-cuentas
       |
       | publica TRANSFERENCIA_PROCESADA
       v
transferencias-resultados
       |
       v
ms-notificaciones
```

`ms-transferencias` publica la solicitud de transferencia en Kafka.

`ms-cuentas` consume la solicitud, procesa la información y publica el resultado en el segundo tópico.

Finalmente, `ms-notificaciones` consume el resultado y genera la notificación correspondiente.

Este enfoque permite reducir el acoplamiento entre los microservicios, ya que no necesitan comunicarse directamente mediante llamadas REST para completar el flujo principal.

### 6. Resiliencia con Resilience4j

El microservicio `ms-notificaciones` incorpora Resilience4j para manejar fallas controladas durante el proceso de generación de notificaciones.

En condiciones normales, cuando la notificación puede procesarse correctamente, el sistema muestra:

```text
Notificación generada correctamente
```

Para comprobar el comportamiento ante una falla, se utiliza la propiedad:

```properties
app.notificaciones.simular-fallo=true
```

Cuando esta configuración está activa, el servicio genera una excepción controlada y Resilience4j ejecuta el mecanismo de fallback.

El mensaje obtenido durante la prueba fue:

```text
FALLBACK ACTIVADO: la notificación quedó pendiente
```

Una vez finalizada la prueba de resiliencia, la propiedad se restablece a:

```properties
app.notificaciones.simular-fallo=false
```

De esta forma, el sistema puede manejar fallas en el servicio de notificaciones sin interrumpir por completo el flujo principal de la aplicación.

### 7. Dockerización y orquestación con Docker Compose

Los microservicios y servicios de infraestructura fueron preparados para ejecutarse mediante contenedores Docker.

Se construyeron imágenes Docker para los siguientes componentes:

```text
eureka-server:1.0
config-server:1.0
auth-server:1.0
ms-cuentas:1.0
ms-notificaciones:1.0
ms-transferencias:1.0
```

Apache Kafka utiliza la imagen:

```text
apache/kafka:4.1.0
```

Cada aplicación Java cuenta con su propio `Dockerfile`, encargado de compilar el proyecto mediante Maven y posteriormente ejecutar el archivo `.jar` generado.

La orquestación de todos los componentes se realiza mediante el archivo:

```text
docker-compose.yaml
```

Docker Compose permite levantar la arquitectura completa mediante:

```powershell
docker compose up -d
```

Los componentes ejecutados son:

```text
Kafka
Eureka Server
Config Server
Auth Server
ms-cuentas
ms-notificaciones
ms-transferencias
```

Dentro de la red creada por Docker Compose, los servicios se comunican utilizando los nombres de los contenedores.

Por ejemplo:

```text
Config Server → config-server:8888
Eureka Server → eureka-server:8761
Kafka         → kafka:9092
Auth Server   → auth-server:9000
```

Para mantener compatibilidad entre la ejecución local y Docker, los microservicios utilizan una variable para definir la dirección del Config Server:

```properties
spring.config.import=configserver:${CONFIG_SERVER_URL:http://localhost:8888}
```

En Docker Compose se utiliza:

```text
CONFIG_SERVER_URL=http://config-server:8888
```

Esto permite que el mismo código pueda ejecutarse tanto de forma local como dentro de contenedores.

Finalmente, mediante el comando:

```powershell
docker compose ps
```

se comprobó que todos los componentes permanecieran en estado `Up`, confirmando el correcto funcionamiento de la orquestación.

### 8. Pruebas realizadas y resultados

Para comprobar el correcto funcionamiento de la arquitectura se realizaron distintas pruebas sobre seguridad, resiliencia, comunicación asíncrona y ejecución mediante Docker.

#### Prueba de autenticación OAuth2

Se solicitó un token al `Auth Server` mediante el flujo `client_credentials`.

Resultado obtenido:

```text
200 OK
```

El servidor entregó correctamente un `access_token` con los permisos:

```text
transferencias.read
transferencias.write
```

#### Prueba sin token

Se realizó una solicitud al endpoint:

```text
POST /api/transferencias
```

sin enviar un token OAuth2.

Resultado obtenido:

```text
401 Unauthorized
```

Esto permitió comprobar que el endpoint se encuentra protegido.

#### Prueba con token válido

Posteriormente se realizó la misma solicitud utilizando un token JWT válido con el permiso `transferencias.write`.

Resultado obtenido:

```text
202 Accepted
Transferencia recibida y enviada a procesamiento.
```

#### Prueba de comunicación con Kafka

Se verificó que `ms-transferencias` publicara correctamente el evento:

```text
TRANSFERENCIA_SOLICITADA
```

Luego, `ms-cuentas` recibió la solicitud y publicó:

```text
TRANSFERENCIA_PROCESADA
```

Finalmente, `ms-notificaciones` recibió el resultado y generó correctamente la notificación.

El flujo comprobado fue:

```text
ms-transferencias
       ↓
Kafka
       ↓
ms-cuentas
       ↓
Kafka
       ↓
ms-notificaciones
```

#### Prueba de Resilience4j

Con el servicio funcionando normalmente se obtuvo:

```text
Notificación generada correctamente
```

Luego se simuló una falla controlada y se comprobó la activación del fallback:

```text
FALLBACK ACTIVADO: la notificación quedó pendiente
```

#### Prueba de Eureka Server

Se comprobó que los servicios quedaran registrados correctamente en Eureka:

```text
AUTH-SERVER
MS-CUENTAS
MS-NOTIFICACIONES
MS-TRANSFERENCIAS
```

Todos se visualizaron con estado:

```text
UP
```

#### Prueba de Docker Compose

Finalmente, se levantó la arquitectura completa mediante:

```powershell
docker compose up -d
```

y se verificó su estado mediante:

```powershell
docker compose ps
```

Los siete componentes permanecieron en ejecución:

```text
Kafka
Eureka Server
Config Server
Auth Server
ms-cuentas
ms-notificaciones
ms-transferencias
```

Con estas pruebas se comprobó el funcionamiento integrado de seguridad, resiliencia, comunicación asíncrona y orquestación mediante contenedores.

### 9. Conclusión

La solución desarrollada permite implementar un sistema de transferencias bancarias utilizando una arquitectura de microservicios segura, resiliente y desacoplada.

La incorporación de OAuth2 y JWT permite proteger el acceso al servicio de transferencias, mientras que Apache Kafka permite realizar la comunicación entre los microservicios de forma asíncrona.

Además, Resilience4j permite manejar fallas controladas en el proceso de notificaciones, evitando que un problema en este servicio detenga completamente el flujo de la aplicación.

El uso de Eureka Server y Config Server permite organizar el descubrimiento y la configuración de los servicios, mientras que Docker y Docker Compose facilitan la ejecución conjunta de toda la arquitectura.

Finalmente, las pruebas realizadas permitieron comprobar que los diferentes componentes funcionan de manera integrada, desde la autenticación del cliente hasta el procesamiento de la transferencia y la generación de la notificación.
