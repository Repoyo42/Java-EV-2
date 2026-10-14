# Java-EV-2
# BodegaNube

## Integrantes

- Mateo Nogueira
- Pablo Rebolledo

## Descripción

Proyecto académico de microservicios con Java y Spring Boot para gestionar usuarios, órdenes, inventario y despacho de pedidos. Cada servicio es una aplicación independiente con su propia base de datos MySQL.

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

## Servicios implementados

- **Inventario** (`6767`): CRUD de productos con SKU único, nombre, descripción y cantidad. API bajo `/inventario`.
- **Usuarios** (`6768`): CRUD de usuarios con nombre, email único y conjunto de roles. API bajo `/usuarios`.
- **Órdenes** (`6769`): CRUD de órdenes con cliente, productos/cantidades, estado y fecha de creación. Incluye búsqueda por cliente en `/ordenes/cliente/{clienteId}`.
- **Despacho** (`6770`): registro de un despacho por orden y avance controlado por estados. API bajo `/despachos`.

Los IDs de clientes, productos y órdenes se guardan como referencias. Los servicios todavía no se llaman entre sí ni comprueban que esos IDs existan en los otros servicios. Los estados del flujo de órdenes y despacho son una base inicial que debe confirmarse con los requisitos de negocio.

## Ejecución y pruebas

Cada microservicio se ejecuta y prueba por separado desde su propia carpeta:

```powershell
.\mvnw.cmd spring-boot:run
.\mvnw.cmd test
```

Se requiere Java 17 o superior y MySQL para la ejecución normal. Las pruebas usan H2 en memoria y no necesitan una instancia MySQL.

Las bases predeterminadas son `inventario_db`, `usuarios_db`, `ordenes_db` y `despacho_db`. Se pueden cambiar mediante `INVENTARIO_DB_URL`, `USUARIOS_DB_URL`, `ORDENES_DB_URL` y `DESPACHO_DB_URL`. `DB_USERNAME` y `DB_PASSWORD` permiten configurar las credenciales compartidas; por defecto son `root` y `1234`.

## Endpoints principales

| Servicio | Operaciones |
| --- | --- |
| Inventario | `POST/GET /inventario`, `GET/PUT/DELETE /inventario/{id}` |
| Usuarios | `POST/GET /usuarios`, `GET/PUT/DELETE /usuarios/{id}` |
| Órdenes | `POST/GET /ordenes`, `GET/PUT/DELETE /ordenes/{id}`, `GET /ordenes/cliente/{clienteId}` |
| Despacho | `POST/GET /despachos`, `GET /despachos/{id}`, `GET /despachos/orden/{ordenId}`, `PATCH /despachos/{id}/estado` |

El flujo de despacho inicial es `PENDIENTE -> EN_PICKING -> PREPARADO -> EN_TRANSITO -> ENTREGADO`; desde `PENDIENTE`, `EN_PICKING` o `PREPARADO` también se permite `CANCELADO`. Las transiciones no válidas responden con HTTP 409.

## Tecnologías

Java 17, Spring Boot, Maven, Spring Web, Spring Data JPA, MySQL, H2 para pruebas, Git y Postman.
