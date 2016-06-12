/**
 * Created by Boss on 06.05.16.
 */
public class SnakeCell {
    private int cellSize;
    private int x;
    private int y;

    public SnakeCell(int size, int x, int y) {
        cellSize = size;
        this.x = x;
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setSize(int size) {
        cellSize = size;
    }

    public int getSize() {
        return cellSize;
    }

}