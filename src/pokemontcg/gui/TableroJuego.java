package pokemontcg.gui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

import pokemontcg.cartas.Carta;
import pokemontcg.cartas.Pokemon;
import pokemontcg.combate.Jugador;

public class TableroJuego extends JFrame {

    // --- Componentes de Información Superior ---
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

    // Listener externo para las cartas de la mano
    private CartaClickListener cartaClickListener;

    public interface CartaClickListener {
        void onCartaSeleccionada(int indiceCarta, Carta carta);
    }

    public TableroJuego() {
        configurarVentana();
        inicializarComponentes();
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

        lblPropioInfo = new JLabel("Jugador: --- | Puntos: 0/3 | Mazo: 0 cartas | Energía del Turno: 0", SwingConstants.CENTER);
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
        actualizarPokemonActivoUI(rival.getPokemonActivo(), lblRivalActivoNombre, barRivalActivoPS, lblRivalActivoEnergias);
        actualizarBancoUI(rival.getBanco().getPokemones(), panelRivalBanco);

        // 3. Datos del Jugador Actual
        int cartasMazoPropio = jugadorActual.getMazo().getCartas().size();
        String estadoEnergia = (jugadorActual.getZonaEnergia() > 0)
                ? "Disponible (" + jugadorActual.getZonaEnergia() + ")"
                : "Ya usada / 0";
        lblPropioInfo.setText(String.format("Jugador: %s | Puntos: %d/3 | Mazo: %d cartas | Energía del Turno: %s",
                jugadorActual.getNombre(), jugadorActual.getPuntosPremio(), cartasMazoPropio, estadoEnergia));
        actualizarPokemonActivoUI(jugadorActual.getPokemonActivo(), lblPropioActivoNombre, barPropioActivoPS, lblPropioActivoEnergias);
        actualizarBancoUI(jugadorActual.getBanco().getPokemones(), panelPropioBanco);

        // 4. Mano interactiva del Jugador Actual
        actualizarManoUI(jugadorActual.getMano().getCartas());

        // 5. Habilitar/Deshabilitar botones básicos según estado
        btnAdjuntarEnergia.setEnabled(jugadorActual.getZonaEnergia() > 0 && jugadorActual.getPokemonActivo() != null);
        btnAtacar.setEnabled(jugadorActual.getPokemonActivo() != null && !jugadorActual.getPokemonActivo().getAtaques().isEmpty());
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

        // Color dinámico según porcentaje de vida
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

            JButton btnCarta = new JButton("<html><center><b>" + carta.getNombre() + "</b><br><small>" + subtitulo + "</small></center></html>");
            btnCarta.setPreferredSize(new Dimension(135, 52));
            btnCarta.setFocusPainted(false);

            btnCarta.addActionListener(e -> {
                if (cartaClickListener != null) {
                    cartaClickListener.onCartaSeleccionada(indice, carta);
                }
            });

            panelMano.add(btnCarta);
        }
    }

    private JLabel crearSlotVacio() {
        JLabel slot = new JLabel("Vacío", SwingConstants.CENTER);
        slot.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        slot.setForeground(Color.GRAY);
        return slot;
    }

    public void registrarEvento(String mensaje) {
        txtLogEventos.append(mensaje + "\n");
        txtLogEventos.setCaretPosition(txtLogEventos.getDocument().getLength());
    }

    // MÉTODOS PARA CONECTAR EVENTOS DESDE EL CONTROLADOR / FLUJO

    public void setCartaClickListener(CartaClickListener listener) {
        this.cartaClickListener = listener;
    }

    public void addAdjuntarEnergiaListener(ActionListener listener) {
        btnAdjuntarEnergia.addActionListener(listener);
    }

    public void addEvolucionarListener(ActionListener listener) {
        btnEvolucionar.addActionListener(listener);
    }

    public void addAtacarListener(ActionListener listener) {
        btnAtacar.addActionListener(listener);
    }

    public void addRetirarListener(ActionListener listener) {
        btnRetirar.addActionListener(listener);
    }

    public void addPasarTurnoListener(ActionListener listener) {
        btnPasarTurno.addActionListener(listener);
    }
}