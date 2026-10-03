# Propuesta TÃ©cnica

## Sistema de transferencias bancarias basado en microservicios

### 1. IntroducciÃ³n

La presente propuesta tÃ©cnica describe la arquitectura implementada para un sistema de transferencias bancarias basado en microservicios.

La soluciÃ³n busca mejorar la seguridad, disponibilidad y desacoplamiento entre los servicios mediante el uso de Spring Boot, Spring Cloud, OAuth2, JWT, Apache Kafka, Resilience4j y Docker.

El sistema permite recibir solicitudes de transferencia, procesarlas de forma asÃ­ncrona y generar una notificaciÃ³n una vez finalizado el procesamiento.

### 2. Objetivo de la propuesta

El objetivo es implementar una soluciÃ³n basada en microservicios que permita procesar transferencias bancarias de forma segura, resiliente y desacoplada.

Para lograrlo se propone:

- Utilizar OAuth2 y JWT para proteger el acceso al servicio de transferencias.
- Utilizar Apache Kafka para la comunicaciÃ³n asÃ­ncrona entre los microservicios.
- Incorporar Resilience4j para manejar fallas en el servicio de notificaciones.
- Utilizar Eureka Server para el registro y descubrimiento de servicios.
- Centralizar la configuraciÃ³n mediante Spring Cloud Config Server.
- Dockerizar los componentes y administrarlos mediante Docker Compose.

### 3. Arquitectura propuesta

La soluciÃ³n se basa en una arquitectura de microservicios orientada a eventos.

Cada componente cumple una funciÃ³n especÃ­fica dentro del sistema, permitiendo mantener responsabilidades separadas y reducir el acoplamiento entre los servicios.

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

- **ms-notificaciones:** consume el resultado de la transferencia y genera la notificaciÃ³n correspondiente. En este microservicio tambiÃ©n se implementa Resilience4j para manejar fallas controladas.

- **Apache Kafka:** permite la comunicaciÃ³n asÃ­ncrona entre los microservicios utilizando los tÃ³picos `transferencias-solicitadas` y `transferencias-resultados`.

- **Eureka Server:** permite registrar y descubrir los microservicios disponibles.

- **Config Server:** centraliza las configuraciones utilizadas por los diferentes servicios.

- **Docker Compose:** permite levantar y administrar todos los componentes de la arquitectura desde un Ãºnico archivo de configuraciÃ³n.

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

Durante las pruebas se comprobÃ³ el comportamiento esperado:

```text
Solicitud sin token vÃ¡lido â†’ 401 Unauthorized
Solicitud con token vÃ¡lido â†’ 202 Accepted
```

De esta forma, el acceso al servicio de transferencias queda restringido a clientes autenticados y con los permisos correspondientes.

### 5. ComunicaciÃ³n asÃ­ncrona con Apache Kafka

La comunicaciÃ³n entre los microservicios se realiza de forma asÃ­ncrona mediante Apache Kafka.

Se utilizan dos tÃ³picos principales:

```text
transferencias-solicitadas
transferencias-resultados
```

El flujo de comunicaciÃ³n es el siguiente:

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

`ms-cuentas` consume la solicitud, procesa la informaciÃ³n y publica el resultado en el segundo tÃ³pico.

Finalmente, `ms-notificaciones` consume el resultado y genera la notificaciÃ³n correspondiente.

Este enfoque permite reducir el acoplamiento entre los microservicios, ya que no necesitan comunicarse directamente mediante llamadas REST para completar el flujo principal.

### 6. Resiliencia con Resilience4j

El microservicio `ms-notificaciones` incorpora Resilience4j para manejar fallas controladas durante el proceso de generaciÃ³n de notificaciones.

En condiciones normales, cuando la notificaciÃ³n puede procesarse correctamente, el sistema muestra:

```text
NotificaciÃ³n generada correctamente
```

Para comprobar el comportamiento ante una falla, se utiliza la propiedad:

```properties
app.notificaciones.simular-fallo=true
```

Cuando esta configuraciÃ³n estÃ¡ activa, el servicio genera una excepciÃ³n controlada y Resilience4j ejecuta el mecanismo de fallback.

El mensaje obtenido durante la prueba fue:

```text
FALLBACK ACTIVADO: la notificaciÃ³n quedÃ³ pendiente
```

Una vez finalizada la prueba de resiliencia, la propiedad se restablece a:

```properties
app.notificaciones.simular-fallo=false
```

De esta forma, el sistema puede manejar fallas en el servicio de notificaciones sin interrumpir por completo el flujo principal de la aplicaciÃ³n.

### 7. DockerizaciÃ³n y orquestaciÃ³n con Docker Compose

Los microservicios y servicios de infraestructura fueron preparados para ejecutarse mediante contenedores Docker.

Se construyeron imÃ¡genes Docker para los siguientes componentes:

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

Cada aplicaciÃ³n Java cuenta con su propio `Dockerfile`, encargado de compilar el proyecto mediante Maven y posteriormente ejecutar el archivo `.jar` generado.

