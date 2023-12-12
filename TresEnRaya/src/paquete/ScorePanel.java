package paquete;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Font;

public class ScorePanel extends JPanel {

    private int playerWins;
    private int computerWins;
    private JLabel scoreLabel;

    public ScorePanel() {
        playerWins = 0;
        computerWins = 0;

        scoreLabel = new JLabel("Jugador: 0 - IA: 0");
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        add(scoreLabel);
    }

    public void updateScore(boolean playerWins) {
        if (playerWins) {
            this.playerWins++;
        } else {
            this.computerWins++;
        }
        scoreLabel.setText("Jugador: " + this.playerWins + " - IA: " + this.computerWins);
    }
}
