# Exp2 Semana 5 - Backend for Frontend (BFF)

## Autor

Felipe Hidalgo

## Asignatura

Desarrollo Backend III - PBY2203

## Descripción del proyecto

Este proyecto implementa el patrón arquitectónico Backend for Frontend (BFF) para el caso del Banco XYZ.

La solución está compuesta por tres backends independientes, cada uno diseñado para responder a las necesidades de un cliente específico:

- BFF Web
- BFF Mobile
- BFF Cajero

Cada backend utiliza la misma base de datos, pero entrega respuestas diferentes y optimizadas según el canal.

Además, se implementó autenticación y autorización mediante JWT, junto con HTTPS y certificados autofirmados para proteger la comunicación.

---

## Estructura del proyecto

```text
Exp2_S5_Felipe_Hidalgo/
├── bff-web/
├── bff-mobile/
├── bff-cajero/
├── evidencias/
├── README.md
└── PROPUESTA_TECNICA.md