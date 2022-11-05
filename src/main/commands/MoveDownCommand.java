package main.commands;

import main.tanks.Tank;
import main.enums.Direction;

public class MoveDownCommand implements Command {

    private Tank tank;

    public MoveDownCommand(Tank tank) {
        this.tank = tank;
    }

    @Override
    public void execute() {
        tank.setDx(0);
        tank.setDy(tank.getSpeed());
        tank.setDirection(Direction.DOWN);
    }

    @Override
    public void stopExecution() {
        tank.setDy(0);
    }
}
