package pokemontcg.gui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

import pokemontcg.cartas.Carta;
import pokemontcg.cartas.Pokemon;
import pokemontcg.combate.Jugador;
import pokemontcg.reglas.ValidacionMazo;
import pokemontcg.reglas.ReglaCombate;
import pokemontcg.cartas.Ataque;
import pokemontcg.cartas.PokemonEvolucion;
import pokemontcg.reglas.ReglaEvolucion;
import pokemontcg.cartas.Entrenador;
import pokemontcg.cartas.Soporte;
import pokemontcg.cartas.Objeto;

public class TableroJuego extends JFrame {

    // --- Componentes de información Superior ---
    private JLabel lblTurno;
    private JLabel lblJugadorActivo;

    // --- Componentes del Rival (Norte) ---
    private JLabel lblRivalInfo;
    private JLabel lblRivalActivoNombre;
    private JProgressBar barRivalActivoPS;
    private JLabel lblRivalActivoEnergias;
    private JPanel panelRivalBanco;

    // --- Componentes Centrales (Registro de Eventos) ---
    private JTextArea txtLogEventos;

    // --- Componentes del Jugador Actual (Sur) ---
    private JLabel lblPropioInfo;
    private JLabel lblPropioActivoNombre;
    private JProgressBar barPropioActivoPS;
    private JLabel lblPropioActivoEnergias;
    private JPanel panelPropioBanco;
    private JPanel panelMano;

    // --- Botones de Acción ---
    private JButton btnAdjuntarEnergia;
    private JButton btnEvolucionar;
    private JButton btnAtacar;
    private JButton btnRetirar;
    private JButton btnPasarTurno;

    // Jugadores y control de turnos
    private Jugador jugadorActual;
    private Jugador rival;
    private int numeroTurno = 1;
    private int ultimoTurnoConSoporte = -1;
    private boolean energiaAdjuntadaEsteTurno = false;
    private Objeto turboVelocidadUsada = null;

    public TableroJuego(Jugador jugador1, Jugador jugador2) {
        configurarVentana();
        inicializarComponentes();

        // Guardar los jugadores
        this.jugadorActual = jugador1;
        this.rival = jugador2;

        // Iniciar el primer turno
        this.numeroTurno = 1;

        // Validar los dos mazos
        if (!ValidacionMazo.validarMazo(jugador1.getMazo())
                || !ValidacionMazo.validarMazo(jugador2.getMazo())) {

            JOptionPane.showMessageDialog(
                    this,
                    "Uno de los mazos no cumple las reglas del juego.",
                    "Mazo inválido",
                    JOptionPane.ERROR_MESSAGE);

            dispose();
            return;
        }

        // Barajar los mazos
        jugador1.getMazo().barajar();
        jugador2.getMazo().barajar();

        // Repartir cinco cartas a cada jugador
        ValidacionMazo.repartirManoInicial(jugador1);
        ValidacionMazo.repartirManoInicial(jugador2);

        // Elegir el Pokémon activo de cada jugador
        if (!elegirPokemonActivo(jugador1)) {
            dispose();
            return;
        }

        if (!elegirPokemonActivo(jugador2)) {
            dispose();
            return;
        }

        // Preparar la energía inicial de cada jugador
        jugador1.agregarEnergiaZona();
        jugador2.agregarEnergiaZona();

        // El primer jugador roba una carta al comenzar su turno
        jugadorActual.robarCarta();
        registrarEvento(
                jugadorActual.getNombre() + " robó una carta al comenzar su turno.");

        // Actualizar el tablero
        actualizarTablero(jugadorActual, rival, numeroTurno);

        // Conectar el botón para pasar el turno
        btnPasarTurno.addActionListener(e -> cambiarTurno());

        // Conectar el botón para adjuntar energía
        btnAdjuntarEnergia.addActionListener(e -> adjuntarEnergiaAlActivo());

        // Conectar el botón para atacar
        btnAtacar.addActionListener(e -> realizarAtaque());

        // Conectar el botón para evolucionar
        btnEvolucionar.addActionListener(e -> evolucionarPokemon());

        // Conectar el botón para retirar Pokémon
        btnRetirar.addActionListener(e -> retirarPokemon());
    }

