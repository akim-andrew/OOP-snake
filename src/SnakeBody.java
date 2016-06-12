/**
 * Created by Boss on 06.05.16.
 */

import java.util.*;

public class SnakeBody {
    public static final int NUMBER_OF_CELLS = 4;
    public static final int CELL_SIZE = 10;
    private ArrayList<SnakeCell> snakeCells = new ArrayList<SnakeCell>();
    private boolean allowToChangeDirection = true;
    private int snakeDirection = 39;
    public int headCoordX;
    public int headCoordY;
    protected String nickname;



    public SnakeBody(String nickname) {
        for (int i = 0; i < NUMBER_OF_CELLS; i++) {
            snakeCells.add(new SnakeCell(CELL_SIZE, 10 + i * CELL_SIZE, CELL_SIZE));
            headCoordX = NUMBER_OF_CELLS * CELL_SIZE;
            headCoordY = CELL_SIZE;
            this.nickname = nickname;
        }

    }

    public ArrayList<SnakeCell> getCellList() {
        return snakeCells;
    }

    //if game restarts, set default values
    public void reloadSnake() {
        snakeCells.clear();
        snakeDirection = 39;
        for (int i = 0; i < NUMBER_OF_CELLS; i++) {
            snakeCells.add(new SnakeCell(CELL_SIZE, 10 + i * CELL_SIZE, CELL_SIZE));
            headCoordX = NUMBER_OF_CELLS * CELL_SIZE;
            headCoordY = CELL_SIZE;
        }
    }

    //(39 right) (37 left) (38 up) (40 down)
    public void setSnakeDirection(int direction) {
        if ((37 <= direction && direction <= 40) && (Math.abs(snakeDirection - direction) != 2) && allowToChangeDirection) {
            snakeDirection = direction;
            allowToChangeDirection = false;
        }
    }

    public void move() {
        SnakeCell cell;
        cell = snakeCells.get(0);
        if (cell.getSize() == CELL_SIZE) {
            snakeCells.remove(0);
        } else {
            cell.setSize(CELL_SIZE);
            cell.setX(cell.getX() + 1);
            cell.setY(cell.getY() + 1);
        }
        switch (snakeDirection) {
            case 40:
                headCoordY += CELL_SIZE;
                if (headCoordY > GameWindow.WINDOW_HEIGHT - CELL_SIZE) {
                    headCoordY = 0;
                }
                snakeCells.add(new SnakeCell(CELL_SIZE, headCoordX, headCoordY));
                break;
            case 38:
                headCoordY -= CELL_SIZE;
                if (headCoordY < 0) {
                    headCoordY = GameWindow.WINDOW_HEIGHT - CELL_SIZE;
                }
                snakeCells.add(new SnakeCell(CELL_SIZE, headCoordX, headCoordY));
                break;
            case 37:
                headCoordX -= CELL_SIZE;
                if (headCoordX < 0) {
                    headCoordX = GameWindow.WINDOW_WIDTH - CELL_SIZE;
                }
                snakeCells.add(new SnakeCell(CELL_SIZE, headCoordX, headCoordY));
                break;
            case 39:
                headCoordX += CELL_SIZE;
                if (headCoordX > GameWindow.WINDOW_WIDTH - CELL_SIZE) {
                    headCoordX = 0;
                }
                snakeCells.add(new SnakeCell(CELL_SIZE, headCoordX, headCoordY));
                break;
            default:
                System.out.println("key code error");
                break;
        }
        allowToChangeDirection = true;
    }
    public String getNickname(){
        return nickname;
    }

}