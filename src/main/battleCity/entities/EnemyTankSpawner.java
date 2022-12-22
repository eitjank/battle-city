package battleCity.entities;

import battleCity.Board;
import battleCity.Map;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class EnemyTankSpawner {

    private int numberOfTanksToSpawn = 18;
    private static final int MAX_TANKS = 4;
    private final List<Tank> tanks;
    private final Random random = new Random();

    private static final int SPAWN_COOLDOWN = 50;
    private int currentSpawnCooldown = SPAWN_COOLDOWN;
    private final Image[] tankImages;
    public static final int[][] SPAWN_POSITIONS = {
            {Map.OBSTACLE_SIZE, Map.OBSTACLE_SIZE},
            {8 * Map.OBSTACLE_SIZE, Map.OBSTACLE_SIZE},
            {Map.OBSTACLE_SIZE, 8 * Map.OBSTACLE_SIZE},
            {19 * Map.OBSTACLE_SIZE, Map.OBSTACLE_SIZE},
    };

    public EnemyTankSpawner(List<battleCity.entities.Tank> tanks, Image[] tankImages) {
        this.tanks = tanks;
        this.tankImages = tankImages;
    }

    public void spawnTanks() {

        if (tanks.size() < MAX_TANKS && numberOfTanksToSpawn > 0) {
            if (currentSpawnCooldown > 0) {
                currentSpawnCooldown--;
                return;
            }
            currentSpawnCooldown = SPAWN_COOLDOWN;
            if (random.nextInt(2) == 1) {
                int randomNumber = random.nextInt(4);
                int spawnX = SPAWN_POSITIONS[randomNumber][0];
                int spawnY = SPAWN_POSITIONS[randomNumber][1];
                if(isTankSpawnOccupied(spawnX, spawnY)){
                    return;
                }
                tanks.add(new battleCity.entities.EnemyTank(spawnX, spawnY, Board.TANK_WIDTH, Board.TANK_HEIGHT, Board.TANK_SPEED, tankImages));
                numberOfTanksToSpawn--;
            }
        }
    }

    private boolean isTankSpawnOccupied(int spawnX, int spawnY) {
        for(Tank tank : tanks) {
            if (new Rectangle(spawnX, spawnY, Board.TANK_WIDTH, Board.TANK_HEIGHT).intersects(tank.getBounds())){
                return true;
            }
        }
        return false;
    }

    public int getNumberOfTanksToSpawn() {
        return numberOfTanksToSpawn;
    }
}
