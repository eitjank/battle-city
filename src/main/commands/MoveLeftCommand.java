package main.commands;

import main.tanks.Tank;
import main.enums.Direction;

public class MoveLeftCommand implements Command {

    private Tank tank;

    public MoveLeftCommand(Tank tank) {
        this.tank = tank;
    }

    @Override
    public void execute() {
        tank.setDx(-tank.getSpeed());
        tank.setDy(0);
        tank.setDirection(Direction.LEFT);
    }

    @Override
    public void stopExecution() {
        tank.setDx(0);
    }
}
