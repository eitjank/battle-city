import java.util.List;
import java.util.Random;

public class AITankController {

    private int nTanksToSpawn = 18;
    private static final int MAX_TANKS = 4;
    private List<Tank> tanks;
    private final Random random = new Random();

    private static final int SPAWN_COOLDOWN = 80;
    private int currentSpawnCooldown = SPAWN_COOLDOWN;

    public AITankController(List<Tank> tanks) {
        this.tanks = tanks;
    }

    public void spawnTanks() {

        if (tanks.size() < MAX_TANKS && nTanksToSpawn > 0) {
            if (currentSpawnCooldown > 0) {
                currentSpawnCooldown--;
                return;
            }
            currentSpawnCooldown = SPAWN_COOLDOWN;
            if (random.nextInt(2)==1) {
                int spawnX;
                int spawnY;
                int randomNumber = random.nextInt(4);
                switch (randomNumber) {
                    case 0 -> {
                        spawnX = 1 * 26;
                        spawnY = 1 * 26;
                    }
                    case 1 -> {
                        spawnX = 8 * 26;
                        spawnY = 1 * 26;
                    }
                    case 2 -> {
                        spawnX = 1 * 26;
                        spawnY = 8 * 26;
                    }
                    case 3 -> {
                        spawnX = 19 * 26;
                        spawnY = 1 * 26;
                    }
                    default -> throw new IllegalStateException("Unexpected value: " + randomNumber);
                }

                tanks.add(new Tank(spawnX, spawnY, 52, 52, 2, PlayerType.Computer));
                nTanksToSpawn--;
            }
        }
    }

    public void controlTanks() {
        for (var tank : tanks) {
            double p = Math.random();
            if (tank.isMoving() && p < 0.0000005) {
                changeDirection(tank);
            } else if (p < 0.02) {
                changeDirection(tank);
            }
            p = Math.random();
            if (p < 0.005) {
                tank.fire();
            }
        }
    }

    private void changeDirection(Tank tank) {
        int speed = tank.getSpeed();
        int randomNumber = random.nextInt(5);

        switch (randomNumber){

            case 0 -> {
                tank.setDx(0);
                tank.setDy(0);
            }
            case 1 -> {
                tank.setDx(-speed);
                tank.setDy(0);
                tank.setDirection(Direction.LEFT);
            }
            case 2 -> {
                tank.setDx(0);
                tank.setDy(-speed);
                tank.setDirection(Direction.UP);
            }
            case 3 -> {
                tank.setDx(speed);
                tank.setDy(0);
                tank.setDirection(Direction.RIGHT);
            }
            case 4 -> {
                tank.setDx(0);
                tank.setDy(speed);
                tank.setDirection(Direction.DOWN);
            }
            default -> throw new IllegalStateException("Unexpected value: " + randomNumber);
        }

    }

    public int getnTanksToSpawn() {
        return nTanksToSpawn;
    }
}
