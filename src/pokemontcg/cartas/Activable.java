package pokemontcg.cartas;

import pokemontcg.combate.Jugador;

public interface Activable {

    void activarEfecto(Jugador jugador, Pokemon objetivo);
}
