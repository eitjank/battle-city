package main;

import main.enums.ObstacleType;

import java.awt.*;

public class Obstacle extends GameObject {

    private final ObstacleType type;
    private final Image image;

    public Obstacle(int x, int y, int width, int height, ObstacleType type, Image image) {
        super(x,y,width,height);
        this.type = type;
        this.image = image;
    }

    public ObstacleType getType() {
        return type;
    }

    public void draw(Graphics g) {
        g.drawImage(image, x, y, null);
    }
}
