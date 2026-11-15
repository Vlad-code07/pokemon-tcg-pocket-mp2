package pokemontcg.datos;

import java.util.ArrayList;
import java.util.List;

import pokemontcg.cartas.Ataque;
import pokemontcg.cartas.Pokemon;
import pokemontcg.cartas.PokemonEvolucion;

public class CatalogoEvolucion {

    public static List<PokemonEvolucion> obtenerEvoluciones() {

        List<PokemonEvolucion> evoluciones = new ArrayList<>();

        // 1. Raichu (evolución de Pikachu)
        PokemonEvolucion raichu = new PokemonEvolucion("Raichu", "Electrico", 100, 1, "", Pokemon.FASE1, false, "Pikachu");
        raichu.agregarAtaque(
                new Ataque("Trueno", "Electrico", 2, 60));
        evoluciones.add(raichu);

        // 2. Charmeleon (evolución de Charmander)
        PokemonEvolucion charmeleon = new PokemonEvolucion("Charmeleon", "Fuego", 90, 2, "Agua", Pokemon.FASE1, false, "Charmander");
        charmeleon.agregarAtaque(
                new Ataque("Garra Ignea", "Fuego", 2, 50));
        evoluciones.add(charmeleon);

        // 3. Charlizard (evolución de Charmeleon)
        PokemonEvolucion charizard = new PokemonEvolucion("Charizard", "Fuego", 150, 2, "Agua", Pokemon.FASE2, false, "Charmeleon");
        charizard.agregarAtaque(
                new Ataque("Giro Fuego", "Fuego", 4, 120));
        evoluciones.add(charizard);

        // 4. Wartortle (evolución de Squirtle)
        PokemonEvolucion wartortle = new PokemonEvolucion("Wartortle", "Agua", 80, 1, "Electrico", Pokemon.FASE1, false, "Squirtle");
        wartortle.agregarAtaque(
                new Ataque("Salpicadura", "Agua", 2, 40));
        evoluciones.add(wartortle);

        // 5. Blastoise (evolución de Wartortle)
        PokemonEvolucion blastoise = new PokemonEvolucion("Blastoise", "Agua", 150, 3, "Electrico", Pokemon.FASE2, false, "Wartortle");
        blastoise.agregarAtaque(
                new Ataque("Hidrobomba", "Agua", 3, 100));
        evoluciones.add(blastoise);

        // 6. Wigglytuff (evolución de Jigglypuff)
        PokemonEvolucion wigglytuff = new PokemonEvolucion("Wigglytuff", "Incoloro", 110, 2, "Psiquico", Pokemon.FASE1, false, "Jigglypuff");
        wigglytuff.agregarAtaque(
                new Ataque("Bofeton", "Incoloro", 2, 50));
        evoluciones.add(wigglytuff);

        // 7. Gyarados (evolución de Magikarp)
        PokemonEvolucion gyarados = new PokemonEvolucion("Gyarados", "Agua", 150, 4, "Electrico", Pokemon.FASE1, false, "Magikarp");
        gyarados.agregarAtaque(
                new Ataque("Hiperrayo", "Agua", 4, 100));
        evoluciones.add(gyarados);

        return evoluciones;
    }
}
