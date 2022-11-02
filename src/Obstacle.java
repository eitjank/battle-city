public class Obstacle extends GameObject {

    private ObstacleType type;

    public Obstacle(int x, int y, int width, int height, ObstacleType type) {
        super(x,y,width,height);
        this.type = type;
    }

    public ObstacleType getType() {
        return type;
    }
}
