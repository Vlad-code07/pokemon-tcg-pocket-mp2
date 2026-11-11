package pokemontcg.combate;

import java.util.ArrayList;
import java.util.List;
import pokemontcg.cartas.Pokemon;

public class Banco {

    private List<Pokemon> pokemones;

    public Banco() {
        this.pokemones = new ArrayList<>();
    }

    public List<Pokemon> getPokemones() {
        return pokemones;
    }

    public void agregarPokemon(Pokemon pokemon) {
        if (pokemon != null && pokemones.size() < 3) {
            pokemones.add(pokemon);
        }
    }

    public Pokemon retirarPokemon(int indice) {
        if (indice >= 0 && indice < pokemones.size()) {
            return pokemones.remove(indice);
        }
        return null;
    }

    public boolean estaVacio() {
        return pokemones.isEmpty();
    }
}
