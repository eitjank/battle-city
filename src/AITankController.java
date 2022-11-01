import java.util.ArrayList;
import java.util.Random;

public class AITankController {

    public int nTanksToSpawn = 18;
    private final int MAX_TANKS = 4;
    private ArrayList<Tank> tanks;
    private final Random random = new Random();

    private static final int SPAWN_COOLDOWN = 100;
    private int currentSpawnCooldown = SPAWN_COOLDOWN;

    public AITankController(ArrayList<Tank> tanks) {
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
                int spawnX, spawnY;
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
                tank.dx = 0;
                tank.dy = 0;
            }
            case 1 -> {
                tank.dx = -speed;
                tank.dy = 0;
                tank.setDirection(Direction.LEFT);
            }
            case 2 -> {
                tank.dx = 0;
                tank.dy = -speed;
                tank.setDirection(Direction.UP);
            }
            case 3 -> {
                tank.dx = speed;
                tank.dy = 0;
                tank.setDirection(Direction.RIGHT);
            }
            case 4 -> {
                tank.dx = 0;
                tank.dy = speed;
                tank.setDirection(Direction.DOWN);
            }
        }

    }
}