    private boolean elegirPokemonActivo(Jugador jugador) {

        while (jugador.getPokemonActivo() == null) {

            List<Carta> cartas = jugador.getMano().getCartas();
            List<Pokemon> pokemonDisponibles = new java.util.ArrayList<>();

            for (Carta carta : cartas) {
                if (carta instanceof Pokemon) {
                    Pokemon pokemon = (Pokemon) carta;
                    if (pokemon.esBasico()) {
                        pokemonDisponibles.add(pokemon);
                    }
                }
            }

            if (pokemonDisponibles.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        jugador.getNombre() + " no tiene Pokémon Básicos en su mano.",
                        "Sin Pokémon Básicos",
                        JOptionPane.WARNING_MESSAGE);
                return false;
            }

            String[] opciones = new String[pokemonDisponibles.size()];

            for (int i = 0; i < pokemonDisponibles.size(); i++) {
                Pokemon pokemon = pokemonDisponibles.get(i);
                opciones[i] = pokemon.getNombre() + " - "
                        + pokemon.getPuntosSalud() + " PS";
            }

            String seleccion = (String) JOptionPane.showInputDialog(
                    this,
                    jugador.getNombre() + ", elige tu Pokémon Activo:",
                    "Elegir Pokémon Activo",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]);

            if (seleccion == null) {
                return false;
            }

            int indice = -1;

            for (int i = 0; i < opciones.length; i++) {
                if (opciones[i].equals(seleccion)) {
                    indice = i;
                    break;
                }
            }

