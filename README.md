# Pokémon TCG Pocket - Simulación (Mini Proyecto 2)

Simulación interactiva con interfaz gráfica del juego de cartas **Pokémon TCG Pocket** desarrollada en Java 21 y Java Swing. El proyecto implementa mecánicas de combate por turnos, evolución de Pokémon, validación de mazos de 20 cartas, gestión de energías elementales y asignación oficial de puntos de premio.

---

## Descripción del proyecto

Este proyecto consiste en desarrollar una simulación por interfaz gráfica del juego de cartas Pokémon TCG Pocket, utilizando Java 21 y la biblioteca gráfica Swing bajo el patrón de arquitectura **MVC (Modelo-Vista-Controlador)**.

El sistema implementa las mecánicas solicitadas para la expansión **Genetic Apex** y aplica los principios fundamentales de la Programación Orientada a Objetos (POO): encapsulamiento, herencia, polimorfismo, clases abstractas e interfaces.

---

## Tecnologías

- **Java 21** (JDK 21)
- **Java Swing** (Interfaz Gráfica de Usuario)
- **Git y GitHub** (Control de versiones y flujo colaborativo mediante ramas y Pull Requests)
- **Bibliotecas estándar de Java SE**

---

## Integrantes y Roles

- **Andry Enriquez Rodriguez** — Modelo de datos, jerarquía de cartas, integración de eventos GUI y líder de GitHub.
- **Julián Silva** — Motor de combate, reglas de evolución y validaciones.
- **Vladimir Tello** — Diseño e implementación de interfaz gráfica (`TableroJuego`), flujo visual de turnos y documentación.

---

## Funcionalidades Implementadas

- **Interfaz Gráfica Completa (Java Swing):**
  - Pantalla de configuración inicial para registro de jugadores y selección de mazos (`VentanaInicio`).
  - Tablero principal de combate en tiempo real con barras de vida dinámicas, mano deslizable, banco (máx. 3 Pokémon) y registro cronológico de jugadas (`TableroJuego`).
  - Diálogos modales para cambio de turno y pantalla de resultados (`DialogoCambioTurno`, `DialogoFinPartida`).
- **Sistema de Cartas y Mazos:**
  - Jerarquía de cartas Pokémon (Básicos, Fase 1, Fase 2 y `ex`) y cartas de Entrenador (`Objeto` y `Soporte`).
  - Construcción y validación estricta de mazos de 20 cartas (máximo 2 copias por nombre).
- **Sistema de Turnos y Combate:**
  - Adjuntar energía elemental por turno según el tipo del Pokémon activo (`Agua`, `Fuego`, `Eléctrico`, `Psíquico`, etc.).
  - Evolución de Pokémon respetando turnos en juego y fases evolutivas.
  - Jugar cartas de Entrenador (Soporte / Objeto).
  - Colocar Pokémon en el Banco o retirar al Pokémon activo pagando su costo de retirada.
  - Cálculo de daño base con bonificación automática por debilidad (+20 de daño).
  - Verificación de KO y puntos de premio: 1 punto por Pokémon normal derrotado o 2 puntos si es Pokémon `ex`.
- **Condición de Victoria:** El primer jugador en alcanzar 3 puntos de premio o dejar al rival sin Pokémon en campo gana la partida.

---

## Estructura del Proyecto

```text
src/pokemontcg/
├── cartas/      # Jerarquía de cartas (Carta, Pokemon, PokemonEvolucion, Entrenador, Objeto, Soporte, Ataque, Activable)
├── combate/     # Lógica de estado de partida (Jugador, Mazo, Mano, Banco)
├── datos/       # Catálogos de cartas y mazos predefinidos (CatalogoPokemon, CatalogoEvolucion, CatalogoEntrenadores, CatalogoMazos)
├── gui/         # Vistas gráficas en Java Swing (VentanaInicio, TableroJuego, DialogoCambioTurno, DialogoFinPartida)
├── reglas/      # Validaciones y reglas del juego (ReglaCombate, ReglaEvolucion, ValidacionMazo)
└── App.java     # Punto de entrada principal de la aplicación
