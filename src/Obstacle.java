import java.awt.*;

public class Obstacle {

    int x;
    int y;
    int width = 26;
    int height = 26;
    ObstacleType type;

    public Obstacle(int x, int y, ObstacleType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public Obstacle(int x, int y, int width, int height, ObstacleType type) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.type = type;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

}
