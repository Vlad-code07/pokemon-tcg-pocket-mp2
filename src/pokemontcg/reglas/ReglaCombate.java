package pokemontcg.reglas;

import pokemontcg.cartas.Ataque;
import pokemontcg.cartas.Pokemon;
import pokemontcg.combate.Jugador;
import java.util.function.Consumer;

public class ReglaCombate {

    /**
     * Valida si el Pokémon activo posee las energías necesarias para ejecutar el
     * ataque.
     */
    public boolean puedeAtacar(Pokemon atacante, Ataque ataque) {
        if (atacante == null || ataque == null) {
            return false;
        }

        int energiasDisponibles = 0;

        for (String energia : atacante.getEnergiasAdjuntas()) {
            if (ataque.getTipoEnergia().equalsIgnoreCase("Incoloro")
                    || energia.equalsIgnoreCase(ataque.getTipoEnergia())) {
                energiasDisponibles++;
            }
        }

        return energiasDisponibles >= ataque.getCostoEnergia();
    }

    public void atacar(
            Jugador atacante,
            Pokemon defensor,
            Ataque ataque,
            Jugador rival,
            Consumer<String> registrarEvento) {

        if (atacante == null || defensor == null || ataque == null
                || registrarEvento == null) {
            return;
        }

        Pokemon pkAtacante = atacante.getPokemonActivo();

        if (pkAtacante == null || !puedeAtacar(pkAtacante, ataque)) {
            registrarEvento.accept(
                    "No hay suficientes energías para realizar este ataque.");
            return;
        }

        int danioFinal = ataque.getDanioBase();

        // Descontar las energías utilizadas en el ataque
        for (int i = 0; i < ataque.getCostoEnergia(); i++) {

            String tipoEnergia = ataque.getTipoEnergia();

            if (tipoEnergia.equalsIgnoreCase("Incoloro")) {
                // Los ataques incoloros pueden utilizar cualquier energía
                String energiaDisponible = pkAtacante.getEnergiasAdjuntas().get(0);
                pkAtacante.descartarEnergia(energiaDisponible);

            } else {
                // Los demás ataques consumen energías de su propio tipo
                pkAtacante.descartarEnergia(tipoEnergia);
            }
        }
        if (esDebil(pkAtacante.getTipo(), defensor.getTipo())) {
            danioFinal += 20;
            registrarEvento.accept("¡Debilidad! Se agregan 20 puntos de daño.");
        }

        defensor.recibirDanio(danioFinal);

        registrarEvento.accept(
                pkAtacante.getNombre() + " usó " + ataque.getNombre()
                        + " e hizo " + danioFinal + " de daño.");

        if (defensor.getPuntosSalud() <= 0) {
            registrarEvento.accept(
                    "¡El Pokémon " + defensor.getNombre() + " ha sido derrotado!");

            int puntosPremio = defensor.esEx() ? 2 : 1;
            atacante.agregarPuntosPremio(puntosPremio);

            registrarEvento.accept(
                    "[PREMIO] ¡" + atacante.getNombre()
                            + " gana " + puntosPremio + " punto(s) de premio!");
        }
    }

    /**
     * Tabla de debilidades del documento
     * - Fuego ataca a Agua (Agua es débil a Fuego)
     * - Agua ataca a Fuego (Fuego es débil a Agua)
     * - Eléctrico ataca a Agua (Agua es débil a Eléctrico)
     * - Psíquico ataca a Incoloro (Incoloro es débil a Psíquico)
     */
    private boolean esDebil(String tipoAtacante, String tipoDefensor) {
        if (tipoAtacante.equalsIgnoreCase("Fuego") && tipoDefensor.equalsIgnoreCase("Agua")) {
            return true;
        } else if (tipoAtacante.equalsIgnoreCase("Agua") && tipoDefensor.equalsIgnoreCase("Fuego")) {
            return true;
        } else if (tipoAtacante.equalsIgnoreCase("Eléctrico") && tipoDefensor.equalsIgnoreCase("Agua")) {
            return true;
        } else if (tipoAtacante.equalsIgnoreCase("Psíquico") && tipoDefensor.equalsIgnoreCase("Incoloro")) {
            return true;
        }
        return false;
    }

    public boolean verificarGanador(Jugador jugadorActual, Jugador rival) {
        if (jugadorActual.getPuntosPremio() >= 3) {
            return true;
        }

        if (rival.getMazo().estaVacio()) {
            return true;
        }

        if (rival.getPokemonActivo() == null && rival.getBanco().estaVacio()) {
            return true;
        }

        return false;
    }
}