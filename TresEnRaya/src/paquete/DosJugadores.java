package paquete;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class DosJugadores extends JFrame {

    private JButton[][] buttons;
    private char[][] board;
    private char currentPlayer;
    private boolean gameOver;
    private int scorePlayerX;
    private int scorePlayerO;
    private JLabel labelScoreX;
    private JLabel labelScoreO;

    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem saveMenuItem;
    private JMenuItem loadMenuItem;
    private JMenuItem deleteMenuItem;

    public DosJugadores() {
        setTitle("Tres en raya");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        buttons = new JButton[3][3];
        board = new char[3][3];
        currentPlayer = 'X';
        gameOver = false;
        scorePlayerX = 0;
        scorePlayerO = 0;

        initializeButtons();
        addComponents();
        addActionListeners();
        initializeMenu();
    }

    private void initializeButtons() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 40));
                board[i][j] = ' ';
            }
        }
    }

    private void addComponents() {
        setLayout(new BorderLayout());
        JPanel scorePanel = new JPanel(new GridLayout(2, 2));
        labelScoreX = new JLabel("Jugador X: " + scorePlayerX, SwingConstants.CENTER);
        labelScoreO = new JLabel("Jugador O: " + scorePlayerO, SwingConstants.CENTER);
        scorePanel.add(labelScoreX);
        scorePanel.add(labelScoreO);
        add(scorePanel, BorderLayout.NORTH);

        JPanel gamePanel = new JPanel(new GridLayout(3, 3));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                gamePanel.add(buttons[i][j]);
            }
        }
        add(gamePanel, BorderLayout.CENTER);
    }

    private void addActionListeners() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                final int row = i;
                final int col = j;
                buttons[i][j].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (!gameOver && board[row][col] == ' ') {
                            board[row][col] = currentPlayer;
                            buttons[row][col].setText(String.valueOf(currentPlayer));

                            if (checkWin(currentPlayer)) {
                                updateScore();
                                endGame();
                            } else if (isBoardFull()) {
                                endGame();
                            } else {
                                switchPlayer();
                            }
                        }
                    }
                });
            }
        }
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
    }

    private boolean checkWin(char player) {
        return (checkRows(player) || checkColumns(player) || checkDiagonals(player));
    }

    private boolean checkRows(char player) {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) {
                return true;
            }
        }
        return false;
    }

    private boolean checkColumns(char player) {
        for (int i = 0; i < 3; i++) {
            if (board[0][i] == player && board[1][i] == player && board[2][i] == player) {
                return true;
            }
        }
        return false;
    }

    private boolean checkDiagonals(char player) {
        return (board[0][0] == player && board[1][1] == player && board[2][2] == player) ||
                (board[0][2] == player && board[1][1] == player && board[2][0] == player);
    }

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

    private void updateScore() {
        if (currentPlayer == 'X') {
            scorePlayerX++;
        } else {
            scorePlayerO++;
        }
        labelScoreX.setText("Jugador X: " + scorePlayerX);
        labelScoreO.setText("Jugador O: " + scorePlayerO);
    }

    private void endGame() {
        if (isBoardFull()) {
            JOptionPane.showMessageDialog(null, "¡Es un empate!");
        } else {
            JOptionPane.showMessageDialog(null, "¡Jugador " + currentPlayer + " gana!");
        }
        gameOver = true;

        int option = JOptionPane.showConfirmDialog(null, "¿Quieres jugar otra vez?", "Reiniciar Juego", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            resetGame();
        } else {
            System.exit(0);
        }
    }

    private void resetGame() {
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

    private void initializeMenu() {
        menuBar = new JMenuBar();
        fileMenu = new JMenu("Opciones de guardado");

        saveMenuItem = new JMenuItem("Guardar Partida");
        loadMenuItem = new JMenuItem("Cargar Partida Guardada");
        deleteMenuItem = new JMenuItem("Eliminar Partida Guardada");

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

        fileMenu.add(saveMenuItem);
        fileMenu.add(loadMenuItem);
        fileMenu.add(deleteMenuItem);

        menuBar.add(fileMenu);

        setJMenuBar(menuBar);
    }

    private void saveGame() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("savedGameTwoPlayers.dat"))) {
            oos.writeObject(board);
            oos.writeObject(currentPlayer);
            oos.writeObject(gameOver);
            oos.writeObject(scorePlayerX);
            oos.writeObject(scorePlayerO);
            JOptionPane.showMessageDialog(this, "Partida guardada correctamente");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al guardar la partida");
        }
    }

    private void loadGame() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("savedGameTwoPlayers.dat"))) {
            board = (char[][]) ois.readObject();
            currentPlayer = (char) ois.readObject();
            gameOver = (boolean) ois.readObject();
            scorePlayerX = (int) ois.readObject();
            scorePlayerO = (int) ois.readObject();
            updateScore();
            updateButtons();
            JOptionPane.showMessageDialog(this, "Partida cargada correctamente");
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "No hay partida guardada para cargar");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar la partida");
        }
    }

    private void deleteGame() {
        File file = new File("savedGameTwoPlayers.dat");
        if (file.exists()) {
            file.delete();
            JOptionPane.showMessageDialog(this, "Partida eliminada correctamente");
        } else {
            JOptionPane.showMessageDialog(this, "No hay partida guardada para eliminar");
        }
    }

    private void updateButtons() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText(String.valueOf(board[i][j]));
            }
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new DosJugadores().setVisible(true);
            }
        });
    }
}
