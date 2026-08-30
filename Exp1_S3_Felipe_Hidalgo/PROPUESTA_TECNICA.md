# Propuesta Técnica
## Sistema de Procesamiento Batch - Banco XYZ

## 1. Introducción

El Banco XYZ mantiene procesos legacy encargados de procesar información relacionada con transacciones diarias, cálculo de intereses mensuales y estados de cuenta anuales.

La presente propuesta técnica plantea la modernización de estos procesos mediante Spring Batch, permitiendo automatizar la lectura, validación, transformación y almacenamiento de información proveniente de archivos CSV.

La solución también incorpora procesamiento paralelo y mecanismos de tolerancia a fallos con el objetivo de mejorar el rendimiento, estabilidad y continuidad de los procesos batch.

---

## 2. Objetivo de la solución

Implementar una arquitectura basada en Spring Batch que permita procesar los archivos legacy del Banco XYZ de manera estructurada, manteniendo la integridad de los datos y permitiendo que el sistema pueda continuar funcionando frente a determinados errores.

Los principales objetivos son:

- Procesar archivos CSV mediante Jobs y Steps.
- Validar y transformar los datos utilizando ItemProcessor.
- Almacenar los registros válidos en una base de datos MySQL.
- Generar reportes asociados a los procesos batch.
- Implementar procesamiento paralelo.
- Comparar diferentes cantidades de hilos.
- Seleccionar una configuración adecuada según los resultados obtenidos.
- Implementar políticas de tolerancia a fallos.
- Incorporar mecanismos de Skip y Retry.

---

## 3. Arquitectura propuesta

La solución utiliza Spring Batch y divide cada proceso en Jobs y Steps.

La arquitectura general utilizada es:

```text
Archivo CSV
    ↓
ItemReader
    ↓
ItemProcessor
    ↓
ItemWriter
    ↓
Base de datos MySQL
    ↓
Reporte / Resultado
```

La separación de responsabilidades permite mantener cada etapa del procesamiento claramente definida.

### ItemReader

Se utiliza para realizar la lectura de los archivos CSV.

### ItemProcessor

Se utiliza para validar, transformar y normalizar la información antes de almacenarla.

### ItemWriter

Se utiliza para almacenar los registros válidos en MySQL.

### Tasklet

Se utiliza en los procesos que requieren generar reportes adicionales después de finalizar el procesamiento principal.

---

## 4. Jobs implementados

La solución contempla tres Jobs principales.

### 4.1. transaccionesJob

Procesa:

```text
transacciones.csv
```

Su objetivo es analizar las transacciones diarias, detectar información incorrecta y almacenar únicamente los registros válidos.

Las principales validaciones realizadas corresponden a:

- Campos vacíos.
- Identificadores inválidos.
- Fechas incorrectas.
- Montos menores o iguales a cero.
- Tipos de transacción no reconocidos.

Los tipos válidos son normalizados para mantener consistencia en los datos.

Después del procesamiento se genera:

```text
reportes/resumen_transacciones.txt
```

Los registros válidos son almacenados en:

```text
transacciones_procesadas
```

---

### 4.2. interesesJob

Procesa:

```text
intereses.csv
```

El objetivo del Job es calcular los intereses correspondientes a cuentas de ahorro y préstamos.

Las principales validaciones corresponden a:

- Número de cuenta.
- Nombre.
- Saldo.
- Edad.
- Tipo de cuenta.

Las tasas utilizadas son:

```text
Cuenta de ahorro: 1 %
Préstamo:         2 %
```

Después de aplicar la tasa correspondiente se calcula:

```text
interés calculado
saldo final
```

Los resultados válidos son almacenados en:

```text
intereses_procesados
```

---

### 4.3. cuentasAnualesJob

Procesa:

```text
cuentas_anuales.csv
```

Su objetivo es consolidar movimientos asociados a las cuentas y generar información útil para auditorías.

Se validan:

- Número de cuenta.
- Fecha.
- Tipo de transacción.
- Monto.
- Descripción.