            if (indice >= 0) {
                Pokemon elegido = pokemonDisponibles.get(indice);
                jugador.setPokemonActivo(elegido);
                jugador.getMano().getCartas().remove(elegido);
            }
        }

        return true;
    }

    private void jugarPokemonDesdeMano(int indiceCarta) {
        List<Carta> cartas = jugadorActual.getMano().getCartas();

        if (indiceCarta < 0 || indiceCarta >= cartas.size()) {
            return;
        }

        Carta carta = cartas.get(indiceCarta);

        if (!(carta instanceof Pokemon)) {
            return;
        }

        Pokemon pokemon = (Pokemon) carta;

        if (!pokemon.esBasico()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Solo puedes colocar Pokémon Básicos.",
                    "No se puede jugar",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (jugadorActual.getPokemonActivo() == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Primero necesitas un Pokémon activo.",
                    "No se puede jugar",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean agregado = jugadorActual.getBanco()
                .agregarDesdeMano(pokemon, numeroTurno);

        if (agregado) {
            jugadorActual.getMano().removerCarta(indiceCarta);

            registrarEvento(
                    jugadorActual.getNombre() + " colocó a "
                            + pokemon.getNombre() + " en el banco.");

            actualizarTablero(jugadorActual, rival, numeroTurno);
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "No puedes colocar más Pokémon en el banco.",
                    "Banco lleno",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void adjuntarEnergiaAlActivo() {
        if (energiaAdjuntadaEsteTurno) {
            JOptionPane.showMessageDialog(
                    this,
                    "Ya adjuntaste una energía durante este turno.",
                    "Energía utilizada",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Pokemon> disponibles = new java.util.ArrayList<>();

        // Agregar el Pokémon activo
        if (jugadorActual.getPokemonActivo() != null) {
            disponibles.add(jugadorActual.getPokemonActivo());
        }

        // Agregar los Pokémon del banco
        disponibles.addAll(jugadorActual.getBanco().getPokemones());

        if (disponibles.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "No tienes Pokémon a los que puedas adjuntar energía.",
                    "Sin Pokémon",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] opciones = new String[disponibles.size()];

        for (int i = 0; i < disponibles.size(); i++) {
            Pokemon pokemon = disponibles.get(i);
            opciones[i] = pokemon.getNombre() + " - " + pokemon.getTipo();
        }

        String seleccion = (String) JOptionPane.showInputDialog(
                this,
                "Selecciona el Pokémon que recibirá la energía:",
                "Adjuntar energía",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);

        if (seleccion == null) {
            return;
        }

        int indice = -1;

        for (int i = 0; i < opciones.length; i++) {
            if (opciones[i].equals(seleccion)) {
                indice = i;
                break;
            }
        }

        if (indice == -1) {
            return;
        }

        Pokemon objetivo = disponibles.get(indice);

        objetivo.adjuntarEnergia(objetivo.getTipo());
        jugadorActual.gastarEnergiaZona();
        energiaAdjuntadaEsteTurno = true;

        registrarEvento(
                jugadorActual.getNombre() + " adjuntó una energía a "
                        + objetivo.getNombre() + ".");

        actualizarTablero(jugadorActual, rival, numeroTurno);
    }

    private void realizarAtaque() {
        Pokemon atacante = jugadorActual.getPokemonActivo();
        Pokemon objetivo = rival.getPokemonActivo();

        if (atacante == null || objetivo == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "No hay Pokémon disponibles para combatir.",
                    "No se puede atacar",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Ataque> ataques = atacante.getAtaques();

        if (ataques.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Este Pokémon no tiene ataques disponibles.",
                    "Sin ataques",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] opciones = new String[ataques.size()];

        for (int i = 0; i < ataques.size(); i++) {
            Ataque ataque = ataques.get(i);

            opciones[i] = ataque.getNombre()
                    + " - Daño: " + ataque.getDanioBase()
                    + " - Energía: " + ataque.getCostoEnergia();
        }

        String seleccion = (String) JOptionPane.showInputDialog(
                this,
                "Selecciona el ataque que deseas utilizar:",
                "Elegir ataque",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);

        if (seleccion == null) {
            return;
        }

        int indice = -1;

        for (int i = 0; i < opciones.length; i++) {
            if (opciones[i].equals(seleccion)) {
                indice = i;
                break;
            }
        }

        if (indice < 0) {
            return;
        }

        Ataque ataqueElegido = ataques.get(indice);
        ReglaCombate reglas = new ReglaCombate();

        if (!reglas.puedeAtacar(atacante, ataqueElegido)) {
            JOptionPane.showMessageDialog(
                    this,
                    "No tienes suficientes energías para realizar este ataque.",
                    "Energía insuficiente",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        reglas.atacar(
                jugadorActual,
                objetivo,
                ataqueElegido,
                rival,
                this::registrarEvento);

        if (objetivo.estaDebilitado()) {
            // Retirar el Pokémon derrotado del campo
            rival.setPokemonActivo(null);

            // Comprobar si el rival tiene Pokémon en el banco
            if (!rival.getBanco().estaVacio()) {
                Pokemon siguiente = rival.getBanco().retirarPokemon(0);
                rival.setPokemonActivo(siguiente);

                registrarEvento(
                        rival.getNombre() + " ahora tiene a "
                                + siguiente.getNombre() + " como Pokémon activo.");
            }
        }

        // Actualizar el tablero antes de mostrar el resultado
        actualizarTablero(jugadorActual, rival, numeroTurno);

        if (reglas.verificarGanador(jugadorActual, rival)) {
            String condicionVictoria = jugadorActual.getPuntosPremio() >= 3
                    ? "¡Consiguió 3 puntos de premio!"
                    : "¡Su rival ya no puede continuar la partida!";

            registrarEvento(
                    "¡" + jugadorActual.getNombre() + " ha ganado la partida!");

            boolean jugarOtraVez = DialogoFinPartida.mostrar(
                    this,
                    jugadorActual.getNombre(),
                    condicionVictoria);

            if (jugarOtraVez) {
                dispose();
                new VentanaInicio().setVisible(true);
            } else {
                dispose();
            }
            return;
        }
        actualizarTablero(jugadorActual, rival, numeroTurno);
    }

    private void evolucionarPokemon() {

        // Obtener los Pokémon que están en juego
        List<Pokemon> objetivos = new java.util.ArrayList<>();

        if (jugadorActual.getPokemonActivo() != null) {
            objetivos.add(jugadorActual.getPokemonActivo());
        }

        objetivos.addAll(jugadorActual.getBanco().getPokemones());

        if (objetivos.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "No tienes Pokémon en juego para evolucionar.",
                    "Sin Pokémon",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Buscar cartas de evolución en la mano
        List<PokemonEvolucion> evoluciones = new java.util.ArrayList<>();

        for (Carta carta : jugadorActual.getMano().getCartas()) {
            if (carta instanceof PokemonEvolucion) {
                evoluciones.add((PokemonEvolucion) carta);
            }
        }

        if (evoluciones.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "No tienes cartas de evolución en tu mano.",
                    "Sin evoluciones",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Mostrar los Pokémon que se pueden seleccionar
        String[] opcionesPokemon = new String[objetivos.size()];

        for (int i = 0; i < objetivos.size(); i++) {
            Pokemon pokemon = objetivos.get(i);

            String zona = pokemon == jugadorActual.getPokemonActivo()
                    ? "Activo"
                    : "Banco";

            opcionesPokemon[i] = pokemon.getNombre()
                    + " - " + zona;
        }

        String seleccionPokemon = (String) JOptionPane.showInputDialog(
                this,
                "Selecciona el Pokémon que deseas evolucionar:",
                "Elegir Pokémon",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcionesPokemon,
                opcionesPokemon[0]);

        if (seleccionPokemon == null) {
            return;
        }

        int indicePokemon = -1;

        for (int i = 0; i < opcionesPokemon.length; i++) {
            if (opcionesPokemon[i].equals(seleccionPokemon)) {
                indicePokemon = i;
                break;
            }
        }

        if (indicePokemon < 0) {
            return;
        }

        Pokemon objetivo = objetivos.get(indicePokemon);

        // Mostrar las cartas de evolución disponibles
        String[] opcionesEvolucion = new String[evoluciones.size()];

        for (int i = 0; i < evoluciones.size(); i++) {
            PokemonEvolucion evolucion = evoluciones.get(i);

            opcionesEvolucion[i] = evolucion.getNombre()
                    + " - Evoluciona de "
                    + evolucion.getNombrePreEvolucion();
        }

        String seleccionEvolucion = (String) JOptionPane.showInputDialog(
                this,
                "Selecciona la carta de evolución:",
                "Elegir evolución",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcionesEvolucion,
                opcionesEvolucion[0]);

        if (seleccionEvolucion == null) {
            return;
        }

        int indiceEvolucion = -1;

        for (int i = 0; i < opcionesEvolucion.length; i++) {
            if (opcionesEvolucion[i].equals(seleccionEvolucion)) {
                indiceEvolucion = i;
                break;
            }
        }

        if (indiceEvolucion < 0) {
            return;
        }

        PokemonEvolucion cartaElegida = evoluciones.get(indiceEvolucion);

        // Aplicar las reglas de evolución
        ReglaEvolucion reglas = new ReglaEvolucion();

        String error = reglas.evolucionarPokemon(
                jugadorActual,
                objetivo,
                cartaElegida,
                numeroTurno);

        if (error != null) {
            JOptionPane.showMessageDialog(
                    this,
                    error,
                    "No se puede evolucionar",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        registrarEvento(
                jugadorActual.getNombre()
                        + " evolucionó a "
                        + objetivo.getNombre()
                        + " en "
                        + cartaElegida.getNombre() + ".");

        actualizarTablero(jugadorActual, rival, numeroTurno);
    }

    private void jugarEntrenadorDesdeMano(int indice) {
        Carta carta = jugadorActual.getMano().getCartas().get(indice);

        if (!(carta instanceof Entrenador)) {
            return;
        }

        Entrenador entrenador = (Entrenador) carta;

        // Comprobar el lí­mite de cartas de soporte
        if (entrenador instanceof Soporte
                && ultimoTurnoConSoporte == numeroTurno) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ya utilizaste una carta de soporte en este turno.",
                    "Lí­mite de soporte",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Pokemon objetivo = null;

        // Seleccionar Pokémon para utilizar Misty
        if (entrenador instanceof Soporte
                && entrenador.getNombre().equals("Misty")) {

            List<Pokemon> opcionesPokemon = new java.util.ArrayList<>();

            Pokemon activo = jugadorActual.getPokemonActivo();

            if (activo != null
                    && activo.getTipo().equalsIgnoreCase("Agua")) {
                opcionesPokemon.add(activo);
            }

            for (Pokemon pokemon : jugadorActual.getBanco().getPokemones()) {
                if (pokemon.getTipo().equalsIgnoreCase("Agua")) {
                    opcionesPokemon.add(pokemon);
                }
            }

            if (opcionesPokemon.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "No tienes Pokémon de tipo Agua para utilizar Misty.",
                        "No se puede utilizar",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            String[] nombres = new String[opcionesPokemon.size()];

            for (int i = 0; i < opcionesPokemon.size(); i++) {
                nombres[i] = opcionesPokemon.get(i).getNombre();
            }

            String seleccion = (String) JOptionPane.showInputDialog(
                    this,
                    "Selecciona el Pokémon que recibirá las energías:",
                    "Misty",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    nombres,
                    nombres[0]);

            if (seleccion == null) {
                return;
            }

            for (Pokemon pokemon : opcionesPokemon) {
                if (pokemon.getNombre().equals(seleccion)) {
                    objetivo = pokemon;
                    break;
                }
            }
        }

        // Retirar la carta de la mano y activar su efecto
        jugadorActual.getMano().removerCarta(indice);

        entrenador.activarEfecto(jugadorActual, objetivo);

        if (entrenador instanceof Objeto
                && entrenador.getNombre().equals("Turbo Velocidad")) {

            Objeto turbo = (Objeto) entrenador;

            if (turbo.isTurboActivo()) {
                turboVelocidadUsada = turbo;
            }
        }

        if (entrenador instanceof Soporte) {
            ultimoTurnoConSoporte = numeroTurno;
        }

        registrarEvento(
                jugadorActual.getNombre()
                        + " utilizó " + entrenador.getNombre());

        actualizarTablero(jugadorActual, rival, numeroTurno);
    }

    private void retirarPokemon() {

        Pokemon activo = jugadorActual.getPokemonActivo();

        if (activo == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "No tienes un Pokémon activo para retirar.",
                    "Sin Pokémon activo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Pokemon> banco = jugadorActual.getBanco().getPokemones();

        if (banco.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "No tienes Pokémon en el banco para reemplazar al activo.",
                    "Banco vacío",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Comprobar el costo de retirada
        int costoRetirada = activo.getCostoRetirada();
        if (turboVelocidadUsada != null
                && turboVelocidadUsada.isTurboActivo()
                && turboVelocidadUsada.getPokemonAfectado() == activo) {

            costoRetirada = Math.max(0, costoRetirada - 1);
        }

        int energiasDisponibles = activo.getEnergiasAdjuntas().size();

        if (energiasDisponibles < costoRetirada) {
            JOptionPane.showMessageDialog(
                    this,
                    activo.getNombre() + " necesita "
                            + costoRetirada + " energía(s) para retirarse, "
                            + "pero solo tiene " + energiasDisponibles + ".",
                    "Energía insuficiente",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Mostrar los Pokémon disponibles en el banco
        String[] opciones = new String[banco.size()];

        for (int i = 0; i < banco.size(); i++) {
            Pokemon pokemon = banco.get(i);

            opciones[i] = pokemon.getNombre()
                    + " - " + pokemon.getPuntosSalud() + " PS"
                    + " - Energías: "
                    + pokemon.getEnergiasAdjuntas().size();
        }

        String seleccion = (String) JOptionPane.showInputDialog(
                this,
                "Selecciona el Pokémon que entrará al campo:",
                "Retirar Pokémon",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);

        if (seleccion == null) {
            return;
        }

        int indice = -1;

        for (int i = 0; i < opciones.length; i++) {
            if (opciones[i].equals(seleccion)) {
                indice = i;
                break;
            }
        }

        if (indice < 0) {
            return;
        }

        // Descartar las energías necesarias para la retirada
        for (int i = 0; i < costoRetirada; i++) {
            String energia = activo.getEnergiasAdjuntas().get(0);
            activo.descartarEnergia(energia);
        }

        // Retirar el Pokémon elegido del banco
        Pokemon nuevoActivo = jugadorActual.getBanco().retirarPokemon(indice);

        // Enviar el anterior activo al banco
        jugadorActual.getBanco().agregarPokemon(activo);

        // Establecer el nuevo Pokémon activo
        jugadorActual.setPokemonActivo(nuevoActivo);

        registrarEvento(
                jugadorActual.getNombre()
                        + " retiró a " + activo.getNombre()
                        + " pagando " + costoRetirada + " energía(s). "
                        + nuevoActivo.getNombre() + " es ahora su Pokémon activo.");

        actualizarTablero(jugadorActual, rival, numeroTurno);
    }

    private void configurarVentana() {
        setTitle("Pokémon TCG Pocket - Combate");
        setSize(1100, 780);
        setMinimumSize(new Dimension(950, 680));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
    }

    private void inicializarComponentes() {
        // 1. BARRA SUPERIOR (Turno y Jugador Activo)
        JPanel panelCabecera = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 8));
        panelCabecera.setBackground(new Color(40, 44, 52));

        lblTurno = new JLabel("Turno: 1");
        lblTurno.setForeground(Color.WHITE);
        lblTurno.setFont(new Font("SansSerif", Font.BOLD, 16));

        lblJugadorActivo = new JLabel("Turno de: ---");
        lblJugadorActivo.setForeground(new Color(255, 215, 0));
        lblJugadorActivo.setFont(new Font("SansSerif", Font.BOLD, 16));

        panelCabecera.add(lblTurno);
        panelCabecera.add(lblJugadorActivo);

        // 2. ZONA RIVAL
        JPanel panelRival = crearZonaRival();

        JPanel contenedorNorte = new JPanel(new BorderLayout());
        contenedorNorte.add(panelCabecera, BorderLayout.NORTH);
        contenedorNorte.add(panelRival, BorderLayout.CENTER);
        add(contenedorNorte, BorderLayout.NORTH);

        // 3. ZONA CENTRAL (Registro de eventos con scroll)
        txtLogEventos = new JTextArea();
        txtLogEventos.setEditable(false);
        txtLogEventos.setLineWrap(true);
        txtLogEventos.setWrapStyleWord(true);
        txtLogEventos.setFont(new Font("Monospaced", Font.PLAIN, 13));
        txtLogEventos.setText("=== COMIENZA EL COMBATE ===\n");

        JScrollPane scrollLog = new JScrollPane(txtLogEventos);
        scrollLog.setBorder(new TitledBorder("Registro de Eventos"));
        add(scrollLog, BorderLayout.CENTER);

        // 4. ZONA JUGADOR ACTUAL
        JPanel panelJugador = crearZonaJugador();
        add(panelJugador, BorderLayout.SOUTH);
    }

    private JPanel crearZonaRival() {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setBorder(new TitledBorder("Campo del Rival"));
        panel.setBackground(new Color(245, 235, 235));

        lblRivalInfo = new JLabel("Rival: --- | Puntos: 0/3 | Mazo: 0 cartas", SwingConstants.CENTER);
        lblRivalInfo.setFont(new Font("SansSerif", Font.BOLD, 13));
        panel.add(lblRivalInfo, BorderLayout.NORTH);

        JPanel panelCampoRival = new JPanel(new GridLayout(1, 2, 15, 0));
        panelCampoRival.setOpaque(false);

        JPanel subActivo = new JPanel(new GridLayout(3, 1));
        subActivo.setBorder(new TitledBorder("Pokémon Activo Rival"));
        lblRivalActivoNombre = new JLabel("Sin Pokémon Activo", SwingConstants.CENTER);
        lblRivalActivoNombre.setFont(new Font("SansSerif", Font.BOLD, 13));

        barRivalActivoPS = new JProgressBar(0, 100);
        barRivalActivoPS.setStringPainted(true);
        barRivalActivoPS.setString("0 / 0 PS");

        lblRivalActivoEnergias = new JLabel("Energías adjuntas: 0", SwingConstants.CENTER);

        subActivo.add(lblRivalActivoNombre);
        subActivo.add(barRivalActivoPS);
        subActivo.add(lblRivalActivoEnergias);

        panelRivalBanco = new JPanel(new GridLayout(1, 3, 5, 5));
        panelRivalBanco.setBorder(new TitledBorder("Banco Rival (Máx. 3)"));
        for (int i = 0; i < 3; i++) {
            panelRivalBanco.add(crearSlotVacio());
        }

        panelCampoRival.add(subActivo);
        panelCampoRival.add(panelRivalBanco);
        panel.add(panelCampoRival, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearZonaJugador() {
        JPanel panel = new JPanel(new BorderLayout(10, 8));
        panel.setBorder(new TitledBorder("Tu Campo"));
        panel.setBackground(new Color(235, 242, 250));

        lblPropioInfo = new JLabel("Jugador: --- | Puntos: 0/3 | Mazo: 0 cartas | Energía del Turno: 0",
                SwingConstants.CENTER);
        lblPropioInfo.setFont(new Font("SansSerif", Font.BOLD, 13));
        panel.add(lblPropioInfo, BorderLayout.NORTH);

        JPanel panelCampoPropio = new JPanel(new GridLayout(1, 2, 15, 0));
        panelCampoPropio.setOpaque(false);

        JPanel subActivo = new JPanel(new GridLayout(3, 1));
        subActivo.setBorder(new TitledBorder("Tu Pokémon Activo"));
        lblPropioActivoNombre = new JLabel("Sin Pokémon Activo", SwingConstants.CENTER);
        lblPropioActivoNombre.setFont(new Font("SansSerif", Font.BOLD, 13));

        barPropioActivoPS = new JProgressBar(0, 100);
        barPropioActivoPS.setStringPainted(true);
        barPropioActivoPS.setString("0 / 0 PS");

        lblPropioActivoEnergias = new JLabel("Energías adjuntas: 0", SwingConstants.CENTER);

        subActivo.add(lblPropioActivoNombre);
        subActivo.add(barPropioActivoPS);
        subActivo.add(lblPropioActivoEnergias);

        panelPropioBanco = new JPanel(new GridLayout(1, 3, 5, 5));
        panelPropioBanco.setBorder(new TitledBorder("Tu Banco (Máx. 3)"));
        for (int i = 0; i < 3; i++) {
            panelPropioBanco.add(crearSlotVacio());
        }

        panelCampoPropio.add(subActivo);
        panelCampoPropio.add(panelPropioBanco);
        panel.add(panelCampoPropio, BorderLayout.CENTER);

        JPanel panelControles = new JPanel(new BorderLayout(5, 5));
        panelControles.setOpaque(false);

        panelMano = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));
        panelMano.setBorder(new TitledBorder("Tu Mano (Haz clic en una carta para jugarla)"));
        panelMano.setPreferredSize(new Dimension(0, 90));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 5));
        btnAdjuntarEnergia = new JButton("Adjuntar Energía");
        btnEvolucionar = new JButton("Evolucionar");
        btnAtacar = new JButton("Atacar");
        btnRetirar = new JButton("Retirar");
        btnPasarTurno = new JButton("Pasar Turno");

        panelBotones.add(btnAdjuntarEnergia);
        panelBotones.add(btnEvolucionar);
        panelBotones.add(btnAtacar);
        panelBotones.add(btnRetirar);
        panelBotones.add(btnPasarTurno);

        panelControles.add(panelMano, BorderLayout.CENTER);
        panelControles.add(panelBotones, BorderLayout.SOUTH);

        panel.add(panelControles, BorderLayout.SOUTH);

        return panel;
    }

    // MÉTODOS DE ACTUALIZACIÓN VISUAL CON EL MODELO REAL

    public void actualizarTablero(Jugador jugadorActual, Jugador rival, int numeroTurno) {
        if (jugadorActual == null || rival == null) {
            return;
        }

        // 1. Cabecera
        lblTurno.setText("Turno: " + numeroTurno);
        lblJugadorActivo.setText("Turno de: " + jugadorActual.getNombre());

        // 2. Datos del Rival
        int cartasMazoRival = rival.getMazo().getCartas().size();
        lblRivalInfo.setText(String.format("Rival: %s | Puntos: %d/3 | Mazo: %d cartas",
                rival.getNombre(), rival.getPuntosPremio(), cartasMazoRival));
        actualizarPokemonActivoUI(rival.getPokemonActivo(), lblRivalActivoNombre, barRivalActivoPS,
                lblRivalActivoEnergias);
        actualizarBancoUI(rival.getBanco().getPokemones(), panelRivalBanco);

        // 3. Datos del Jugador Actual
        String tipoEnergia = (jugadorActual.getPokemonActivo() != null)
                ? jugadorActual.getPokemonActivo().getTipo()
                : "Sin tipo";

        String estadoEnergia = (jugadorActual.getZonaEnergia() > 0)
                ? "Disponible (" + jugadorActual.getZonaEnergia() + ") - " + tipoEnergia
                : "Ya usada / 0";

        lblPropioInfo.setText(String.format("Jugador: %s | Puntos: %d/3 | Mazo: %d cartas | Energía del Turno: %s",
                jugadorActual.getNombre(), jugadorActual.getPuntosPremio(),
                jugadorActual.getMazo().getCartas().size(), estadoEnergia));
        actualizarPokemonActivoUI(jugadorActual.getPokemonActivo(), lblPropioActivoNombre, barPropioActivoPS,
                lblPropioActivoEnergias);
        actualizarBancoUI(jugadorActual.getBanco().getPokemones(), panelPropioBanco);

        // 4. Mano interactiva del Jugador Actual
        actualizarManoUI(jugadorActual.getMano().getCartas());

        // 5. Habilitar/Deshabilitar botones básicos según estado
        btnAdjuntarEnergia.setEnabled(jugadorActual.getZonaEnergia() > 0 && jugadorActual.getPokemonActivo() != null);
        btnAtacar.setEnabled(
                jugadorActual.getPokemonActivo() != null && !jugadorActual.getPokemonActivo().getAtaques().isEmpty());
        btnRetirar.setEnabled(jugadorActual.getPokemonActivo() != null && !jugadorActual.getBanco().estaVacio());

        revalidate();
        repaint();
    }

    private void actualizarPokemonActivoUI(Pokemon p, JLabel lblNombre, JProgressBar barPS, JLabel lblEnergias) {
        if (p == null) {
            lblNombre.setText("Sin Pokémon Activo");
            barPS.setMaximum(100);
            barPS.setValue(0);
            barPS.setString("0 / 0 PS");
            barPS.setForeground(Color.GRAY);
            lblEnergias.setText("Energías adjuntas: 0");
            return;
        }

        String etiquetaEx = p.esEx() ? " [EX]" : "";
        lblNombre.setText(String.format("%s%s (%s - %s)", p.getNombre(), etiquetaEx, p.getTipo(), p.getEtapa()));

        barPS.setMaximum(p.getPuntosSaludMaximos());
        barPS.setValue(p.getPuntosSalud());
        barPS.setString(p.getPuntosSalud() + " / " + p.getPuntosSaludMaximos() + " PS");

        // Color dinámico segúń porcentaje de vida
        double porcentaje = (double) p.getPuntosSalud() / p.getPuntosSaludMaximos();
        if (porcentaje > 0.5) {
            barPS.setForeground(new Color(46, 160, 67)); // Verde
        } else if (porcentaje > 0.25) {
            barPS.setForeground(new Color(210, 153, 34)); // Amarillo
        } else {
            barPS.setForeground(new Color(218, 54, 51)); // Rojo
        }

        lblEnergias.setText(p.getTextoEnergiasAdjuntas());
    }

    private void actualizarBancoUI(List<Pokemon> pokemonesBanco, JPanel panelBanco) {
        panelBanco.removeAll();
        for (int i = 0; i < 3; i++) {
            if (i < pokemonesBanco.size()) {
                Pokemon p = pokemonesBanco.get(i);
                JPanel tarjetaBanco = new JPanel(new GridLayout(3, 1));
                tarjetaBanco.setBorder(BorderFactory.createLineBorder(new Color(70, 100, 140), 1));
                tarjetaBanco.setBackground(Color.WHITE);

                JLabel lblNom = new JLabel(p.getNombre() + " (" + p.getEtapa() + ")", SwingConstants.CENTER);
                lblNom.setFont(new Font("SansSerif", Font.BOLD, 11));

                JProgressBar barMini = new JProgressBar(0, p.getPuntosSaludMaximos());
                barMini.setValue(p.getPuntosSalud());
                barMini.setStringPainted(true);
                barMini.setString(p.getPuntosSalud() + "/" + p.getPuntosSaludMaximos());

                JLabel lblEn = new JLabel("Energías: " + p.getEnergiasAdjuntas().size(), SwingConstants.CENTER);
                lblEn.setFont(new Font("SansSerif", Font.PLAIN, 11));

                tarjetaBanco.add(lblNom);
                tarjetaBanco.add(barMini);
                tarjetaBanco.add(lblEn);
                panelBanco.add(tarjetaBanco);
            } else {
                panelBanco.add(crearSlotVacio());
            }
        }
    }

    private void actualizarManoUI(List<Carta> cartasMano) {
        panelMano.removeAll();
        if (cartasMano.isEmpty()) {
            panelMano.add(new JLabel("No tienes cartas en la mano."));
            return;
        }

        for (int i = 0; i < cartasMano.size(); i++) {
            final int indice = i;
            final Carta carta = cartasMano.get(i);

            String subtitulo = (carta instanceof Pokemon)
                    ? ((Pokemon) carta).getEtapa() + " | " + ((Pokemon) carta).getPuntosSalud() + " PS"
                    : carta.getCategoria();

            JButton btnCarta = new JButton("<html><center><b>" + carta.getNombre() + "</b><br><small>" + subtitulo
                    + "</small></center></html>");
            btnCarta.setPreferredSize(new Dimension(135, 52));
            btnCarta.setFocusPainted(false);

            btnCarta.addActionListener(e -> {
                if (carta instanceof Entrenador) {
                    jugarEntrenadorDesdeMano(indice);
                } else if (carta instanceof Pokemon) {
                    jugarPokemonDesdeMano(indice);
                }
            });

            panelMano.add(btnCarta);
        }
    }

    private JLabel crearSlotVacio() {
        JLabel slot = new JLabel("vacío", SwingConstants.CENTER);
        slot.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        slot.setForeground(Color.GRAY);
        return slot;
    }

    public void registrarEvento(String mensaje) {
        txtLogEventos.append(mensaje + "\n");
        txtLogEventos.setCaretPosition(txtLogEventos.getDocument().getLength());
    }

    // Cambiar el turno del jugador
    private void cambiarTurno() {
        turboVelocidadUsada = null;

        // Intercambiar los jugadores
        Jugador temporal = jugadorActual;
        jugadorActual = rival;
        rival = temporal;

        // Aumentar el número del turno
        numeroTurno++;
        energiaAdjuntadaEsteTurno = false;
        // Entregar una energía al jugador que comienza su turno
        jugadorActual.agregarEnergiaZona();
        // Ocultar la mano durante el cambio de turno
        panelMano.removeAll();
        panelMano.revalidate();
        panelMano.repaint();

        // Mostrar el diálogo para que el siguiente jugador continúe
        DialogoCambioTurno.mostrar(this, jugadorActual.getNombre());

        if (jugadorActual.getMazo().estaVacio()) {

            registrarEvento(
                    jugadorActual.getNombre() + " se quedó sin cartas en el mazo.");

            boolean jugarOtraVez = DialogoFinPartida.mostrar(
                    this,
                    rival.getNombre(),
                    jugadorActual.getNombre() + " se quedó sin cartas en el mazo.");

            if (jugarOtraVez) {
                dispose();
                new VentanaInicio().setVisible(true);
            } else {
                dispose();
            }

            return;
        }

        // Robar una carta al comenzar el turno
        jugadorActual.robarCarta();

        registrarEvento(
                jugadorActual.getNombre() + " robó una carta al comenzar su turno.");

        // Actualizar el tablero después de robar
        actualizarTablero(jugadorActual, rival, numeroTurno);
    }
}