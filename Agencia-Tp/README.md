# Agencia-Tp

Aplicación de escritorio desarrollada en **Java** para la gestión de una agencia de viajes. Permite administrar viajes, destinos, transportes y responsables a bordo, además de consultar y gestionar el estado de los viajes.

## Funcionalidades

* Crear y gestionar viajes.
* Administrar destinos y kilómetros totales.
* Registrar y asignar transportes a los viajes.
* Gestionar responsables a bordo y su disponibilidad.
* Iniciar, avanzar y finalizar viajes.
* Consultar viajes por diferentes criterios.
* Calcular los kilómetros recorridos y controlar el avance de cada viaje.
* Persistir el estado de la aplicación.

## Tecnologías

* **Java**
* **Java Swing** — interfaz gráfica
* **Programación Orientada a Objetos**
* **Colecciones de Java** (`List`, `Set`, `Map`)
* **Serialización** para persistencia de datos
* **Git / GitHub**

## Arquitectura

El proyecto está organizado siguiendo una separación entre:

* **Model** — entidades y lógica principal del sistema.
* **Controller** — operaciones y gestión de la lógica de la aplicación.
* **View** — interfaces gráficas desarrolladas con Swing.

Entre las principales entidades se encuentran `Viaje`, `Destino`, `Transporte` y `ResponsableABordo`, junto con diferentes tipos de transporte y viaje.

## Ejecución

1. Clonar el repositorio:

```bash
git clone https://github.com/Tomasbruno03/Agencia-Tp.git
```

2. Abrir el proyecto en un IDE compatible con Java, como **IntelliJ IDEA** o **Eclipse**.

3. Ejecutar la clase principal del proyecto.

## Objetivo

Proyecto académico realizado para practicar y aplicar conceptos de **Programación Orientada a Objetos en Java**, incluyendo herencia, polimorfismo, encapsulamiento, colecciones, manejo de estados y persistencia de información.
