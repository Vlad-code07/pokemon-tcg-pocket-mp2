# pokemon-tcg-pocket
Simulación por consola de Pokémon TCG Pocket en Java 21 para el Mini Proyecto 1

# Descripción del proyecto
Este proyecto consiste en desarrollar una simulación por consola del juego de cartas Pokémon TCG Pocket, utilizando Java 21.

El sistema implementará las mecánicas solicitadas para la expansión Genetic Apex y aplicará los principios de Programación Orientada a Objetos: encapsulamiento, herencia, polimorfismo, clases abstractas e interfaces.

# Tecnologías
- Java 21
- Git
- GitHub
- Bibliotecas estándar de Java SE

# Integrantes
- Andry Enriquez Rodriguez — Modelo de datos, cartas y líder de GitHub.
- Julián Silva — Motor de combate y reglas.
- Vladimir Tello — Flujo de turnos, interfaz de consola y documentación.

# Funcionalidades previstas
- Sistema de cartas Pokémon y entrenador.
- Construcción y validación de mazos de 20 cartas.
- Gestión de jugadores, manos, banco y energías.
- Sistema de turnos y combate.
- Ataques, debilidades y puntos de premio.
- Condiciones de victoria.
- Interfaz de consola con menús numerados.

# Requisitos
- Tener instalado el JDK 21.
- Disponer de una terminal o entorno de desarrollo compatible con Java.

# Estado del proyecto
En desarrollo.

# Compilación y ejecución
# Pokémon TCG Pocket - Simulador en Java

Simulador interactivo por consola del juego de cartas Pokémon TCG Pocket desarrollado en Java. El proyecto implementa mecánicas avanzadas de combate por turnos, validación de mazos, gestión de energías elementales y asignación oficial de puntos de premio.

---

## Descripción del Juego

El juego simula una batalla entre dos jugadores utilizando las reglas de Pokémon TCG Pocket:

- **Estructura del turno:**
  - Adjuntar energía elemental de la zona según el tipo del Pokémon activo (`Agua`, `Fuego`, `Eléctrico`, `Psíquico`, etc.).
  - Jugar cartas de Entrenador (Soporte / Objeto).
  - Colocar Pokémon en el Banco o retirar al Pokémon activo.
  - Atacar al Pokémon rival o pasar turno.
- **Sistema de Combate:**
  - Cálculo de daño base y aplicación automática de bono de debilidad (+20 de daño).
  - Verificación de KO: al derrotar a un Pokémon rival, el jugador atacante recibe 1 punto de premio (o 2 puntos si el Pokémon derrotado es `ex`).
- **Condición de Victoria:** El primer jugador en alcanzar 3 puntos de premio o dejar al rival sin Pokémon en campo gana la partida.

---

## Instrucciones de Ejecución

### Requisitos Previos
- Java Development Kit (JDK) 17 o superior.
- Visual Studio Code o cualquier IDE compatible con Java (o la consola de comandos).

### Compilación y Ejecución desde la Consola

1. **Clonar el repositorio:**
   ```bash
   git clone https://github.com/andryunivalle/pokemon-tcg-pocket
   cd pokemon-tcg-pocket

# Trabajo colaborativo
El equipo utilizará Git y GitHub para gestionar el código fuente, registrar los cambios mediante commits, trabajar con ramas y revisar los Pull Requests antes de integrar las funcionalidades.
