# Sistema de Procesamiento Batch - Banco XYZ

## Descripción

Este proyecto implementa una solución de procesamiento batch utilizando Spring Batch para modernizar procesos legacy del Banco XYZ.

El sistema procesa archivos CSV que contienen información de transacciones, intereses y movimientos anuales de cuentas. Durante el procesamiento se realizan validaciones, transformaciones, almacenamiento en una base de datos MySQL, generación de reportes, procesamiento paralelo y manejo de fallos.

El proyecto contempla tres procesos principales:

- Reporte de transacciones diarias.
- Cálculo de intereses mensuales.
- Generación de estados de cuenta anuales.

---

## Objetivo

Desarrollar una solución con Spring Batch capaz de leer información proveniente de archivos CSV, validar y transformar los datos mediante ItemProcessor, almacenar los resultados válidos en una base de datos relacional y generar reportes asociados a cada proceso.

Además, el proyecto incorpora técnicas de procesamiento paralelo y mecanismos de tolerancia a fallos para mejorar el rendimiento, estabilidad y continuidad de los Jobs.

---

## Tecnologías utilizadas

- Java 17
- Spring Boot
- Spring Batch
- Spring JDBC
- MySQL
- Gradle
- Visual Studio Code
- Git y GitHub

---

## Estructura general del proyecto

```text
src/
└── main/
    ├── java/
    │   └── cl/duoc/bankbatch/
    │       ├── config/
    │       │   ├── BatchThreadConfig.java
    │       │   ├── TransaccionesJobConfig.java
    │       │   ├── InteresesJobConfig.java
    │       │   ├── CuentasAnualesJobConfig.java
    │       │   └── TransaccionSkipPolicy.java
    │       │
    │       ├── dto/
    │       │   └── Clases utilizadas para la lectura de archivos CSV
    │       │
    │       ├── listener/
    │       │   └── TransaccionSkipListener.java
    │       │
    │       ├── model/
    │       │   └── Modelos utilizados para almacenar los datos procesados
    │       │
    │       ├── processor/
    │       │   ├── TransaccionProcessor.java
    │       │   ├── InteresProcessor.java
    │       │   └── CuentaAnualProcessor.java
    │       │
    │       ├── tasklet/
    │       │   └── Tasklets utilizados para generar reportes
    │       │
    │       └── writer/
    │           └── TransaccionRetryWriter.java
    │
    └── resources/
        ├── application.properties
        └── data/
            ├── transacciones.csv
            ├── intereses.csv
            └── cuentas_anuales.csv
```

---

## Jobs implementados

### 1. Reporte de transacciones diarias

El Job `transaccionesJob` procesa el archivo:

```text
data/transacciones.csv
```

El proceso realiza las siguientes acciones:

1. Lee las transacciones desde el archivo CSV.
2. Valida los campos de cada registro.
3. Normaliza los tipos de transacción.
4. Valida los formatos de fecha.
5. Descarta registros con montos inválidos.
6. Almacena las transacciones válidas en MySQL.
7. Genera un resumen de las transacciones procesadas.

La información procesada se almacena en:

```text
transacciones_procesadas
```

También se genera:

```text
reportes/resumen_transacciones.txt
```

---

### 2. Cálculo de intereses mensuales

El Job `interesesJob` procesa el archivo:

```text
data/intereses.csv
```

Durante el procesamiento se realizan validaciones sobre:

- Identificador de la cuenta.
- Nombre del cliente.
- Saldo.
- Edad.
- Tipo de cuenta.

Los tipos de cuenta considerados son:

```text
ahorro
prestamo
```

Se aplican las siguientes tasas:

```text
Ahorro:    1 %
Préstamo:  2 %
```

El sistema calcula el interés y el nuevo saldo final de cada cuenta válida.

Los resultados se almacenan en:

```text
intereses_procesados
```

---

### 3. Estados de cuenta anuales

El Job `cuentasAnualesJob` procesa:

```text
data/cuentas_anuales.csv
```

El proceso valida:

