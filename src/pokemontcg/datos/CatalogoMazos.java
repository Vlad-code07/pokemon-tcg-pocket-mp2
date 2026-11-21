package pokemontcg.datos;

import java.util.List;

import pokemontcg.cartas.Pokemon;
import pokemontcg.cartas.Entrenador;
import pokemontcg.cartas.Objeto;
import pokemontcg.combate.Mazo;
import pokemontcg.cartas.PokemonEvolucion;
import pokemontcg.cartas.Soporte;
import pokemontcg.cartas.Ataque;

public class CatalogoMazos {

    // Crear el mazo electrico
    public static Mazo crearMazoElectrico() {

        Mazo mazo = new Mazo();

        agregarPokemon(mazo, "Pikachu");
        agregarPokemon(mazo, "Zapdos ex");
        agregarPokemon(mazo, "Mewtwo ex");
        agregarPokemon(mazo, "Snorlax");
        agregarPokemon(mazo, "Jigglypuff");

        agregarEvolucion(mazo, "Raichu");

        agregarEntrenadores(mazo, 9);

        return mazo;
    }

    // Crear el mazo de fuego
    public static Mazo crearMazoFuego() {

    Mazo mazo = new Mazo();

    agregarPokemon(mazo, "Charmander");
    agregarPokemon(mazo, "Moltres ex");
    agregarPokemon(mazo, "Mewtwo ex");
    agregarPokemon(mazo, "Snorlax");
    agregarPokemon(mazo, "Jigglypuff");

    agregarEvolucion(mazo, "Charmeleon");
    agregarEvolucion(mazo, "Charizard");

    agregarEntrenadores(mazo, 8);

    return mazo;
}

    // Crear el mazo de agua
    public static Mazo crearMazoAgua() {

    Mazo mazo = new Mazo();

    agregarPokemon(mazo, "Squirtle");
    agregarPokemon(mazo, "Magikarp");
    agregarPokemon(mazo, "Articuno ex");
    agregarPokemon(mazo, "Snorlax");
    agregarPokemon(mazo, "Jigglypuff");

    agregarEvolucion(mazo, "Wartortle");
    agregarEvolucion(mazo, "Blastoise");
    agregarEvolucion(mazo, "Gyarados");

    agregarEntrenadores(mazo, 7);

    return mazo;
}

    // Crear el mazo mixto
    public static Mazo crearMazoMixto() {

    Mazo mazo = new Mazo();

    agregarPokemon(mazo, "Pikachu");
    agregarPokemon(mazo, "Charmander");
    agregarPokemon(mazo, "Squirtle");
    agregarPokemon(mazo, "Jigglypuff");
    agregarPokemon(mazo, "Magikarp");

    agregarEvolucion(mazo, "Raichu");
    agregarEvolucion(mazo, "Charmeleon");
    agregarEvolucion(mazo, "Wartortle");
    agregarEvolucion(mazo, "Gyarados");

    agregarEntrenadores(mazo, 6);

    return mazo;
}

    // Agregar dos copias independientes de cada Pokemon seleccionado
    private static void agregarPokemon(Mazo mazo, String nombre) {

    List<Pokemon> pokemonDisponibles =
            CatalogoPokemon.obtenerPokemon();

    for (Pokemon pokemon : pokemonDisponibles) {

        if (pokemon.getNombre().equals(nombre)) {

            for (int copia = 0; copia < 2; copia++) {

                Pokemon nuevoPokemon = new Pokemon(
                        pokemon.getNombre(),
                        pokemon.getTipo(),
                        pokemon.getPuntosSaludMaximos(),
                        pokemon.getCostoRetirada(),
                        pokemon.getDebilidad(),
                        pokemon.getEtapa(),
                        pokemon.esEx()
                );

                for (Ataque ataque : pokemon.getAtaques()) {

                    Ataque nuevoAtaque = new Ataque(
                            ataque.getNombre(),
                            ataque.getTipoEnergia(),
                            ataque.getCostoEnergia(),
                            ataque.getDanioBase()
                    );

                    nuevoPokemon.agregarAtaque(nuevoAtaque);
                }

                mazo.agregarCarta(nuevoPokemon);
            }

            break;
        }
    }
}

    // Agregar cartas de evolución al mazo
private static void agregarEvolucion(Mazo mazo, String nombre) {

    List<PokemonEvolucion> evoluciones =
            CatalogoEvolucion.obtenerEvoluciones();

    for (PokemonEvolucion evolucion : evoluciones) {

        if (evolucion.getNombre().equals(nombre)) {

            PokemonEvolucion nuevaEvolucion = new PokemonEvolucion(
                    evolucion.getNombre(),
                    evolucion.getTipo(),
                    evolucion.getPuntosSaludMaximos(),
                    evolucion.getCostoRetirada(),
                    evolucion.getDebilidad(),
                    evolucion.getEtapa(),
                    evolucion.esEx(),
                    evolucion.getNombrePreEvolucion()
            );

            for (Ataque ataque : evolucion.getAtaques()) {

                Ataque nuevoAtaque = new Ataque(
                        ataque.getNombre(),
                        ataque.getTipoEnergia(),
                        ataque.getCostoEnergia(),
                        ataque.getDanioBase()
                );

                nuevaEvolucion.agregarAtaque(nuevoAtaque);
            }

            mazo.agregarCarta(nuevaEvolucion);
            break;
        }
    }
}

    // Agregar una cantidad específica de entrenadores
private static void agregarEntrenadores(Mazo mazo, int cantidad) {

    List<Entrenador> entrenadores =
            CatalogoEntrenadores.obtenerEntrenadores();

    for (int i = 0; i < cantidad; i++) {

        Entrenador entrenador = entrenadores.get(i % entrenadores.size());

        Entrenador nuevaCarta;

        if (entrenador instanceof Objeto) {
            nuevaCarta = new Objeto(entrenador.getNombre());
        } else {
            nuevaCarta = new Soporte(entrenador.getNombre());
        }

        mazo.agregarCarta(nuevaCarta);
    }
}

    // Obtener un mazo según el nombre seleccionado
    public static Mazo obtenerMazo(String nombreMazo) {

        switch (nombreMazo) {

            case "Mazo Electrico":
                return crearMazoElectrico();

            case "Mazo Fuego":
                return crearMazoFuego();

            case "Mazo Agua":
                return crearMazoAgua();

            case "Mazo Mixto":
                return crearMazoMixto();

            default:
                return null;
        }
    }
}
