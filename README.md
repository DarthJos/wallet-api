# 🚀 MeliWallet API

Sistema de Billetera Digital de alta disponibilidad y consistencia eventual, diseñado como proyecto de preparación para el onboarding en Mercado Libre. El sistema permite gestionar cuentas, procesar transferencias P2P y garantizar la integridad financiera mediante patrones avanzados de microservicios.

---

## 🎯 Requerimientos del Sistema

### Funcionales
- **Gestión de Cuentas:** Creación de billeteras digitales para usuarios.
- **Carga de Saldo:** Depósitos de dinero en la cuenta.
- **Transferencias P2P:** Movimiento de fondos entre cuentas de la plataforma.
- **Consulta de Movimientos:** Historial de transacciones y saldo en tiempo real.

### No Funcionales (Enfoque Senior)
- **Atomicidad (ACID):** Garantía de que el dinero no se duplique ni se pierda en fallos.
- **Idempotencia:** Implementación de `X-Idempotency-Key` para evitar transacciones duplicadas por reintentos de red.
- **Consistencia Eventual:** Uso de mensajería asíncrona para servicios secundarios (Notificaciones).
- **Escalabilidad:** Arquitectura preparada para alta concurrencia.

---

## 🛠️ Stack Tecnológico
- **Lenguaje:** Java 21
- **Framework:** Spring Boot 3.x
- **Base de Datos:** PostgreSQL (Persistencia relacional)
- **Cache/Idempotencia:** Redis
- **Mensajería:** Apache Kafka
- **Herramientas:** Lombok, MapStruct, JUnit 5, Testcontainers.

---

## 🏗️ Arquitectura y Diseño

Se utiliza **Arquitectura Hexagonal (Ports & Adapters)** para desacoplar la lógica de negocio de las dependencias tecnológicas.

### Diagrama C4 (Nivel 2: Contenedores)



1. **API Gateway:** Punto de entrada único.
2. **Wallet-API (Core):** Procesa la lógica financiera.
3. **Redis:** Almacena llaves de idempotencia temporales.
4. **PostgreSQL:** Fuente de verdad para saldos y transacciones.
5. **Kafka:** Broker para eventos de dominio (Transferencia Completada).
6. **Notification-Service:** Consumidor asíncrono para alertas al usuario.

---

## 📂 Estructura de Paquetes
```text
com.meli.wallet
 ├── domain               <-- El corazón: Reglas de negocio puras
 │    ├── model           <-- Clases como "Account", "Transfer"
 │    └── repository      <-- Puertos (Interfaces) de salida a la DB
 ├── application          <-- Los Casos de Uso
 │    ├── service         <-- Orquestadores de la lógica
 │    └── port            <-- Puertos (Interfaces) de entrada
 └── infrastructure       <-- El mundo exterior (Adaptadores)
      ├── input           <-- Entrada (Controllers REST)
      └── output          <-- Salida (Repositorios JPA, Clientes externos)
           ├── persistence <-- Implementación real de la DB
           └── external    <-- (Opcional) Clientes de otras APIs
```
---

## 📝 Architecture Decision Records (ADR)

### ADR 01: Estrategia de Idempotencia
* **Contexto:** Las transacciones financieras pueden duplicarse por reintentos de red.
* **Decisión:** Implementar `X-Idempotency-Key` en el Header.
* **Consecuencia:** Uso de Redis para verificación rápida de duplicados antes de procesar la lógica de negocio.

### ADR 02: Patrón Arquitectónico
* **Contexto:** Se requiere un sistema fácil de testear y mantener.
* **Decisión:** Arquitectura Hexagonal.
* **Consecuencia:** Separación clara entre el Dominio (negocio) y la Infraestructura (detalles técnicos).

### ADR 03: Manejo de Precisión Financiera
* **Decisión:** Uso estricto de `BigDecimal` para montos monetarios y códigos ISO 4217 para divisas, evitando errores de precisión de punto flotante.

### ADR 04: Integridad de Saldo y Locking
* **Estrategia:** Se implementa Pessimistic Locking (`SELECT FOR UPDATE`) para las operaciones de actualización de saldo.
* **Razón:** n un entorno de alta concurrencia financiera, es vital prevenir el "double spending". El bloqueo pesimista asegura que solo una transacción modifique una cuenta a la vez.
* **Persistencia:** Uso de PostgreSQL con tipos `NUMERIC` para evitar errores de redondeo.

---

## 🛠️ Guía de Ejecución Local
1. Asegurarse de tener Docker instalado y corriendo.
2. Ejecutar `docker-compose up -d` para levantar la base de datos.
3. Ejecutar la aplicación desde el IDE o con `./mvnw spring-boot:run`.