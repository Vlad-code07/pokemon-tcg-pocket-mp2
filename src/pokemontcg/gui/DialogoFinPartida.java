package pokemontcg.gui;

import javax.swing.*;
import java.awt.*;

public class DialogoFinPartida {

    public static boolean mostrar(
            JFrame ventana,
            String nombreGanador,
            String condicionVictoria) {

        // Variable para saber si se quiere jugar otra vez
        final boolean[] jugarOtraVez = {false};

        // Crear la ventana de final de partida
        JDialog dialogo = new JDialog(
                ventana,
                "Fin de la partida",
                true
        );

        dialogo.setSize(450, 280);
        dialogo.setLocationRelativeTo(ventana);
        dialogo.setResizable(false);

        // Panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(15, 15));

        panel.setBorder(
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        );

        // Mensaje del ganador
        JLabel mensaje = new JLabel(
                "<html><center>"
                + "<h1>¡VICTORIA!</h1>"
                + "<h2>" + nombreGanador + "</h2>"
                + "<p>" + condicionVictoria + "</p>"
                + "</center></html>",
                SwingConstants.CENTER
        );

        mensaje.setFont(new Font("Arial", Font.BOLD, 16));

        panel.add(mensaje, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel();

        JButton botonReiniciar = new JButton("JUGAR OTRA VEZ");
        JButton botonSalir = new JButton("SALIR");

        botonReiniciar.setFont(
                new Font("Arial", Font.BOLD, 13)
        );

        botonSalir.setFont(
                new Font("Arial", Font.BOLD, 13)
        );

        panelBotones.add(botonReiniciar);
        panelBotones.add(botonSalir);

        panel.add(panelBotones, BorderLayout.SOUTH);

        // Acción para jugar otra vez
        botonReiniciar.addActionListener(e -> {

            jugarOtraVez[0] = true;

            dialogo.dispose();
        });

        // Acción para salir
        botonSalir.addActionListener(e -> {

            jugarOtraVez[0] = false;

            dialogo.dispose();
        });

        // Si se cierra con la X, se considera que desea salir
        dialogo.setDefaultCloseOperation(
                JDialog.DISPOSE_ON_CLOSE
        );

        dialogo.add(panel);

        // Mostrar la ventana
        dialogo.setVisible(true);

        return jugarOtraVez[0];
    }
}