La orquestaciÃ³n de todos los componentes se realiza mediante el archivo:

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
Config Server â†’ config-server:8888
Eureka Server â†’ eureka-server:8761
Kafka         â†’ kafka:9092
Auth Server   â†’ auth-server:9000
```

Para mantener compatibilidad entre la ejecuciÃ³n local y Docker, los microservicios utilizan una variable para definir la direcciÃ³n del Config Server:

```properties
spring.config.import=configserver:${CONFIG_SERVER_URL:http://localhost:8888}
```

En Docker Compose se utiliza:

```text
CONFIG_SERVER_URL=http://config-server:8888
```

Esto permite que el mismo cÃ³digo pueda ejecutarse tanto de forma local como dentro de contenedores.

Finalmente, mediante el comando:

```powershell
docker compose ps
```

se comprobÃ³ que todos los componentes permanecieran en estado `Up`, confirmando el correcto funcionamiento de la orquestaciÃ³n.

### 8. Pruebas realizadas y resultados

Para comprobar el correcto funcionamiento de la arquitectura se realizaron distintas pruebas sobre seguridad, resiliencia, comunicaciÃ³n asÃ­ncrona y ejecuciÃ³n mediante Docker.

#### Prueba de autenticaciÃ³n OAuth2

Se solicitÃ³ un token al `Auth Server` mediante el flujo `client_credentials`.

Resultado obtenido:

```text
200 OK
```

El servidor entregÃ³ correctamente un `access_token` con los permisos:

```text
transferencias.read
transferencias.write
```

#### Prueba sin token

Se realizÃ³ una solicitud al endpoint:

```text
POST /api/transferencias
```

sin enviar un token OAuth2.

Resultado obtenido:

```text
401 Unauthorized
```

Esto permitiÃ³ comprobar que el endpoint se encuentra protegido.

#### Prueba con token vÃ¡lido

Posteriormente se realizÃ³ la misma solicitud utilizando un token JWT vÃ¡lido con el permiso `transferencias.write`.

Resultado obtenido:

```text
202 Accepted
Transferencia recibida y enviada a procesamiento.
```

#### Prueba de comunicaciÃ³n con Kafka

Se verificÃ³ que `ms-transferencias` publicara correctamente el evento:

```text
TRANSFERENCIA_SOLICITADA
```

Luego, `ms-cuentas` recibiÃ³ la solicitud y publicÃ³:

```text
TRANSFERENCIA_PROCESADA
```

Finalmente, `ms-notificaciones` recibiÃ³ el resultado y generÃ³ correctamente la notificaciÃ³n.

El flujo comprobado fue:

```text
ms-transferencias
       â†“
Kafka
       â†“
ms-cuentas
       â†“
Kafka
       â†“
ms-notificaciones
```

#### Prueba de Resilience4j

Con el servicio funcionando normalmente se obtuvo:

```text
NotificaciÃ³n generada correctamente
```

Luego se simulÃ³ una falla controlada y se comprobÃ³ la activaciÃ³n del fallback:

```text
FALLBACK ACTIVADO: la notificaciÃ³n quedÃ³ pendiente
```

#### Prueba de Eureka Server

Se comprobÃ³ que los servicios quedaran registrados correctamente en Eureka:

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

Finalmente, se levantÃ³ la arquitectura completa mediante:

```powershell
docker compose up -d
```

y se verificÃ³ su estado mediante:

```powershell
docker compose ps
```

Los siete componentes permanecieron en ejecuciÃ³n:

```text
Kafka
Eureka Server
Config Server
Auth Server
ms-cuentas
ms-notificaciones
ms-transferencias
```

Con estas pruebas se comprobÃ³ el funcionamiento integrado de seguridad, resiliencia, comunicaciÃ³n asÃ­ncrona y orquestaciÃ³n mediante contenedores.

### 9. ConclusiÃ³n

La soluciÃ³n desarrollada permite implementar un sistema de transferencias bancarias utilizando una arquitectura de microservicios segura, resiliente y desacoplada.

La incorporaciÃ³n de OAuth2 y JWT permite proteger el acceso al servicio de transferencias, mientras que Apache Kafka permite realizar la comunicaciÃ³n entre los microservicios de forma asÃ­ncrona.

AdemÃ¡s, Resilience4j permite manejar fallas controladas en el proceso de notificaciones, evitando que un problema en este servicio detenga completamente el flujo de la aplicaciÃ³n.

El uso de Eureka Server y Config Server permite organizar el descubrimiento y la configuraciÃ³n de los servicios, mientras que Docker y Docker Compose facilitan la ejecuciÃ³n conjunta de toda la arquitectura.

Finalmente, las pruebas realizadas permitieron comprobar que los diferentes componentes funcionan de manera integrada, desde la autenticaciÃ³n del cliente hasta el procesamiento de la transferencia y la generaciÃ³n de la notificaciÃ³n.
