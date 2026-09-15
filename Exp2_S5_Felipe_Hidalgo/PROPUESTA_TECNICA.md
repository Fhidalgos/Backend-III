# Propuesta Técnica - Backend for Frontend (BFF)

## Autor

Felipe Hidalgo

## Asignatura

Desarrollo Backend III - PBY2203

## 1. Introducción

Para el caso del Banco XYZ se propone implementar una arquitectura basada en el patrón Backend for Frontend (BFF).

La solución considera tres tipos de clientes diferentes: Web, Mobile y Cajero Automático. Debido a que cada uno posee necesidades distintas, se decidió implementar un backend independiente para cada canal.

El objetivo es entregar solamente los datos y funcionalidades necesarias para cada cliente, manteniendo además mecanismos de seguridad mediante autenticación, autorización, JWT y HTTPS.

---

## 2. Estrategia seleccionada

La estrategia seleccionada consiste en utilizar BFF independientes.

La arquitectura queda organizada de la siguiente forma:

```text
Cliente Web
    |
    v
BFF Web
Puerto 8081

Cliente Mobile
    |
    v
BFF Mobile
Puerto 8082

Cajero Automático
    |
    v
BFF Cajero
Puerto 8083