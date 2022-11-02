import java.util.ArrayList;
import java.util.List;

public class Map {

    private static final int OBSTACLE_SIZE = 26;
    private static final int PLAYER_BASE_SIZE = OBSTACLE_SIZE * 2;
    private List<Obstacle> obstacles = new ArrayList<>();
    private static Map mapObject;
    private static final int[][] MATRIX = {
            {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
            {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2},
            {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2},
            {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2},
            {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2},
            {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2},
            {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2},
            {2, 2, 2, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 2},
            {2, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 2},
            {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2},
            {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2},
            {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2},
            {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 2},
            {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 2},
            {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 2},
            {2, 1, 1, 0, 0, 0, 0, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 2},
            {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 3, 0, 1, 0, 0, 0, 0, 0, 0, 2},
            {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 2},
            {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
    };

    private Map() {
    }

    public static Map getInstance() {

        if (mapObject == null) {
            mapObject = new Map();
        }

        return mapObject;
    }

    public void resetMap() {
        for (int i = 0; i < MATRIX.length; i++) {
            for (int j = 0; j < MATRIX[i].length; j++) {
                if (MATRIX[i][j] == 1)
                    obstacles.add(new Obstacle(j * OBSTACLE_SIZE, i * OBSTACLE_SIZE,
                            OBSTACLE_SIZE, OBSTACLE_SIZE, ObstacleType.DESTRUCTIBLE));
                else if (MATRIX[i][j] == 2)
                    obstacles.add(new Obstacle(j * OBSTACLE_SIZE, i * OBSTACLE_SIZE,
                            OBSTACLE_SIZE, OBSTACLE_SIZE, ObstacleType.INDESTRUCTIBLE));
                else if (MATRIX[i][j] == 3)
                    obstacles.add(new Obstacle(j * OBSTACLE_SIZE, i * OBSTACLE_SIZE,
                            PLAYER_BASE_SIZE, PLAYER_BASE_SIZE, ObstacleType.PLAYER_BASE));
            }
        }
    }

    public List<Obstacle> getObstacles() {
        return obstacles;
    }

}
