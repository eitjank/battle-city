package battleCity.entities;

import battleCity.commands.MoveDownCommand;
import battleCity.commands.MoveLeftCommand;
import battleCity.commands.MoveRightCommand;
import battleCity.commands.MoveUpCommand;

import java.awt.*;
import java.util.Random;

public class EnemyTank extends Tank {

    private static final Random random = new Random();
    private final MoveDownCommand moveDownCommand;
    private final MoveLeftCommand moveLeftCommand;
    private final MoveRightCommand moveRightCommand;
    private final MoveUpCommand moveUpCommand;

    private static final double PROBABILITY_TO_CHANGE_DIRECTION_WHEN_MOVING = 0.005;
    private static final double PROBABILITY_TO_CHANGE_DIRECTION_WHEN_IDLE = 0.06;

    public EnemyTank(int x, int y, int width, int height, int speed, Image[] images) {
        super(x, y, width, height, speed, images);
        moveDownCommand = new MoveDownCommand(this);
        moveLeftCommand = new MoveLeftCommand(this);
        moveRightCommand = new MoveRightCommand(this);
        moveUpCommand = new MoveUpCommand(this);
    }

    @Override
    public void update() {
        super.update();
        double p = Math.random();
        if (isMoving())  {
            if(p <= PROBABILITY_TO_CHANGE_DIRECTION_WHEN_MOVING){
                changeDirection();
            }
        } else if (p <= PROBABILITY_TO_CHANGE_DIRECTION_WHEN_IDLE){
            changeDirection();
        }
    }

    @Override
    public boolean shouldRandomlyFire() {
        return Math.random() <= 0.005;
    }

    private void changeDirection() {
        int randomNumber = random.nextInt(5);

        switch (randomNumber) {
            case 0 -> stopTank();
            case 1 -> moveLeftCommand.execute();
            case 2 -> moveUpCommand.execute();
            case 3 -> moveRightCommand.execute();
            case 4 -> moveDownCommand.execute();
            default -> throw new IllegalStateException("Unexpected value: " + randomNumber);
        }

    }
}
