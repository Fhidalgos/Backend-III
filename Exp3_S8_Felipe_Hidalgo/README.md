# Exp 3 - Semana 8 - Desarrollo Backend III

## Sistema de transferencias bancarias con microservicios

Este proyecto corresponde a la Experiencia 3 de la Semana 8 de la asignatura Desarrollo Backend III.

La solución implementa una arquitectura basada en microservicios para procesar transferencias bancarias de forma segura y asíncrona. Para ello se utilizan Spring Boot, Spring Cloud, OAuth2, JWT, Apache Kafka, Resilience4j, Eureka Server, Config Server y Docker Compose.

## Objetivo

El objetivo del proyecto es implementar una arquitectura de microservicios segura, resiliente y desacoplada.

La solución permite:

- Proteger el servicio de transferencias mediante OAuth2 y JWT.
- Procesar transferencias mediante comunicación asíncrona con Apache Kafka.
- Registrar y descubrir los microservicios mediante Eureka Server.
- Centralizar la configuración mediante Spring Cloud Config Server.
- Aplicar tolerancia a fallos mediante Resilience4j.
- Dockerizar los microservicios y ejecutarlos de forma conjunta mediante Docker Compose.

## Arquitectura del sistema

La solución utiliza una arquitectura de microservicios orientada a eventos.

El flujo principal de una transferencia es el siguiente:

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
Kafka
transferencias-solicitadas
       |
       v
ms-cuentas
       |
       v
Kafka
transferencias-resultados
       |
       v
ms-notificaciones
```

Además, los microservicios utilizan:

- **Eureka Server:** registro y descubrimiento de servicios.
- **Config Server:** configuración centralizada.
- **Auth Server:** generación de tokens OAuth2 y JWT.
- **Apache Kafka:** comunicación asíncrona entre los microservicios.
- **Resilience4j:** tolerancia a fallos en el servicio de notificaciones.
- **Docker Compose:** ejecución y orquestación de todos los componentes.

## Estructura del proyecto

```text
Exp3_S8_Felipe_Hidalgo/
│
├── auth-server/
│   └── Servidor de autorización OAuth2
│
├── config-server/
│   └── Servidor de configuración centralizada
│
├── eureka-server/
│   └── Servidor de descubrimiento de servicios
│
├── ms-cuentas/
│   └── Procesa las transferencias recibidas desde Kafka
│
├── ms-notificaciones/
│   └── Recibe los resultados y genera las notificaciones
│
├── ms-transferencias/
│   └── Recibe las solicitudes de transferencia y publica eventos
│
├── evidencias/
│   └── Capturas de funcionamiento del proyecto
│
├── docker-compose.yaml
├── PROPUESTA_TECNICA.md
└── README.md
```

## Ejecución del proyecto con Docker Compose

### Requisitos

Para ejecutar el proyecto se necesita:

- Docker Desktop.
- Postman.
- Puertos 8081, 8082, 8083, 8761, 8888, 9000 y 9092 disponibles.

### Levantar la arquitectura

Desde la carpeta raíz del proyecto ejecutar:

```powershell
docker compose up -d
```

Este comando inicia los siguientes componentes:

- Apache Kafka.
- Eureka Server.
- Config Server.
- Auth Server.
- ms-cuentas.
- ms-notificaciones.
- ms-transferencias.

Para comprobar que los contenedores están funcionando:

```powershell
docker compose ps
```

Todos los servicios deben aparecer con estado `Up`.

### Verificar Eureka Server

Abrir en el navegador:

```text
http://localhost:8761
```

Deben aparecer registrados:

```text
AUTH-SERVER
MS-CUENTAS
MS-NOTIFICACIONES
MS-TRANSFERENCIAS
```

### Detener la arquitectura

Para detener y eliminar los contenedores:

```powershell
docker compose down
```

## Seguridad con OAuth2 y JWT

El servicio `ms-transferencias` está protegido mediante OAuth2 y JWT.

Para realizar una transferencia primero se debe obtener un token desde el Auth Server.

### Obtener token

En Postman crear una solicitud:

```text
POST http://localhost:9000/oauth2/token
```

En la pestaña **Authorization** seleccionar **Basic Auth**.

```text
Username: backend-client
Password: backend3-secret
```

En **Body → x-www-form-urlencoded** seleccionar **Bulk Edit** y pegar:

```text
grant_type:client_credentials
scope:transferencias.read transferencias.write
```

Luego se puede volver a la vista normal de `x-www-form-urlencoded` para verificar que Postman separó correctamente ambos campos.

Si la autenticación es correcta, el servidor responde con estado:

```text
200 OK
```

y entrega un `access_token`.

### Realizar una transferencia

Crear una solicitud:

```text
POST http://localhost:8083/api/transferencias
```

En **Authorization** seleccionar **Bearer Token** y pegar el `access_token` generado anteriormente.

En **Body → raw → JSON** utilizar, por ejemplo:

```json
{
  "cuentaOrigen": 701,
  "cuentaDestino": 801,
  "monto": 25000
}
```

Si el token es válido y contiene el permiso `transferencias.write`, la respuesta esperada es:

```text
202 Accepted
Transferencia recibida y enviada a procesamiento.
```

Si se intenta realizar la solicitud sin token, el servicio responde:

```text
401 Unauthorized
```

De esta forma, el servicio de transferencias queda protegido y solamente puede ser utilizado mediante un token OAuth2 válido.

## Comunicación asíncrona con Apache Kafka

La comunicación entre los microservicios se realiza de forma asíncrona mediante Apache Kafka.

El flujo es el siguiente:

```text
ms-transferencias
       |
       v
