package pokemontcg.datos;

import java.util.ArrayList;
import java.util.List;

import pokemontcg.cartas.Ataque;
import pokemontcg.cartas.Pokemon;

public class CatalogoPokemon {

    public static List<Pokemon> obtenerPokemon() {

        List<Pokemon> pokemon = new ArrayList<>();

        // 1. Pikachu
        Pokemon pikachu = new Pokemon(
                "Pikachu", "Electrico", 60, 1, "Básico", false);
        pikachu.agregarAtaque(
                new Ataque("Mordisco", "Electrico", 1, 20));
        pokemon.add(pikachu);

        // 2. Charmander
        Pokemon charmander = new Pokemon(
                "Charmander", "Fuego", 60, 1, "Básico", false);
        charmander.agregarAtaque(
                new Ataque("Ascuas", "Fuego", 1, 30));
        pokemon.add(charmander);

        // 3. Squirtle
        Pokemon squirtle = new Pokemon(
                "Squirtle", "Agua", 60, 1, "Básico", false);
        squirtle.agregarAtaque(
                new Ataque("Placaje", "Agua", 1, 20));
        pokemon.add(squirtle);

        // 4. Jigglypuff
        Pokemon jigglypuff = new Pokemon(
                "Jigglypuff", "Incoloro", 70, 1, "Básico", false);
        jigglypuff.agregarAtaque(
                new Ataque("Golpe", "Incoloro", 1, 20));
        pokemon.add(jigglypuff);

        // 5. Magikarp
        Pokemon magikarp = new Pokemon(
                "Magikarp", "Agua", 30, 1, "Básico", false);
        magikarp.agregarAtaque(
                new Ataque("Chapoteo", "Agua", 1, 10));
        pokemon.add(magikarp);

        // 6. Snorlax
        Pokemon snorlax = new Pokemon(
                "Snorlax", "Incoloro", 150, 4, "Básico", false);
        snorlax.agregarAtaque(
                new Ataque("Golpe Cuerpo", "Incoloro", 3, 100));
        pokemon.add(snorlax);

        // 7. Mewtwo ex
        Pokemon mewtwo = new Pokemon(
                "Mewtwo ex", "Psíquico", 150, 2, "Básico", true);
        mewtwo.agregarAtaque(
                new Ataque("Ataque Psíquico", "Psíquico", 2, 50));
        mewtwo.agregarAtaque(
                new Ataque("Psicorrayo", "Psíquico", 3, 150));
        pokemon.add(mewtwo);

        // 8. Zapdos ex
        Pokemon zapdos = new Pokemon(
                "Zapdos ex", "Electrico", 130, 1, "Básico", true);
        zapdos.agregarAtaque(
                new Ataque("Picotazo", "Incoloro", 1, 30));
        zapdos.agregarAtaque(
                new Ataque("Tormenta Eléctrica", "Electrico", 3, 100));
        pokemon.add(zapdos);

        // 9. Moltres ex
        Pokemon moltres = new Pokemon(
                "Moltres ex", "Fuego", 130, 2, "Básico", true);
        moltres.agregarAtaque(
                new Ataque("Aleteo", "Incoloro", 1, 30));
        moltres.agregarAtaque(
                new Ataque("Danza Infernal", "Fuego", 3, 70));
        pokemon.add(moltres);

        // 10. Articuno ex
        Pokemon articuno = new Pokemon(
                "Articuno ex", "Agua", 130, 2, "Básico", true);
        articuno.agregarAtaque(
                new Ataque("Viento Helado", "Incoloro", 1, 30));
        articuno.agregarAtaque(
                new Ataque("Ventisca", "Agua", 3, 100));
        pokemon.add(articuno);

        return pokemon;
    }
}