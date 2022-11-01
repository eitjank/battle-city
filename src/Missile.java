import java.awt.*;

public class Missile {
    int x;
    int y;
    private int dx;
    private int dy;
    static int size = 6;

    public Missile(int x, int y, int dx, int dy) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
    }

    public void move() {
        x += dx;
        y += dy;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }
}