- Número de cuenta.
- Fecha.
- Tipo de transacción.
- Monto.
- Descripción.

Los datos válidos se almacenan en:

```text
cuentas_anuales_procesadas
```

Al finalizar se genera un informe detallado para auditoría:

```text
reportes/informe_cuentas_anuales.txt
```

---

## Validación de datos

Los datos son procesados mediante implementaciones de `ItemProcessor`.

Cuando un registro contiene información inválida, puede ser descartado para impedir que datos inconsistentes sean almacenados en la base de datos.

Entre las principales validaciones implementadas se encuentran:

- Campos obligatorios vacíos.
- Identificadores incorrectos.
- Fechas inválidas.
- Montos negativos o iguales a cero.
- Tipos de transacción incorrectos.
- Tipos de cuenta no reconocidos.
- Edad fuera del rango permitido.
- Saldos inválidos.
- Descripciones vacías.

También se realiza normalización de determinados valores antes de almacenarlos.

---

## Procesamiento paralelo

Para mejorar el rendimiento se implementó procesamiento multi-thread mediante un `TaskExecutor`.

La cantidad de hilos puede configurarse desde:

```properties
batch.threads=4
```

El lector utilizado para el Job de transacciones se encuentra sincronizado mediante `SynchronizedItemStreamReader`, permitiendo utilizar el procesamiento paralelo de forma segura.

---

## Comparación de rendimiento

Para determinar la configuración de procesamiento más conveniente se realizaron pruebas utilizando el mismo conjunto de datos con 1, 2 y 4 hilos.

Se realizaron dos ejecuciones por configuración.

| Cantidad de hilos | Ejecución 1 | Ejecución 2 | Promedio del Job |
|---:|---:|---:|---:|
| 1 hilo | 740 ms | 668 ms | 704 ms |
| 2 hilos | 1222 ms | 682 ms | 952 ms |
| 4 hilos | 696 ms | 640 ms | 668 ms |

La configuración de **4 hilos** obtuvo el menor tiempo promedio de ejecución del Job, con aproximadamente **668 ms**.

Por esta razón se seleccionó como configuración final:

```properties
batch.threads=4
```

Los resultados también muestran que aumentar la cantidad de hilos no garantiza automáticamente una mejora de rendimiento, por lo que fue necesario realizar pruebas con diferentes configuraciones antes de seleccionar la opción final.

---

## Tolerancia a fallos

El Job de transacciones utiliza mecanismos de tolerancia a fallos de Spring Batch.

La configuración incluye:

```java
.faultTolerant()
.skipPolicy(transaccionSkipPolicy)
.retry(TransientDataAccessException.class)
.retryLimit(3)
```

### SkipPolicy

Se implementó una política personalizada:

```text
TransaccionSkipPolicy
```

Esta política permite omitir errores de lectura producidos por registros CSV mal formados, evitando que un único registro incorrecto detenga completamente el Job.

También se utiliza:

```text
TransaccionSkipListener
```

para registrar en consola los elementos omitidos.

Durante una prueba controlada se incorporó un registro CSV mal formado. Spring Batch detectó el error, omitió el registro y permitió que el Job continuara hasta finalizar con estado:

```text
COMPLETED
```

---

## Política de reintento

También se configuró una política de Retry para errores transitorios relacionados con el acceso a datos:

```java
.retry(TransientDataAccessException.class)
.retryLimit(3)
```

Esto permite realizar hasta tres intentos cuando ocurre una falla transitoria.

Para comprobar el funcionamiento se implementó una prueba controlada mediante:

```text
TransaccionRetryWriter
```

Durante la prueba se produjo intencionalmente una falla transitoria en la primera escritura.

Spring Batch realizó nuevamente el intento y el Job finalizó correctamente con:

```text
COMPLETED
```

La simulación de Retry se encuentra desactivada durante la ejecución normal mediante:

```properties
batch.prueba.retry=false
```

---

## Resultados obtenidos

Luego de aplicar las validaciones correspondientes, se obtuvieron los siguientes resultados en la base de datos:

