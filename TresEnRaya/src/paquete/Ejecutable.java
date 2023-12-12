package paquete;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Ejecutable extends JPanel {

    private JButton vsComputerButton;
    private JButton vsPlayerButton;
    private JButton helpButton;

    public Ejecutable(ActionListener actionListener) {
        setLayout(new GridLayout(3, 1));

        vsComputerButton = new JButton("Jugar contra la IA");
        vsPlayerButton = new JButton("Jugar 2 jugadores");
        helpButton = new JButton("Help");
    

        vsComputerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (actionListener != null) {
                    ActionEvent newEvent = new ActionEvent(e.getSource(), e.getID(), "vsComputer");
                    actionListener.actionPerformed(newEvent);
                }
                // Iniciar la instancia de TresEnRaya para el modo vsComputer
                new ContralaIA(true).setVisible(true);
            }
        });

        vsPlayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (actionListener != null) {
                    ActionEvent newEvent = new ActionEvent(e.getSource(), e.getID(), "vsPlayer");
                    actionListener.actionPerformed(newEvent);
                }
                // Iniciar la instancia de la clase 2Jugadores
                new DosJugadores().setVisible(true);
            }
        });

        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (actionListener != null) {
                    ActionEvent newEvent = new ActionEvent(e.getSource(), e.getID(), "help");
                    actionListener.actionPerformed(newEvent);
                }
                // Mostrar ayuda 
                JOptionPane.showMessageDialog(null, 
                		"------------------------------------------------------------------------------------------------------\r\n"
                		+ "¿Cómo jugar?\r\n"
                		+ "Haz clic en un cuadro vacío para colocar tu símbolo ('X' u 'O'). Alterna turnos con tu oponente.\r\n "
                		+ "------------------------------------------------------------------------------------------------------\r\n"
                		+ "¿Cómo ganar?\r\n"
                		+ "Forma una línea de tres símbolos seguidos en horizontal, vertical o diagonal para ganar.\r\n"
                		+ "------------------------------------------------------------------------------------------------------\r\n"
                		+ "¿Cómo reiniciar el juego?\r\n"
                		+ "Si deseas empezar de nuevo, reinicia la aplicación o vuelve a cargar la ventana del juego.\r\n"
                		+ "------------------------------------------------------------------------------------------------------\r\n"
                		+ "¿Qué sucede en caso de empate?\r\n"
                		+ "Si todos los cuadros están llenos y nadie ha ganado, el juego termina en empate.\r\n"
                		+ "------------------------------------------------------------------------------------------------------\r\n"
                		+ "¿Puedo deshacer un movimiento?\r\n"
                		+ "No, una vez que colocas tu símbolo, no puedes deshacerlo. ¡Piensa estratégicamente!\r\n"
                		+ "------------------------------------------------------------------------------------------------------\r\n"
                		+ "¿Cómo salir del juego?\r\n"
                		+ "Cierra la ventana del juego para salir del juego.\r\n"
                		+ "------------------------------------------------------------------------------------------------------\r\n"
                		+ "¿Puedo jugar contra la computadora?\r\n"
                		+ "Si, ademas de la versión para dos jugadores locales. Tambien existe la version contra la maquina..\r\n"
                		+ "------------------------------------------------------------------------------------------------------\r\n"
                		+ "¿Hay atajos de teclado?\r\n"
                		+ "No, la interacción se realiza mediante clics del mouse en los botones del juego.\r\n"
                		+ "------------------------------------------------------------------------------------------------------\r\n"
                		+ "¿Cómo obtener ayuda adicional?\r\n"
                		+ "Si tienes problemas o preguntas adicionales, consulta la documentación del juego en PDF\r\n"
                		+ "------------------------------------------------------------------------------------------------------");
            }
        });	

        add(vsComputerButton);
        add(vsPlayerButton);
        add(helpButton);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Selecciona un modo");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(300, 200);

                Ejecutable modeSelectionPanel = new Ejecutable(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Este ActionListener no realizará ninguna acción directa aquí
                    }
                });

                frame.add(modeSelectionPanel);
                frame.setVisible(true);
            }
        });
    }
}
