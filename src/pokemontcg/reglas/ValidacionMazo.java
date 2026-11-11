package pokemontcg.reglas;

import pokemontcg.cartas.Carta;
import pokemontcg.cartas.Pokemon;
import pokemontcg.combate.Mazo;
import pokemontcg.combate.Jugador;

public class ValidacionMazo {

    public static boolean validarMazo(Mazo mazo) {
        if (mazo.getCartas().size() != 20) {
            System.out.println("El mazo debe contener exactamente 20 cartas.");
            return false;
        }

        int contieneBasico = 0;
        java.util.List<Carta> listaCartas = mazo.getCartas();
        
        for (int i = 0; i < listaCartas.size(); i++) {
            Carta cartaActual = listaCartas.get(i);

            // Se valida correctamente si la carta es un Pokémon Básico
            if (cartaActual instanceof Pokemon) {
                Pokemon p = (Pokemon) cartaActual;
                if (p.getEtapa().equalsIgnoreCase("Básico") || p.getEtapa().equalsIgnoreCase("Basico")) {
                    contieneBasico++;
                }
            }

            String nombreActual = cartaActual.getNombre();
            int repeticiones = 0;

            for (int j = 0; j < listaCartas.size(); j++) {
                if (listaCartas.get(j).getNombre().equals(nombreActual)) {
                    repeticiones++;
                }
            }    

            if (repeticiones > 2) {
                System.out.println("La carta " + nombreActual + " se repite más de 2 veces en el mazo.");
                return false;
            }
        }    

        if (contieneBasico < 6) {
            System.out.println("El mazo debe contener al menos 6 Pokémon básicos.");
            return false;
        }
        System.out.println("¡Mazo validado exitosamente!");
        return true;
    }

    public static void repartirManoInicial(Jugador jugador) {
        if (jugador == null || jugador.getMazo() == null) return;

        boolean tieneBasicoValido = false;

        while (!tieneBasicoValido) {
            // 1. Devolver mano al mazo si no hay un básico seleccionable
            while (!jugador.getMano().getCartas().isEmpty()) {
                Carta cartaDevuelta = jugador.getMano().getCartas().remove(0);
                jugador.getMazo().agregarCarta(cartaDevuelta);
            }

            // 2. Mezclar y robar 5 cartas
            jugador.getMazo().barajar();
            for (int i = 0; i < 5; i++) {
                if (!jugador.getMazo().estaVacio()) {
                    jugador.getMano().agregarCarta(jugador.getMazo().robarCarta());
                }
            }

            // 3. Garantizar al menos 1 Pokémon básico que NO sea ex
            for (Carta carta : jugador.getMano().getCartas()) {
                if (carta instanceof Pokemon) {
                    Pokemon p = (Pokemon) carta;
                    if (!p.esEx()) {
                        tieneBasicoValido = true;
                        break;
                    }
                }
            }
            
            if (!tieneBasicoValido) {
                System.out.println("¡" + jugador.getNombre() + " no tiene Pokémon básicos iniciales (no-ex). Rebarajando!");
            }
        }
    }
}