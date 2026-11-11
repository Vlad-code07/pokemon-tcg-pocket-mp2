package pokemontcg.datos;

import java.util.ArrayList;
import java.util.List;

import pokemontcg.cartas.Entrenador;
import pokemontcg.cartas.Objeto;
import pokemontcg.cartas.Soporte;

public class CatalogoEntrenadores {

    public static List<Entrenador> obtenerEntrenadores() {

        List<Entrenador> entrenadores = new ArrayList<>();

        // Cartas de objeto
        entrenadores.add(new Objeto("Poción"));
        entrenadores.add(new Objeto("Poké Ball"));
        entrenadores.add(new Objeto("Turbo Velocidad"));

        // Cartas de soporte
        entrenadores.add(new Soporte("Investigación del Profesor"));
        entrenadores.add(new Soporte("Misty"));

        return entrenadores;
    }
}
