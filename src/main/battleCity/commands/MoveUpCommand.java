package battleCity.commands;


import battleCity.enums.Direction;
import battleCity.entities.Tank;

public class MoveUpCommand implements Command {
    private Tank tank;

    public MoveUpCommand(Tank tank) {
        this.tank = tank;
    }

    @Override
    public void execute() {
        tank.setDx(0);
        tank.setDy(-tank.getSpeed());
        tank.setDirection(Direction.UP);
    }

    @Override
    public void stopExecution() {
        tank.setDy(0);
    }
}
