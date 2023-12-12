package paquete;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

// Clase principal que extiende JFrame para la ventana del juego
public class ContralaIA extends JFrame {

    private JButton[][] buttons;  // Matriz de botones que representa el tablero
    private char[][] board;  // Matriz que almacena el estado actual del tablero
    private char currentPlayer;  // Jugador actual ('X' u 'O')
    private boolean gameOver;  // Indica si el juego ha terminado
    
    private ScorePanel scorePanel;
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem saveMenuItem;
    private JMenuItem loadMenuItem;
    private JMenuItem deleteMenuItem;

    // Constructor que inicializa la ventana del juego
    public ContralaIA(boolean b) {
        setTitle("Tres en raya");  // Título de la ventana
        setSize(400, 400);  // Tamaño de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Cierra la aplicación al cerrar la ventana

        buttons = new JButton[3][3];  // Inicializa la matriz de botones
        board = new char[3][3];  // Inicializa la matriz que representa el tablero
        currentPlayer = 'X';  // Inicia con el jugador 'X'
        gameOver = false;  // El juego no ha comenzado
        initializeScorePanel();  // Llama al método para inicializar el ScorePanel
        initializeButtons();     // Inicializa los botones del tablero
     // Inicializa el menú y las opciones
        initializeMenu();
        initializeButtons();  // Inicializa los botones del tablero
        addComponents();  // Añade los componentes a la ventana
        addActionListeners();  // Añade los escuchadores de eventos a los botones
    }
    private void initializeScorePanel() {
        scorePanel = new ScorePanel();
        add(scorePanel, BorderLayout.SOUTH);  // Añade el ScorePanel en la parte inferior de la ventana
    }
    private void initializeMenu() {
        menuBar = new JMenuBar();
        fileMenu = new JMenu("Opciones de guardado");

        saveMenuItem = new JMenuItem("Guardar Partida");
        loadMenuItem = new JMenuItem("Cargar Partida Guardada");
        deleteMenuItem = new JMenuItem("Eliminar Partida Guardada");

        // Asigna acciones a las opciones del menú
        saveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveGame();
            }
        });

        loadMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadGame();
            }
        });

        deleteMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteGame();
            }
        });

        // Agrega las opciones al menú
        fileMenu.add(saveMenuItem);
        fileMenu.add(loadMenuItem);
        fileMenu.add(deleteMenuItem);

        // Agrega el menú a la barra de menú
        menuBar.add(fileMenu);

        // Asigna la barra de menú a la ventana
        setJMenuBar(menuBar);
    }

    // Método para guardar la partida en un archivo
    private void saveGame() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("savedGame.dat"))) {
            oos.writeObject(board);
            oos.writeObject(currentPlayer);
            oos.writeObject(gameOver);
            JOptionPane.showMessageDialog(this, "Partida guardada correctamente");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al guardar la partida");
        }
    }

    // Método para cargar la partida desde un archivo
    private void loadGame() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("savedGame.dat"))) {
            board = (char[][]) ois.readObject();
            currentPlayer = (char) ois.readObject();
            gameOver = (boolean) ois.readObject();
            updateButtons();  // Actualiza los botones con la información cargada
            JOptionPane.showMessageDialog(this, "Partida cargada correctamente");
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "No hay partida guardada para cargar");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar la partida");
        }
    }

    // Método para eliminar la partida guardada
    private void deleteGame() {
        File file = new File("savedGame.dat");
        if (file.exists()) {
            file.delete();
            JOptionPane.showMessageDialog(this, "Partida eliminada correctamente");
        } else {
            JOptionPane.showMessageDialog(this, "No hay partida guardada para eliminar");
        }
    }

    // Método para actualizar los botones con la información cargada
    private void updateButtons() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText(String.valueOf(board[i][j]));
            }
        }
    }
    // Inicializa los botones del tablero y la matriz del tablero
    private void initializeButtons() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton();  // Crea un nuevo botón
                buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 40));  // Establece el tamaño de la fuente
                board[i][j] = ' ';  // Inicializa el tablero con espacios en blanco
            }
        }
    }

    // Añade los botones al diseño de la ventana
    private void addComponents() {
        setLayout(new BorderLayout());  // Usa un BorderLayout en lugar de GridLayout

        // Agrega el ScorePanel en la parte superior de la ventana
        scorePanel = new ScorePanel();
        add(scorePanel, BorderLayout.NORTH);

        // Agrega el GridLayout para las celdas del juego en el centro
        JPanel gamePanel = new JPanel(new GridLayout(3, 3));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                gamePanel.add(buttons[i][j]);  // Añade cada botón al panel del juego
            }
        }
        add(gamePanel, BorderLayout.CENTER);
    }


    // Añade escuchadores de eventos a los botones del tablero
    private void addActionListeners() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                final int row = i;
                final int col = j;
                buttons[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (!gameOver && board[row][col] == ' ') {
                            // Realiza la jugada del jugador actual
                            board[row][col] = currentPlayer;
                            buttons[row][col].setText(String.valueOf(currentPlayer));

                            // Verifica si el jugador actual ha ganado o si hay empate
                            if (checkWin(currentPlayer)) {
                                endGame();
                            } else if (isBoardFull()) {
                                handleGameResult();  // Cambiado a handleGameResult() para empates
                            } else {
                                switchPlayer();  // Cambia al siguiente jugador
                                // Si es el turno de la computadora, realiza su jugada
                                if (currentPlayer == 'O') {
                                    playComputerTurn();
                                }
                            }
                        }
                    }
                });
            }
        }
    }

    // Método para manejar el resultado del juego (empate, victoria, etc.)
    private void handleGameResult() {
        String message;
        if (isBoardFull()) {
            message = "¡Es un empate!";
        } else {
            String winner = (currentPlayer == 'X') ? "Jugador" : "IA";
            message = winner + " ha ganado!";
            scorePanel.updateScore(currentPlayer == 'X'); // Actualiza el ScorePanel según el ganador
        }
        gameOver = true;

        // Pide reiniciar el juego
        int option = JOptionPane.showOptionDialog(
                null,
                message + "\n¿Quieres jugar otra vez?",
                "Fin del juego",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new Object[]{"Sí", "No"},
                "Sí");

        if (option == JOptionPane.YES_OPTION) {
            resetGame();  // Reinicia el juego si el jugador elige jugar otra vez
        } else {
            System.exit(0);  // Cierra la aplicación si el jugador elige salir
        }
    }

    // Cambia al siguiente jugador ('X' a 'O' o viceversa)
    private void switchPlayer() {
        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
    }

    // Verifica si el jugador actual ha ganado
    private boolean checkWin(char player) {
        // Verifica filas, columnas y diagonales para determinar la victoria
        return (checkRows(player) || checkColumns(player) || checkDiagonals(player));
    }

    // Verifica las filas para determinar la victoria
    private boolean checkRows(char player) {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) {
                return true;
            }
        }
        return false;
    }

    // Verifica las columnas para determinar la victoria
    private boolean checkColumns(char player) {
        for (int i = 0; i < 3; i++) {
            if (board[0][i] == player && board[1][i] == player && board[2][i] == player) {
                return true;
            }
        }
        return false;
    }

    // Verifica las diagonales para determinar la victoria
    private boolean checkDiagonals(char player) {
        return (board[0][0] == player && board[1][1] == player && board[2][2] == player) ||
                (board[0][2] == player && board[1][1] == player && board[2][0] == player);
    }

    // Verifica si el tablero está lleno (empate)
    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    // Realiza la jugada de la computadora
    private void playComputerTurn() {
        // Verifica si hay una jugada ganadora para la computadora
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    // Simula la jugada
                    board[i][j] = currentPlayer;
                    if (checkWin(currentPlayer)) {
                        // Jugada ganadora encontrada
                        buttons[i][j].setText(String.valueOf(currentPlayer));
                        endGame();
                        return;
                    }
                    // Deshace la jugada
                    board[i][j] = ' ';
                }
            }
        }

        // Verifica si hay una jugada para bloquear al jugador humano
        char opponent = (currentPlayer == 'X') ? 'O' : 'X';
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    // Simula la jugada del oponente
                    board[i][j] = opponent;
                    if (checkWin(opponent)) {
                        // Jugada de bloqueo encontrada
                        board[i][j] = currentPlayer;
                        buttons[i][j].setText(String.valueOf(currentPlayer));
                        switchPlayer();
                        return;
                    }
                    // Deshace la jugada del oponente
                    board[i][j] = ' ';
                }
            }
        }

        // Si no hay jugadas ganadoras ni de bloqueo, realiza una jugada estratégica
        int[] move = findStrategicMove();
        board[move[0]][move[1]] = currentPlayer;
        buttons[move[0]][move[1]].setText(String.valueOf(currentPlayer));

        // Verifica si el jugador actual ha ganado o si hay empate
        if (checkWin(currentPlayer)) {
            endGame();
        } else if (isBoardFull()) {
            JOptionPane.showMessageDialog(null, "¡Sois iguales de malos, EMPATEEE!");
            gameOver = true;
        }

        switchPlayer();
    }
    // Encuentra una jugada estratégica para la computadora (en este caso, una jugada aleatoria)
    private int[] findStrategicMove() {
        int row, col;
        do {
            row = (int) (Math.random() * 3);
            col = (int) (Math.random() * 3);
        } while (board[row][col] != ' ');

        return new int[]{row, col};
    }

 // Método para finalizar el juego mostrando un mensaje de victoria para el jugador actual
    private void endGame() {
        String message;
        if (isBoardFull()) {
            message = "¡Es un empate!";
        } else {
            String winner = (currentPlayer == 'X') ? "Jugador" : "IA";
            message = winner + " ha ganado!";
            scorePanel.updateScore(currentPlayer == 'X'); // Actualiza el ScorePanel según el ganador
        }
        gameOver = true;

        // Pide reiniciar el juego
        int option = JOptionPane.showOptionDialog(
                null,
                message + "\n¿Quieres jugar otra vez?",
                "Fin del juego",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new Object[]{"Sí", "No"},
                "Sí");

        if (option == JOptionPane.YES_OPTION) {
            resetGame();  // Reinicia el juego si el jugador elige jugar otra vez
        } else {
            System.exit(0);  // Cierra la aplicación si el jugador elige salir
        }
    }



 // Método para reiniciar el juego
    private void resetGame() {
        // Reinicia las variables del juego
        currentPlayer = 'X';
        gameOver = false;

        // Limpia la matriz del tablero y los textos de los botones
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
                buttons[i][j].setText("");
            }
        }
    }


    // Método principal para iniciar la aplicación
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Ejecutable(null).setVisible(true);  // Crea una instancia de la clase y muestra la ventana
            }
        });
    }
}
