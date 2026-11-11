package pokemontcg;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import pokemontcg.cartas.Ataque;
import pokemontcg.cartas.Carta;
import pokemontcg.cartas.Entrenador;
import pokemontcg.cartas.Pokemon;
import pokemontcg.cartas.Soporte;
import pokemontcg.combate.Jugador;
import pokemontcg.datos.CatalogoEntrenadores;
import pokemontcg.datos.CatalogoPokemon;
import pokemontcg.reglas.ReglaCombate;
import pokemontcg.reglas.ValidacionMazo;

public class App {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ReglaCombate reglaCombate = new ReglaCombate();

        System.out.println("==========================================");
        System.out.println("   === INICIANDO POKEMON TCG POCKET ===   ");
        System.out.println("==========================================");

        // 1. Nombres Personalizados (RF5)
        System.out.print("Ingresa el nombre del Jugador 1: ");
        String n1 = scanner.nextLine();
        System.out.print("Ingresa el nombre del Jugador 2: ");
        String n2 = scanner.nextLine();

        Jugador ash = new Jugador(n1.isBlank() ? "Jugador 1" : n1);
        Jugador gary = new Jugador(n2.isBlank() ? "Jugador 2" : n2);

        // 2. Poblar Mazos desde Catálogos (Agregamos múltiples copias para asegurar cartas suficientes)
        for (int i = 0; i < 3; i++) {
            for (Pokemon p : CatalogoPokemon.obtenerPokemon()) {
                ash.getMazo().agregarCarta(p);
                gary.getMazo().agregarCarta(p);
            }
            for (Carta e : CatalogoEntrenadores.obtenerEntrenadores()) {
                ash.getMazo().agregarCarta(e);
                gary.getMazo().agregarCarta(e);
            }
        }

        // 3. Preparación Inicial (Mezclar y Repartir Mano)
        ValidacionMazo.repartirManoInicial(ash);
        ValidacionMazo.repartirManoInicial(gary);

        // Seleccionar Pokémon Activo Inicial desde la mano de cada jugador (RF3)
        seleccionarPokemonActivoInicial(ash, scanner);
        seleccionarPokemonActivoInicial(gary, scanner);

        // 4. Bucle Principal de Juego (Game Loop)
        Jugador actual = ash;
        Jugador oponente = gary;
        int turno = 1;
        boolean juegoActivo = true;