Los registros válidos son almacenados en:

```text
cuentas_anuales_procesadas
```

Posteriormente se genera:

```text
reportes/informe_cuentas_anuales.txt
```

Este informe contiene el detalle de los movimientos asociados a las cuentas procesadas.

---

## 5. Procesamiento y validación de datos

Para realizar las validaciones se implementaron diferentes ItemProcessor.

```text
TransaccionProcessor
InteresProcessor
CuentaAnualProcessor
```

Cada Processor tiene la responsabilidad de verificar la información correspondiente a su proceso.

Cuando un registro contiene información que no cumple las reglas definidas, este puede ser descartado antes de llegar al ItemWriter.

Esto evita almacenar datos incorrectos en la base de datos.

También se realizan transformaciones y normalizaciones, como la conversión de tipos de transacción a un formato uniforme y la aceptación de distintos formatos válidos de fecha.

---

## 6. Base de datos

Se seleccionó MySQL como base de datos relacional para almacenar los resultados procesados.

La base utilizada es:

```text
bank_batch
```

Las principales tablas son:

```text
transacciones_procesadas
intereses_procesados
cuentas_anuales_procesadas
```

Spring Batch también utiliza tablas internas para mantener información relacionada con la ejecución de Jobs y Steps.

---

## 7. Seguridad de las credenciales

La contraseña de MySQL no se almacena directamente dentro del código fuente ni en el repositorio.

La configuración utiliza:

```properties
spring.datasource.password=${DB_PASSWORD}
```

La contraseña es entregada al proyecto mediante una variable de entorno.

En Windows PowerShell:

```powershell
$env:DB_PASSWORD="CONTRASEÑA_MYSQL"
```

Esto evita publicar la contraseña de la base de datos en GitHub.

---

## 8. Estrategia de escalamiento

Para mejorar el rendimiento del proceso se seleccionó una estrategia de procesamiento multi-thread.

La cantidad de hilos se administra mediante la propiedad:

```properties
batch.threads=4
```

La configuración se realiza mediante:

```text
BatchThreadConfig
```

Se utiliza un:

```text
ThreadPoolTaskExecutor
```

que permite definir la cantidad de hilos utilizados durante la ejecución.

Para mantener segura la lectura del archivo de transacciones se utiliza:

```text
SynchronizedItemStreamReader
```

El procesamiento se realiza mediante chunks de:

```text
10 registros
```

---

## 9. Pruebas de rendimiento

Para determinar una configuración adecuada se realizaron pruebas utilizando:

```text
1 hilo
2 hilos
4 hilos
```

Todas las pruebas fueron realizadas utilizando el mismo conjunto de datos.

Se realizaron dos ejecuciones por cada configuración.

| Configuración | Ejecución 1 | Ejecución 2 | Tiempo promedio |
|---|---:|---:|---:|
| 1 hilo | 740 ms | 668 ms | 704 ms |
| 2 hilos | 1222 ms | 682 ms | 952 ms |
| 4 hilos | 696 ms | 640 ms | 668 ms |

La configuración que obtuvo el menor tiempo promedio fue:

```text
4 hilos = 668 ms
```

Por esta razón se seleccionó como configuración final:

```properties
batch.threads=4
```

Los resultados demostraron también que utilizar una mayor cantidad de hilos no garantiza automáticamente una mejora del rendimiento.

La ejecución con dos hilos presentó un tiempo superior en una de las pruebas, lo que demuestra la importancia de realizar mediciones antes de seleccionar una configuración.

---

## 10. Tolerancia a fallos

Para aumentar la resiliencia se utiliza la configuración:

```java
.faultTolerant()
```

Sobre esta configuración se implementaron mecanismos de Skip y Retry.

---

## 11. Política personalizada de Skip

Se desarrolló:

```text
TransaccionSkipPolicy
```

Su función es permitir que determinados errores de lectura del archivo CSV puedan ser omitidos sin detener completamente el Job.

