import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Tank {
    int x;
    int y;
    int dx = 0;
    int dy = 0;
    private int width;
    private int height;
    private Direction direction = Direction.UP;
    private int speed;
    private final int bulletSpeed = 10;
    private static final int FIRE_COOLDOWN = 15;
    private int currentFireCooldown = 0;
    private PlayerType playerType;
    ArrayList<Missile> missiles = new ArrayList<>();

    public Tank(int x, int y, int width, int height, int speed, PlayerType playerType) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.playerType = playerType;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int[] getCenter() {
        return new int[]{x + width / 2, y + height / 2};
    }

    public void update() {
        if (currentFireCooldown > 0) {
            currentFireCooldown--;
        }
    }

    public boolean isMoving() {
        return dx != 0 || dy != 0;
    }

    public void move() {
        x += dx;
        y += dy;
    }
    public void fire() {
        if (currentFireCooldown > 0) {
            return;
        }
        int missileDx, missileDy;
        switch (direction) {
            case LEFT -> {
                missileDx = -bulletSpeed;
                missileDy = 0;
            }
            case RIGHT -> {
                missileDx = bulletSpeed;
                missileDy = 0;
            }
            case UP -> {
                missileDx = 0;
                missileDy = -bulletSpeed;
            }
            case DOWN -> {
                missileDx = 0;
                missileDy = bulletSpeed;
            }
            default -> throw new IllegalStateException("Unexpected value: " + direction);
        }
        int[] center = getCenter();
        missiles.add(new Missile(center[0] - Missile.size / 2, center[1] - Missile.size / 2, missileDx, missileDy));
        currentFireCooldown = FIRE_COOLDOWN;
    }

    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            dx = -speed;
            dy = 0;
            direction = Direction.LEFT;
        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = speed;
            dy = 0;
            direction = Direction.RIGHT;
        }

        if (key == KeyEvent.VK_UP) {
            dy = -speed;
            dx = 0;
            direction = Direction.UP;
        }

        if (key == KeyEvent.VK_DOWN) {
            dy = speed;
            dx = 0;
            direction = Direction.DOWN;
        }

        if (key == KeyEvent.VK_SPACE) {
            fire();
        }
    }

    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            dx = 0;
        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = 0;
        }

        if (key == KeyEvent.VK_UP) {
            dy = 0;
        }

        if (key == KeyEvent.VK_DOWN) {
            dy = 0;
        }

    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public PlayerType getPlayerType() {
        return playerType;
    }

    public void setPlayerType(PlayerType playerType) {
        this.playerType = playerType;
    }


    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
