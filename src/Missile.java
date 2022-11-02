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

}
