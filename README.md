# Java-EV-2
# BodegaNube

## Integrantes

- Mateo Nogueira
- Pablo Rebolledo

## Descripción

Proyecto académico de microservicios con Java y Spring Boot para gestionar usuarios, órdenes, inventario y despacho de pedidos. Este documento describe la estructura y los requisitos que se deben implementar para la Evaluación Parcial N°2.

## Estructura del proyecto

```text
proyecto/
├── usuarios-service/
│   ├── pom.xml
│   └── src/
├── despacho-service/
│   ├── pom.xml
│   └── src/
├── ordenes-service/
│   ├── pom.xml
│   └── src/
└── inventario-service/
    ├── pom.xml
    └── src/
```

Cada servicio tendrá su propia aplicación Spring Boot y base de datos. Su código se organizará en las capas `controller`, `service`, `repository` y `model`.

## Microservicios

- **Usuarios:** gestión de usuarios y roles.
- **Despacho:** gestión del picking y despacho de pedidos.
- **Órdenes:** creación y administración de órdenes.
- **Inventario:** gestión de productos y existencias.

## Tecnologías

Java 17, Spring Boot, Maven, Spring Web, Spring Data JPA, MySQL, Git y Postman.
