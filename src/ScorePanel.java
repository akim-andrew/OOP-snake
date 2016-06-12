/**
 * Created by Boss on 06.05.16.
 */
import javax.swing.*;
import java.awt.*;


//count and displays your score
public class ScorePanel extends JPanel {
    public static final int SCORE_HEIGHT = 25;
    private static final int SCORE_WIDTH = 500;
    private int score = 0;
    private int level = 1;
    private JLabel scoreLabel = new JLabel("Your score==>");
    private JLabel actualScore = new JLabel("0");

    public ScorePanel() {
        this.add(scoreLabel).setForeground(Color.WHITE);
        scoreLabel.setFont(new Font("Times New Romane", Font.PLAIN, 14));
        this.add(actualScore).setForeground(Color.WHITE);
        this.setBackground(Color.BLACK);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setPreferredSize(new Dimension(SCORE_WIDTH, SCORE_HEIGHT));

    }

    public void incScore(int inc) {
        score += inc;
        actualScore.setText(Integer.toString(score));
    }



    public int getScore() {
        return score;
    }

    public void reloadPanel() {
        score = 0;
        level = 1;
        actualScore.setText(Integer.toString(score));

    }
}
