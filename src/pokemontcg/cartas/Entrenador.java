package pokemontcg.cartas;

import pokemontcg.combate.Jugador;

public abstract class Entrenador extends Carta implements Activable {

    public Entrenador(String nombre, String categoria) {
        super(nombre, categoria);
    }

    @Override
    public abstract void activarEfecto(Jugador jugador, Pokemon objetivo);
}