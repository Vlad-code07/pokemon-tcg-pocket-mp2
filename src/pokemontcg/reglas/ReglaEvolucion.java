package pokemontcg.reglas;

import java.util.List;

import pokemontcg.cartas.Pokemon;
import pokemontcg.cartas.PokemonEvolucion;
import pokemontcg.combate.Jugador;

public class ReglaEvolucion {

    private String etapaPrevia(String etapa) {
        if (Pokemon.FASE1.equals(etapa)) {
            return Pokemon.BASICO;
        } else if (Pokemon.FASE2.equals(etapa)) {
            return Pokemon.FASE1;
        }
        return "";
    }
    
    public String validarEvolucion(Jugador jugador, Pokemon pokemonObjetivo, PokemonEvolucion carta, int turno) {

        if (turno <= 2){
            return "No puedes evolucionar en tu primer turno";
        }

        boolean enJuego = pokemonObjetivo == jugador.getPokemonActivo() || jugador.getBanco().getPokemones().contains(pokemonObjetivo);

        if (!enJuego) {
            return "El Pokémon objetivo no está en juego";
        }

        if (!carta.getNombrePreEvolucion().equals(pokemonObjetivo.getNombre())){
            return carta.getNombre() + " no evoluciona de " + pokemonObjetivo.getNombre();
        }

        if (!etapaPrevia(carta.getEtapa()).equals(pokemonObjetivo.getEtapa())) {
            return "No puede evolucionar de " + pokemonObjetivo.getNombre() + " a " + carta.getEtapa();
        }

        if (turno <= pokemonObjetivo.getTurnoEnJuego()) {
            return pokemonObjetivo.getNombre() + " no puede evolucionar en el mismo turno que fue jugado";
        }

        return null;
    }

    public String evolucionarPokemon(Jugador jugador, Pokemon pokemonObjetivo, PokemonEvolucion carta, int turno) {
        
        int indiceMano = jugador.getMano().getCartas().indexOf(carta);

        if (indiceMano == -1) {
            return "Esa carta no está en tu mano";
        }

        String error = validarEvolucion(jugador, pokemonObjetivo, carta, turno);
        if (error != null) {
            return error;
        }

        carta.heredarEstado(pokemonObjetivo);

        if (pokemonObjetivo == jugador.getPokemonActivo()) {
            jugador.setPokemonActivo(carta);
        } else {
            List<Pokemon> banco = jugador.getBanco().getPokemones();
            banco.set(banco.indexOf(pokemonObjetivo), carta);
        }

        jugador.getMano().removerCarta(indiceMano);

        carta.setTurnoEnJuego(turno);

        return null;
    }
}