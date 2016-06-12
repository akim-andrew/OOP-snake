/**
 * Created by Boss on 06.05.16.
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.awt.event.KeyEvent;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;



public class GameWindow {
    public static final int WINDOW_WIDTH = 500;
    public static final int WINDOW_HEIGHT = 350;
    private JFrame mainFrame = new JFrame();
    private JLabel endGameLabel = new JLabel();
    private String nickname;
    private SnakeBody snake = new SnakeBody(nickname);
    private GameField snakeField;
    private ScorePanel scorePanel = new ScorePanel();
    private Food food = new Food();
    private int timeBetweenMoves;
    private boolean allowToRestartTheGame = false;
    private boolean allowToMoveSnake = true;
    final int SNAKE_RUNNING_SPEED_FASTEST = 80;
    final int SNAKE_RUNNING_SPEED_FASTER = 100;
    final int SNAKE_RUNNING_SPEED_FAST = 120;
    protected static SnakeBody player1;

    private enum GAME_TYPE {NO_MAZE, BORDER};
    private GAME_TYPE selectedGameType = GAME_TYPE.NO_MAZE;

    public int getTimeBetweenMoves() {
        return timeBetweenMoves;
    }


        public static void main(String[] args) {
            if (args.length != 1) {

                System.exit(-1);
            }
            GameWindow game = new GameWindow();

            game.setUpGame(args[0]);
        }


    public void setUpGame(String name) {

         timeBetweenMoves = 120;					//snake speed
        int scoreToChangeSpeedLevel = 50;

        setUpGui();
        food.generateFood(snake.getCellList());		//place food on the game field
        while(true) {
            if (!allowToMoveSnake) {                //if snake dead, wait for new game
                continue;
            }
            snakeField.repaint();
            try {
                Thread.sleep(timeBetweenMoves);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            snake.move();
            checkIfSnakeGotFood();
            if ((scorePanel.getScore()) == scoreToChangeSpeedLevel) {
                timeBetweenMoves -= 10;
                scoreToChangeSpeedLevel += 50;
            }


            if (snakeCollapsed()) {
                endGameLabel.setText("You have lost. Want to start over? Y/N");
                allowToRestartTheGame = true;
                allowToMoveSnake = false;
            }
            switch (selectedGameType) {
                case BORDER:
                    if (checkIfSnakeTouchedBorder()) {
                        endGameLabel.setText("You have lost. Want to start over? Y/N");
                        allowToRestartTheGame = true;
                        allowToMoveSnake = false;
                    }


            }
        }
    }

    private void saveScores() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection c = (Connection) DriverManager.getConnection("jdbc:mysql://127.0.0.1/snakedb?user=djek&password=case379djek");
            Statement s = (Statement) c.createStatement();
            String query = "insert into pacman_table(nickname, score) values (?, ?)";
            PreparedStatement preparedStmt = c.prepareStatement(query);
            preparedStmt.setString(1, snake.getNickname());
            preparedStmt.execute();
            c.close();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }



    public void checkIfSnakeGotFood() {
        ArrayList<SnakeCell> cellList = snake.getCellList();
        SnakeCell cell = cellList.get(cellList.size() - 1);
        if (food.equals(cell)) {
            food.generateFood(snake.getCellList());
            cell.setSize(cell.getSize() + 2);
            cell.setX(cell.getX() - 1);
            cell.setY(cell.getY() - 1);
            scorePanel.incScore(10);
        }

    }

    public boolean checkIfSnakeTouchedBorder( ) {
        if (snake.headCoordX >= GameWindow.WINDOW_WIDTH - SnakeBody.CELL_SIZE+1) {
            snake.headCoordX = 0;
            return true;
        } else if (snake.headCoordX <= 0) {
            snake.headCoordX = GameWindow.WINDOW_WIDTH - SnakeBody.CELL_SIZE;
            return true;
        } else if (snake.headCoordY >= GameWindow.WINDOW_HEIGHT ) {
            snake.headCoordY = 0;
            return true;
        } else if (snake.headCoordY <= 0) {
            snake.headCoordY = GameWindow.WINDOW_HEIGHT - SnakeBody.CELL_SIZE;
            return true;
        }
        return false;
    }



    public boolean snakeCollapsed() {
        ArrayList<SnakeCell> cellList = snake.getCellList();
        SnakeCell cell = cellList.get(cellList.size() - 1);
        Iterator itr = cellList.iterator();
        while(itr.hasNext()){
            if(itr.next().equals(cell) && itr.hasNext()) {
                return true;
            }
        }
        return false;
    }

    public void setUpGui() {
        snakeField = new GameField(snake.getCellList(), food,  snake);
        snakeField.addKeyListener(new SnakeKeyListener());
        snakeField.add(endGameLabel);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setJMenuBar(mainFrame);
        mainFrame.setResizable(false);
        mainFrame.getContentPane().add(BorderLayout.CENTER, snakeField);
        mainFrame.getContentPane().add(BorderLayout.SOUTH, scorePanel);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    public void restartGame() {
        snake.reloadSnake();
        food.generateFood(snake.getCellList());
        scorePanel.reloadPanel();
        scorePanel.repaint();
        endGameLabel.setText("");
        allowToMoveSnake = true;
    }

    public class SnakeKeyListener implements KeyListener {
        public void keyPressed(KeyEvent e) {
            snake.setSnakeDirection(e.getKeyCode());
            if (e.getKeyCode() == 89 && allowToRestartTheGame) {
                allowToRestartTheGame = false;
                restartGame();

            }
            if (e.getKeyCode() == 78 && allowToRestartTheGame) {
                allowToRestartTheGame = true;
                System.exit(0);}
            System.out.println(e.getKeyCode());


        }
        public void keyReleased(KeyEvent e) { }
        public void keyTyped(KeyEvent e) { }
    }

    public void setJMenuBar(JFrame frame) {

        JMenuBar mymbar = new JMenuBar();

        JMenu game = new JMenu("Game");
        JMenuItem newgame = new JMenuItem("New Game");
        JMenuItem exit = new JMenuItem("Exit");
        newgame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                restartGame();
            }
        });
        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        game.add(newgame);
        game.addSeparator();
        game.add(exit);
        mymbar.add(game);

        JMenu type = new JMenu("Type");
        JMenuItem noMaze = new JMenuItem("No Maze");
        noMaze.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectedGameType = GAME_TYPE.NO_MAZE;
                restartGame();
            }
        });
        JMenuItem border = new JMenuItem("Border Maze");
        border.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectedGameType = GAME_TYPE.BORDER;
                restartGame();
            }
        });
        type.add(noMaze);
        type.add(border);
        mymbar.add(type);

        JMenu level = new JMenu("Level");
        JMenuItem level1 = new JMenuItem("Level 1");
        level1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                timeBetweenMoves = SNAKE_RUNNING_SPEED_FAST;
                restartGame();
            }
        });
        JMenuItem level2 = new JMenuItem("Level 2");
        level2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                timeBetweenMoves = SNAKE_RUNNING_SPEED_FASTER;
                restartGame();
            }
        });
        JMenuItem level3 = new JMenuItem("Level 3");
        level3.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                timeBetweenMoves = SNAKE_RUNNING_SPEED_FASTEST;
                restartGame();
            }
        });
        level.add(level1);
        level.add(level2);
        level.add(level3);
        mymbar.add(level);

        JMenu help = new JMenu("Help");
        JMenuItem creator = new JMenuItem("Creator");
        JMenuItem instruction = new JMenuItem("Instraction");
        creator.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Author: Djek \nVersion: 1.0.0 \n ");
            }
        });

        instruction.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "  It is very easy! \nYou need eat apple and don`t touch yourself \n ");
            }
        });

        help.add(creator);
        help.add(instruction);
        mymbar.add(help);

        frame.setJMenuBar(mymbar);
    }


    public String getNickname() {
        return nickname;

    }
}