transferencias-solicitadas
       |
       v
ms-cuentas
       |
       v
transferencias-resultados
       |
       v
ms-notificaciones
```

`ms-transferencias` publica un evento con los datos de la transferencia.

`ms-cuentas` consume ese evento, procesa la transferencia y publica un nuevo evento con el resultado.

Finalmente, `ms-notificaciones` consume el resultado y genera la notificación correspondiente.

Esta comunicación permite mantener los microservicios desacoplados y evita dependencias REST directas entre ellos.

## Resiliencia con Resilience4j

El microservicio `ms-notificaciones` utiliza Resilience4j para aplicar tolerancia a fallos.

Cuando el servicio de notificaciones funciona normalmente, se genera el mensaje:

```text
Notificación generada correctamente
```

Para probar el mecanismo de resiliencia se puede simular una falla mediante la configuración:

```properties
app.notificaciones.simular-fallo=true
```

Cuando se produce la falla, Resilience4j activa el fallback y muestra:

```text
FALLBACK ACTIVADO: la notificación quedó pendiente
```

Después de realizar la prueba, la configuración debe volver a:

```properties
app.notificaciones.simular-fallo=false
```

De esta forma, el sistema puede continuar operando aunque el servicio de notificaciones presente una falla controlada.

## Evidencias

Las capturas utilizadas para demostrar el funcionamiento se encuentran en la carpeta:

```text
evidencias/
```

Entre las principales evidencias se encuentran:

- Generación correcta del token OAuth2.
- Solicitud sin token con respuesta `401 Unauthorized`.
- Solicitud con token válido con respuesta `202 Accepted`.
- Funcionamiento normal de Resilience4j.
- Activación del fallback de Resilience4j.
- Imágenes Docker de los microservicios.
- Servicios registrados en Eureka.
- Contenedores levantados mediante Docker Compose.
- Publicación del evento desde `ms-transferencias`.
- Procesamiento del evento en `ms-cuentas`.
- Recepción del resultado en `ms-notificaciones`.
- Flujo completo funcionando mediante Docker Compose.

## Conclusión

En este proyecto se implementó una arquitectura de microservicios orientada a eventos, incorporando mecanismos de seguridad, resiliencia y comunicación asíncrona.

OAuth2 y JWT permiten proteger el acceso al servicio de transferencias, mientras que Apache Kafka permite desacoplar la comunicación entre los microservicios. Además, Resilience4j entrega tolerancia a fallos en el proceso de notificaciones.

Finalmente, Docker y Docker Compose permiten ejecutar todos los componentes de forma conjunta y mantener una arquitectura más ordenada y portable.

Con las pruebas realizadas se comprobó el funcionamiento del flujo completo desde la solicitud de una transferencia hasta la generación de la notificación correspondiente.