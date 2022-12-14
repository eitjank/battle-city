package battleCity.entities;

import java.awt.*;

public class Missile extends GameObject {
    private int dx;
    private int dy;
    public static final int SIZE = 6;

    public Missile(int x, int y, int dx, int dy) {
        super(x, y, SIZE, SIZE);
        this.dx = dx;
        this.dy = dy;
    }

    public void move() {
        x += dx;
        y += dy;
    }

    public void draw(Graphics g){
        g.setColor(Color.white);
        g.fillRect(x, y, width, height);
    }

}
