package battleCity.commands;

import battleCity.entities.Missile;
import battleCity.entities.Tank;

import java.util.List;

public class FireCommand implements Command {

    private Tank tank;
    private List<Missile> missiles;

    public FireCommand(Tank tank, List<Missile> missiles) {
        this.tank = tank;
        this.missiles = missiles;
    }

    @Override
    public void execute() {
        if(tank.canFire()) {
            missiles.add(tank.fire());
        }
    }


    @Override
    public void stopExecution() {

    }
}