        while (juegoActivo) {
            // Regla RF2/RF3: Derrota si no hay cartas en el mazo al robar
            if (actual.getMazo().estaVacio()) {
                System.out.println("\n[DUELO] ¡" + actual.getNombre() + " no tiene cartas en el mazo para robar!");
                System.out.println("\n*** ¡" + oponente.getNombre() + " HA GANADO LA PARTIDA! ***");
                break;
            }

            actual.robarCarta();
            actual.agregarEnergiaZona();

            boolean energiaAdjuntadaEsteTurno = false;
            boolean soporteUsadoEsteTurno = false;
            boolean finTurno = false;

            while (!finTurno && juegoActivo) {
                mostrarEstadoCampo(turno, actual, oponente, energiaAdjuntadaEsteTurno);

                System.out.println("\n¿Qué deseas hacer?");
                System.out.println("1. Adjuntar energía de zona");
                System.out.println("2. Jugar carta de entrenador");
                System.out.println("3. Colocar Pokémon en banco");
                System.out.println("4. Retirar Pokémon activo");
                System.out.println("5. Atacar");
                System.out.println("6. Pasar turno");
                System.out.println("==========================================");
                System.out.print("Selecciona una opción (1-6): ");

                String opcion = scanner.nextLine();

                switch (opcion) {
                    case "1":
                    if (energiaAdjuntadaEsteTurno) {
                        System.out.println("[AVISO] Ya adjuntaste energía este turno.");
                    } else if (actual.getZonaEnergia() <= 0) {
                        System.out.println("[AVISO] No tienes energía disponible en la zona.");
                    } else if (actual.getPokemonActivo() == null) {
                        System.out.println("[AVISO] No tienes un Pokémon activo.");
                    } else {
                        String tipoEnergia = actual.getPokemonActivo().getCategoria(); 
                        
                        actual.getPokemonActivo().adjuntarEnergia(tipoEnergia);
                        actual.gastarEnergiaZona();
                        energiaAdjuntadaEsteTurno = true;
                        
                        System.out.println("[ENERGIA] ¡Energía " + tipoEnergia + " adjuntada a " + actual.getPokemonActivo().getNombre() + "!");
                    }
                    break;

                    case "2":
                        List<Integer> indicesEntrenadores = new ArrayList<>();
                        System.out.println("\n--- CARTAS DE ENTRENADOR EN MANO ---");
                        for (int i = 0; i < actual.getMano().getCartas().size(); i++) {
                            Carta c = actual.getMano().getCartas().get(i);
                            if (c instanceof Entrenador) {
                                indicesEntrenadores.add(i);
                                String tipoE = (c instanceof Soporte) ? "Soporte" : "Objeto";
                                System.out.println((indicesEntrenadores.size()) + ". " + c.getNombre() + " [" + tipoE + "]");
                            }
                        }

                        if (indicesEntrenadores.isEmpty()) {
                            System.out.println("[AVISO] No tienes cartas de Entrenador en la mano.");
                            break;
                        }

                        System.out.print("Selecciona el número de carta a jugar (0 para cancelar): ");
                        int idxSelE = Integer.parseInt(scanner.nextLine());

                        if (idxSelE > 0 && idxSelE <= indicesEntrenadores.size()) {
                            int indiceMano = indicesEntrenadores.get(idxSelE - 1);
                            Entrenador cartaE = (Entrenador) actual.getMano().getCartas().get(indiceMano);

                            if (cartaE instanceof Soporte && soporteUsadoEsteTurno) {
                                System.out.println("[RESTRICCION] Solo puedes jugar 1 carta de Soporte por turno.");
                                break;
                            }

                            actual.getMano().getCartas().remove(indiceMano);
                            if (cartaE instanceof Soporte) {
                                soporteUsadoEsteTurno = true;
                            }

                            System.out.println("[ENTRENADOR] Se jugó: " + cartaE.getNombre());
                            cartaE.activarEfecto(actual, actual.getPokemonActivo());
                        } else {
                            System.out.println("[CANCELADO] No se jugó ninguna carta.");
                        }
                        break;

                    case "3":
                        if (actual.getBanco().getPokemones().size() >= 2) {
                            System.out.println("[RESTRICCION] Tu banco ya tiene el límite de 2 Pokémon.");
                            break;
                        }

                        List<Integer> indicesPokemon = new ArrayList<>();
                        System.out.println("\n--- POKEMON BÁSICOS EN MANO ---");
                        for (int i = 0; i < actual.getMano().getCartas().size(); i++) {
                            Carta c = actual.getMano().getCartas().get(i);
                            if (c instanceof Pokemon) {
                                indicesPokemon.add(i);
                                System.out.println((indicesPokemon.size()) + ". " + c.getNombre() + " (" + ((Pokemon) c).getPuntosSalud() + " PS)");
                            }
                        }

                        if (indicesPokemon.isEmpty()) {
                            System.out.println("[AVISO] No tienes Pokémon en mano para enviar al banco.");
                            break;
                        }

                        System.out.print("Selecciona el Pokémon a enviar al banco (0 para cancelar): ");
                        int idxSelP = Integer.parseInt(scanner.nextLine());

                        if (idxSelP > 0 && idxSelP <= indicesPokemon.size()) {
                            int indiceMano = indicesPokemon.get(idxSelP - 1);
                            Pokemon pk = (Pokemon) actual.getMano().getCartas().remove(indiceMano);
                            actual.getBanco().agregarPokemon(pk);
                            System.out.println("[BANCO] ¡" + pk.getNombre() + " fue colocado en el Banco!");
                        } else {
                            System.out.println("[CANCELADO] Acción cancelada.");
                        }
                        break;

                    case "4":
                        if (actual.getBanco().getPokemones().isEmpty()) {
                            System.out.println("[AVISO] No tienes Pokémon en el banco para reemplazar al activo.");
                        } else if (actual.getPokemonActivo() == null) {
                            System.out.println("[AVISO] No tienes Pokémon activo.");
                        } else {
                            Pokemon activoActual = actual.getPokemonActivo();
                            int costoRet = activoActual.getCostoRetirada();

                            System.out.println("[RETIRO] " + activoActual.getNombre() + " tiene un costo de retirada de " + costoRet + " energía(s).");

                            if (activoActual.getEnergiasAdjuntas().size() < costoRet) {
                                System.out.println("[RESTRICCION] Se requieren " + costoRet + " energía(s) adjunta(s) para retirarse. (" +
                                                   activoActual.getEnergiasAdjuntas().size() + "/" + costoRet + ")");
                                break;
                            }

                            // Descartar las energías requeridas para la retirada
                            for (int k = 0; k < costoRet; k++) {
                                if (!activoActual.getEnergiasAdjuntas().isEmpty()) {
                                    activoActual.getEnergiasAdjuntas().remove(0);
                                }
                            }

                            System.out.println("\n--- SELECCIONA EL NUEVO POKÉMON ACTIVO DEL BANCO ---");
                            for (int b = 0; b < actual.getBanco().getPokemones().size(); b++) {
                                Pokemon pB = actual.getBanco().getPokemones().get(b);
                                System.out.println((b + 1) + ". " + pB.getNombre() + " (" + pB.getPuntosSalud() + " PS)");
                            }
                            System.out.print("Selecciona una opción: ");
                            int idxB = Integer.parseInt(scanner.nextLine()) - 1;

                            if (idxB >= 0 && idxB < actual.getBanco().getPokemones().size()) {
                                Pokemon nuevoActivo = actual.getBanco().getPokemones().remove(idxB);
                                actual.getBanco().agregarPokemon(activoActual);
                                actual.setPokemonActivo(nuevoActivo);
                                System.out.println("[RETIRO] ¡" + activoActual.getNombre() + " pagó su costo de retirada (" + costoRet + 
                                                   " energía(s)) y pasó al banco! Nuevo activo: " + nuevoActivo.getNombre());
                            } else {
                                System.out.println("[ERROR] Selección inválida.");
                            }
                        }
                        break;

                    case "5":
                        if (turno == 1) {
                            System.out.println("[RESTRICCION] El primer jugador no puede atacar en su primer turno.");
                        } else if (actual.getPokemonActivo() != null && oponente.getPokemonActivo() != null) {
                            Pokemon activoPropio = actual.getPokemonActivo();
                            Pokemon activoRival = oponente.getPokemonActivo();

                            if (activoPropio.getAtaques().isEmpty()) {
                                System.out.println("[AVISO] Tu Pokémon no tiene ataques registrados.");
                                break;
                            }

                            Ataque ataqueElegido = activoPropio.getAtaques().get(0);

                            // Delegar el ataque y la validación de energías a ReglaCombate
                            reglaCombate.atacar(actual, activoRival, ataqueElegido, oponente);

                            // Verificar KO y condiciones de victoria al finalizar la acción
                            if (activoRival.estaDebilitado()) {
                                if (reglaCombate.verificarGanador(actual, oponente)) {
                                    juegoActivo = false;
                                } else if (oponente.getBanco().estaVacio()) {
                                    System.out.println("[DUELO] ¡" + oponente.getNombre() + " no tiene Pokémon en el banco para reemplazar al activo!");
                                    System.out.println("\n*** ¡" + actual.getNombre() + " HA GANADO LA PARTIDA! ***");
                                    juegoActivo = false;
                                } else {
                                    Pokemon sustituto = oponente.getBanco().getPokemones().remove(0);
                                    oponente.setPokemonActivo(sustituto);
                                    System.out.println("[BANCO] ¡" + oponente.getNombre() + " promueve a " + sustituto.getNombre() + " al puesto de Pokémon activo!");
                                }
                            }
                            finTurno = true;
                        } else {
                            System.out.println("[AVISO] No hay Pokémon en posición de combate.");
                        }
                        break;

                    case "6":
                        System.out.println("[TURNO] Pasando turno...");
                        finTurno = true;
                        break;

                    default:
                        System.out.println("[ERROR] Opción inválida. Selecciona un número del 1 al 6.");
                        break;
                }
            }

            // Cambiar turnos
            Jugador temp = actual;
            actual = oponente;
            oponente = temp;
            turno++;
        }

