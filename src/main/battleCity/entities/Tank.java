package battleCity.entities;

import battleCity.enums.Direction;

import java.awt.*;

public abstract class Tank extends GameObject {
    private int dx = 0;
    private int dy = 0;
    private Direction direction = Direction.UP;
    private int speed;
    private static final int BULLET_SPEED = 10;
    private static final int FIRE_COOLDOWN = 15;
    private int currentFireCooldown = 0;
    private Image[] images;

    protected Tank(int x, int y, int width, int height, int speed, Image[] images) {
        super(x, y, width, height);
        this.speed = speed;
        this.images = images;
    }

    public int[] getCenter() {
        return new int[]{x + width / 2, y + height / 2};
    }

    public void update() {
        if (currentFireCooldown > 0) {
            currentFireCooldown--;
        }
    }

    public boolean shouldRandomlyFire() {
        return false;
    }

    public boolean isMoving() {
        return dx != 0 || dy != 0;
    }

    public void move() {
        x += dx;
        y += dy;
    }

    public boolean canFire() {
        return currentFireCooldown <= 0;
    }

    public Missile fire() {
        if(!canFire()){
            return null;
        }
        int missileDx;
        int missileDy;
        int[] center = getCenter();
        int x = center[0];
        int y = center[1];
        switch (direction) {
            case LEFT -> {
                missileDx = -BULLET_SPEED;
                missileDy = 0;
                x -= width / 2;
            }
            case RIGHT -> {
                missileDx = BULLET_SPEED;
                missileDy = 0;
                x += width / 2;
            }
            case UP -> {
                missileDx = 0;
                missileDy = -BULLET_SPEED;
                y -= width / 2;
            }
            case DOWN -> {
                missileDx = 0;
                missileDy = BULLET_SPEED;
                y += width / 2;
            }
            default -> throw new IllegalStateException("Unexpected value: " + direction);
        }
        currentFireCooldown = FIRE_COOLDOWN;
        return new Missile(x - Missile.SIZE / 2, y - Missile.SIZE / 2, missileDx, missileDy);
    }

    public void stopTank() {
        dx = 0;
        dy = 0;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public int getDx() {
        return dx;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public int getDy() {
        return dy;
    }

    public int getSpeed() {
        return speed;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    public void draw(Graphics g) {
        g.drawImage(images[direction.ordinal()], x, y, null);
    }

}
