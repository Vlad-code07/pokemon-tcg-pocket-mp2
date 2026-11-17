package pokemontcg.gui;

import javax.swing.*;
import java.awt.*;

public class DialogoCambioTurno {

    public static void mostrar(JFrame ventana, String nombreJugador) {

        // Crear el dialogo de cambio de turno
        JDialog dialogo = new JDialog(
                ventana,
                "Cambio de turno",
                true
        );

        dialogo.setSize(400, 220);
        dialogo.setLocationRelativeTo(ventana);
        dialogo.setResizable(false);

        // Panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(15, 15));
        panel.setBorder(
                BorderFactory.createEmptyBorder(25, 20, 25, 20)
        );

        // Mensaje para el siguiente jugador
        JLabel mensaje = new JLabel(
                "<html><center>Turno de " + nombreJugador
                + "<br><br>Presiona continuar cuando estés listo."
                + "</center></html>",
                SwingConstants.CENTER
        );

        mensaje.setFont(new Font("Arial", Font.BOLD, 17));

        panel.add(mensaje, BorderLayout.CENTER);

        // Boton para continuar
        JButton botonContinuar = new JButton("CONTINUAR");

        botonContinuar.setFont(
                new Font("Arial", Font.BOLD, 14)
        );

        panel.add(botonContinuar, BorderLayout.SOUTH);

        // Cerrar el dialogo al continuar
        botonContinuar.addActionListener(e -> {
            dialogo.dispose();
        });

        // Evitar cerrar el dialogo con la X
        dialogo.setDefaultCloseOperation(
                JDialog.DO_NOTHING_ON_CLOSE
        );

        dialogo.add(panel);

        // Mostrar el dialogo
        dialogo.setVisible(true);
    }
}