La política establece un límite máximo de registros que pueden ser omitidos.

También se implementó:

```text
TransaccionSkipListener
```

para registrar en consola la información relacionada con los registros omitidos.

### Prueba realizada

Se agregó temporalmente un registro mal formado al archivo CSV.

El registro contenía una cantidad incorrecta de columnas.

Durante la ejecución Spring Batch detectó el problema y mostró:

```text
[SKIP] Linea 1002 omitida por error de lectura
```

El Job continuó funcionando y terminó con estado:

```text
COMPLETED
```

Después del proceso se comprobaron:

```text
401 registros válidos
```

en la tabla de transacciones.

El registro utilizado para realizar la prueba fue eliminado posteriormente del archivo original.

---

## 12. Política de Retry

También se configuró un mecanismo de reintento:

```java
.retry(TransientDataAccessException.class)
.retryLimit(3)
```

Esta configuración permite realizar nuevamente una operación cuando ocurre una falla transitoria relacionada con el acceso a datos.

Para comprobar el mecanismo se creó:

```text
TransaccionRetryWriter
```

La clase permite generar una falla controlada únicamente cuando la prueba está habilitada.

La propiedad utilizada es:

```properties
batch.prueba.retry=true
```

Durante la prueba se produjo una falla transitoria en el primer intento de escritura.

El sistema informó:

```text
[RETRY] Falla transitoria simulada.
Spring Batch intentara nuevamente la escritura.
```

Spring Batch realizó nuevamente la operación y el Job finalizó con:

```text
COMPLETED
```

La base de datos mantuvo correctamente:

```text
401 registros válidos
```

Después de finalizar las pruebas se dejó la propiedad en:

```properties
batch.prueba.retry=false
```

De esta forma la falla simulada no se ejecuta durante el funcionamiento normal del sistema.

---

## 13. Resultados finales

Después de procesar y validar los archivos se obtuvieron los siguientes registros válidos:

| Proceso | Registros almacenados |
|---|---:|
| Transacciones | 401 |
| Intereses | 50 |
| Cuentas anuales | 524 |

Además se generan los reportes:

```text
reportes/resumen_transacciones.txt
reportes/informe_cuentas_anuales.txt
```

---

## 14. Configuración final

La configuración seleccionada para el proyecto es:

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

Esta configuración mantiene:

- Conexión segura mediante variable de entorno.
- Inicialización de las tablas internas de Spring Batch.
- Cuatro hilos para el procesamiento paralelo.
- Prueba artificial de Retry desactivada.

---

## 15. Beneficios de la solución

La propuesta implementada permite:

- Modernizar los procesos legacy utilizando Spring Batch.
- Separar las responsabilidades mediante Jobs y Steps.
- Validar los datos antes de almacenarlos.
- Evitar el almacenamiento de información inconsistente.
- Procesar información utilizando múltiples hilos.
- Seleccionar la cantidad de hilos mediante pruebas de rendimiento.
- Continuar el procesamiento frente a determinados errores de lectura.
- Reintentar operaciones ante fallas transitorias.
- Mantener las credenciales fuera del código fuente.
- Generar reportes útiles para revisión y auditoría.

---

## 16. Conclusión

La solución implementada permite modernizar los tres procesos batch solicitados para el Banco XYZ utilizando Spring Batch y MySQL.

La arquitectura permite separar la lectura, procesamiento y escritura de información, facilitando el mantenimiento del proyecto.

Las validaciones implementadas permiten controlar registros incorrectos antes de almacenarlos.

Para mejorar el rendimiento se implementó procesamiento multi-thread y se compararon configuraciones de 1, 2 y 4 hilos. De acuerdo con las mediciones realizadas, cuatro hilos presentaron el mejor tiempo promedio y fueron seleccionados como configuración final.

Finalmente, la implementación de políticas de Skip y Retry permite aumentar la resiliencia del sistema, ya que determinados errores pueden ser controlados sin provocar la finalización completa del proceso batch.