        System.out.println("\n=== PARTIDA FINALIZADA ===");
    }

    private static void seleccionarPokemonActivoInicial(Jugador jugador, Scanner scanner) {
        List<Integer> indicesPokemon = new ArrayList<>();
        System.out.println("\n--- " + jugador.getNombre().toUpperCase() + ": ELIGE TU POKÉMON ACTIVO INICIAL ---");

        for (int i = 0; i < jugador.getMano().getCartas().size(); i++) {
            Carta c = jugador.getMano().getCartas().get(i);
            if (c instanceof Pokemon) {
                Pokemon p = (Pokemon) c;
                if (!p.esEx()) {
                    indicesPokemon.add(i);
                    System.out.println(indicesPokemon.size() + ". " + p.getNombre() + " (" + p.getPuntosSalud() + " PS)");
                }
            }
        }

        System.out.print("Selecciona el número del Pokémon que enviarás a combatir: ");
        int opcion = Integer.parseInt(scanner.nextLine());

        int indiceMano = indicesPokemon.get(opcion - 1);
        Pokemon activoElegido = (Pokemon) jugador.getMano().getCartas().remove(indiceMano);
        jugador.setPokemonActivo(activoElegido);

        System.out.println("[CAMPO] ¡" + activoElegido.getNombre() + " ingresa como Pokémon Activo de " + jugador.getNombre() + "!\n");
    }

    private static void mostrarEstadoCampo(int turno, Jugador actual, Jugador oponente, boolean energiaUsada) {
        System.out.println("\n======================================================================");
        System.out.println("TURNO " + turno + " - " + actual.getNombre());
        System.out.println("======================================================================");

        // --- RIVAL ---
        Pokemon actRival = oponente.getPokemonActivo();
        String infoActRival = (actRival != null)
                ? "[Activo] " + actRival.getNombre() + "   Energía adjunta: " + formatoEnergias(actRival) + " | Costo Retirada: " + actRival.getCostoRetirada()
                : "[Activo] Ninguno";
        String psRival = (actRival != null) ? actRival.getPuntosSalud() + "/" + actRival.getPuntosSaludMaximos() : "0/0";

        System.out.printf("RIVAL (%s)        PS: %s\n", oponente.getNombre(), psRival);
        System.out.println(infoActRival);

        StringBuilder bancoRival = new StringBuilder("[Banco] ");
        if (oponente.getBanco().getPokemones().isEmpty()) {
            bancoRival.append("(vacío)");
        } else {
            for (Pokemon p : oponente.getBanco().getPokemones()) {
                bancoRival.append(p.getNombre()).append(" (").append(p.getPuntosSalud()).append(" PS) ");
            }
        }
        System.out.println(bancoRival.toString());
        System.out.println("Puntos de premio rival: " + oponente.getPuntosPremio() + "/3");

        System.out.println("----------------------------------------------------------------------");

        // --- TU CAMPO ---
        Pokemon actPropio = actual.getPokemonActivo();
        String infoActPropio = (actPropio != null)
                ? "[Activo] " + actPropio.getNombre() + "   Energía adjunta: " + formatoEnergias(actPropio) + " | Costo Retirada: " + actPropio.getCostoRetirada()
                : "[Activo] Ninguno";
        String psPropio = (actPropio != null) ? actPropio.getPuntosSalud() + "/" + actPropio.getPuntosSaludMaximos() : "0/0";

        System.out.printf("TU CAMPO (%s)     PS: %s\n", actual.getNombre(), psPropio);
        System.out.println(infoActPropio);

        StringBuilder bancoPropio = new StringBuilder("[Banco] ");
        if (actual.getBanco().getPokemones().isEmpty()) {
            bancoPropio.append("(vacío)");
        } else {
            for (Pokemon p : actual.getBanco().getPokemones()) {
                bancoPropio.append(p.getNombre()).append(" (").append(p.getPuntosSalud()).append(" PS) ");
            }
        }
        System.out.println(bancoPropio.toString());
        System.out.println("Puntos de premio propios: " + actual.getPuntosPremio() + "/3");
        System.out.println("Zona de energía: (" + (energiaUsada ? "usada" : "disponible") + ")");

        // --- TU MANO ---
        System.out.println("\nTu mano (" + actual.getMano().getCartas().size() + " cartas):");
        for (int i = 0; i < actual.getMano().getCartas().size(); i++) {
            System.out.println("  " + (i + 1) + ". " + actual.getMano().getCartas().get(i).getNombre());
        }
        System.out.println("======================================================================");
    }

    private static String formatoEnergias(Pokemon p) {
        if (p == null) {
            return "0";
        }
        int cantidad = p.getEnergiasAdjuntas().size();
        return cantidad + " (" + p.getTipo() + ")";
    }
}