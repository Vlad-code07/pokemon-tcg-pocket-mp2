package pokemontcg.cartas;

import pokemontcg.combate.Jugador;

public class Soporte extends Entrenador {

    public Soporte(String nombre) {
        super(nombre, "Soporte");
    }

    @Override
    public void activarEfecto(Jugador jugador, Pokemon objetivo) {

        if (jugador == null) {
            return;
        }

        switch (getNombre()) {

            case "Investigación del Profesor":

                // Descarta todas las cartas de la mano.
                jugador.getMano().getCartas().clear();

                // Roba dos cartas.
                jugador.robarCarta();
                jugador.robarCarta();

                System.out.println("Se utilizó Investigación del Profesor.");
                break;

            case "Misty":

                // Si no se especifica un objetivo, utiliza el Pokémon activo.
                if (objetivo == null) {
                    objetivo = jugador.getPokemonActivo();
                }

                if (objetivo != null
                        && objetivo.getTipo().equalsIgnoreCase("Agua")) {

                    // Adjunta hasta tres energías de Agua.
                    for (int i = 0; i < 3; i++) {
                        objetivo.adjuntarEnergia("Agua");
                    }

                    System.out.println("Misty adjuntó 3 energías de Agua.");
                } else {
                    System.out.println("Misty necesita un Pokémon de tipo Agua.");
                }
                break;

            default:
                System.out.println("Carta de soporte no reconocida.");
                break;
        }
    }
}
