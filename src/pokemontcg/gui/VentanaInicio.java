package pokemontcg.gui;

import pokemontcg.combate.Mazo;
import pokemontcg.datos.CatalogoMazos;
import javax.swing.*;
import java.awt.*;
import pokemontcg.combate.Jugador;


public class VentanaInicio extends JFrame {

    // Campos para escribir los nombres de los jugadores
    private JTextField campoNombreJugador1;
    private JTextField campoNombreJugador2;

    // Listas para seleccionar los mazos
    private JComboBox<String> selectorMazoJugador1;
    private JComboBox<String> selectorMazoJugador2;

    // Mazos reales seleccionados por los jugadores
    private Mazo mazoJugador1;
    private Mazo mazoJugador2;

    // Botón para iniciar la partida
    private JButton botonIniciar;

    public VentanaInicio() {

        // Configuración de la ventana
        setTitle("Pokemon TCG Pocket");
        setSize(550, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel principal
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout(10, 10));
        panelPrincipal.setBorder(
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        );

        // Título
        JLabel titulo = new JLabel(
            "POKEMON TCG POCKET",
            SwingConstants.CENTER
        );

        titulo.setFont(new Font("Arial", Font.BOLD, 24));

        panelPrincipal.add(titulo, BorderLayout.NORTH);

        // Panel de datos de los jugadores
        JPanel panelJugadores = new JPanel();
        panelJugadores.setLayout(new GridLayout(4, 2, 10, 15));

        // Jugador 1
        JLabel etiquetaJugador1 = new JLabel("Nombre del jugador 1:");
        campoNombreJugador1 = new JTextField();

        JLabel etiquetaMazo1 = new JLabel("Mazo del jugador 1:");

        String[] mazosDisponibles = {
            "Selecciona un mazo",
            "Mazo Electrico",
            "Mazo Fuego",
            "Mazo Agua",
            "Mazo Mixto"
        };

        selectorMazoJugador1 = new JComboBox<>(mazosDisponibles);

        // Jugador 2
        JLabel etiquetaJugador2 = new JLabel("Nombre del jugador 2:");
        campoNombreJugador2 = new JTextField();

        JLabel etiquetaMazo2 = new JLabel("Mazo del jugador 2:");
        selectorMazoJugador2 = new JComboBox<>(mazosDisponibles);

        // Agregar componentes al panel
        panelJugadores.add(etiquetaJugador1);
        panelJugadores.add(campoNombreJugador1);

        panelJugadores.add(etiquetaMazo1);
        panelJugadores.add(selectorMazoJugador1);

        panelJugadores.add(etiquetaJugador2);
        panelJugadores.add(campoNombreJugador2);

        panelJugadores.add(etiquetaMazo2);
        panelJugadores.add(selectorMazoJugador2);

        panelPrincipal.add(panelJugadores, BorderLayout.CENTER);

        // Botón para iniciar
        botonIniciar = new JButton("INICIAR PARTIDA");

        botonIniciar.setFont(new Font("Arial", Font.BOLD, 16));

        panelPrincipal.add(botonIniciar, BorderLayout.SOUTH);

        // Acción del botón
        botonIniciar.addActionListener(e -> validarDatos());

        // Mostrar la ventana
        add(panelPrincipal);
        setVisible(true);
    }

    // Método para validar los datos ingresados
    private void validarDatos() {

        String nombre1 = campoNombreJugador1.getText().trim();
        String nombre2 = campoNombreJugador2.getText().trim();

        String mazo1 = (String) selectorMazoJugador1.getSelectedItem();
        String mazo2 = (String) selectorMazoJugador2.getSelectedItem();

        // Validar nombres vacíos
        if (nombre1.isEmpty() || nombre2.isEmpty()) {

            JOptionPane.showMessageDialog(
                this,
                "Debes escribir el nombre de los dos jugadores.",
                "Datos incompletos",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // Validar nombres iguales
        if (nombre1.equalsIgnoreCase(nombre2)) {

            JOptionPane.showMessageDialog(
                this,
                "Los jugadores deben tener nombres diferentes.",
                "Nombres no válidos",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // Validar selección de mazos
        if (mazo1.equals("Selecciona un mazo")
                || mazo2.equals("Selecciona un mazo")) {

            JOptionPane.showMessageDialog(
                this,
                "Cada jugador debe seleccionar un mazo.",
                "Mazos no seleccionados",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }
        // Crear los mazos reales seleccionados
        mazoJugador1 = CatalogoMazos.obtenerMazo(mazo1);
        mazoJugador2 = CatalogoMazos.obtenerMazo(mazo2);

        // Crear los jugadores
        Jugador jugador1 = new Jugador(nombre1, mazoJugador1);
        Jugador jugador2 = new Jugador(nombre2, mazoJugador2);

        // Abrir el tablero
        TableroJuego tablero = new TableroJuego(jugador1, jugador2);

        tablero.setVisible(true);

        // Cerrar la ventana de inicio
        dispose();
    }

    // Métodos para obtener los datos de los jugadores
    public String getNombreJugador1() {
        return campoNombreJugador1.getText().trim();
    }

    public String getNombreJugador2() {
        return campoNombreJugador2.getText().trim();
    }

    // Obtener el nombre del mazo seleccionado por cada jugador
    public String getNombreMazoJugador1() {
        return (String) selectorMazoJugador1.getSelectedItem();
    }

    public String getNombreMazoJugador2() {
        return (String) selectorMazoJugador2.getSelectedItem();
    }

    // Método principal para probar la ventana
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new VentanaInicio();
        });
    }

    // Obtener el mazo del jugador 1
public Mazo getMazoJugador1() {
    return mazoJugador1;
}

// Obtener el mazo del jugador 2
public Mazo getMazoJugador2() {
    return mazoJugador2;
}
}