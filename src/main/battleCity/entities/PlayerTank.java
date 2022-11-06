package battleCity.entities;

import java.awt.*;

public class PlayerTank extends Tank {

    private int lives = 3;

    public PlayerTank(int x, int y, int width, int height, int speed, Image[] images) {
        super(x, y, width, height, speed, images);
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }
}