| Proceso | Registros válidos almacenados |
|---|---:|
| Transacciones | 401 |
| Intereses | 50 |
| Cuentas anuales | 524 |

Los registros incorrectos son controlados durante el procesamiento para mantener la integridad de la información almacenada.

---

## Base de datos

El proyecto utiliza MySQL.

La base de datos utilizada es:

```text
bank_batch
```

Las principales tablas del proyecto son:

```text
transacciones_procesadas
intereses_procesados
cuentas_anuales_procesadas
```

Spring Batch también utiliza sus tablas internas de metadatos para almacenar información sobre Jobs, Steps y ejecuciones.

---

## Configuración de conexión

El archivo:

```text
src/main/resources/application.properties
```

utiliza la siguiente configuración:

```properties
spring.application.name=bank-batch

spring.datasource.url=jdbc:mysql://localhost:3306/bank_batch
spring.datasource.username=root
spring.datasource.password=${DB_PASSWORD}

spring.batch.jdbc.initialize-schema=always

spring.batch.job.enabled=true
spring.batch.job.name=transaccionesJob

batch.threads=4
batch.prueba.retry=false
```

La contraseña de MySQL no se almacena directamente dentro del proyecto.

Se utiliza la variable de entorno:

```text
DB_PASSWORD
```

---

## Configuración de la contraseña en Windows PowerShell

Antes de ejecutar el proyecto se debe definir la contraseña de MySQL en la terminal:

```powershell
$env:DB_PASSWORD="CONTRASEÑA_MYSQL"
```

La contraseña solamente se mantiene en la sesión actual de PowerShell.

---

## Ejecución del proyecto

Ubicarse en la carpeta raíz del proyecto.

Para comprobar que el proyecto compila correctamente:

```powershell
.\gradlew.bat clean classes
```

Luego ejecutar:

```powershell
.\gradlew.bat bootRun
```

El Job configurado actualmente como predeterminado es:

```properties
spring.batch.job.name=transaccionesJob
```

---

## Ejecución de los Jobs

Los Jobs disponibles en el proyecto son:

```text
transaccionesJob
interesesJob
cuentasAnualesJob
```

Para ejecutar otro proceso se debe modificar temporalmente:

```properties
spring.batch.job.name=NOMBRE_DEL_JOB
```

Por ejemplo:

```properties
spring.batch.job.name=interesesJob
```

o:

```properties
spring.batch.job.name=cuentasAnualesJob
```

Después se ejecuta nuevamente:

```powershell
.\gradlew.bat bootRun
```

---

## Evidencias realizadas

Durante el desarrollo se registraron evidencias correspondientes a:

- Ejecución exitosa del Job de transacciones.
- Resultado del procesamiento de transacciones en MySQL.
- Ejecución exitosa del Job de intereses.
- Resultado del cálculo de intereses en MySQL.
- Ejecución exitosa del Job de cuentas anuales.
- Resultado de las cuentas anuales en MySQL.
- Comparación de rendimiento utilizando 1 hilo.
- Comparación de rendimiento utilizando 2 hilos.
- Comparación de rendimiento utilizando 4 hilos.
- Prueba controlada de SkipPolicy.
- Continuidad del Job después de un registro CSV mal formado.
- Prueba controlada del mecanismo Retry.
- Recuperación del Job después de una falla transitoria.
- Verificación final de los registros almacenados en MySQL.

---

## Conclusión

La solución desarrollada permite modernizar los procesos batch del Banco XYZ mediante Spring Batch.

Los tres Jobs implementados permiten procesar los archivos legacy, validar y transformar sus datos, almacenar los resultados válidos en MySQL y generar los reportes correspondientes.

La utilización de procesamiento multi-thread permitió comparar distintas configuraciones y seleccionar cuatro hilos como la opción con mejor tiempo promedio durante las pruebas realizadas.

Finalmente, las políticas de Skip y Retry permiten mejorar la resiliencia del proceso, evitando que determinados errores de lectura o fallas transitorias provoquen la interrupción completa del Job.