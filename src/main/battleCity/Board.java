package battleCity;

import battleCity.commands.*;
import battleCity.enums.Direction;
import battleCity.enums.ObstacleType;
import battleCity.entities.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Board extends JPanel implements ActionListener {

    private Timer timer;
    private static final int DELAY = 10;
    private final Image[] tankImages = new Image[4];
    private Image tankLivesImage;
    private final Image[] enemyTankImages = new Image[4];
    private Image enemyTankLivesImage;
    private Image bricksImage;
    private Image steelBricksImage;
    private Image phoenixImage;
    private Image gameWonImage;
    private Image gameOverImage;
    private static final int PLAYER_SPAWN_X = 8 * Map.OBSTACLE_SIZE;
    private static final int PLAYER_SPAWN_Y = 16 * Map.OBSTACLE_SIZE;
    private static final int TANKS_LEFT_ROW_WIDTH = 4;
    private static final int[] PLAY_AGAIN_INSTRUCTIONS_POSITION = {200, 550};
    private static final int[] GAME_END_IMAGE_POSITION = {110, 150};
    private static final int[] PLAYER_LIVES_POSITION = {610, 20};
    private static final int[] ENEMIES_LEFT_POSITION = {610, 200};
    private PlayerTank playerTank;
    private List<Tank> tanks;
    private List<Missile> missiles;
    private EnemyTankSpawner enemyTankSpawner;
    private Map map;
    private boolean gameOver = false;
    private boolean gameWon = false;
    private final java.util.Map<Integer, Command> commandMap = new HashMap<>();
    public static final int TANK_WIDTH = 52;
    public static final int TANK_HEIGHT = 52;
    public static final int TANK_SPEED = 2;

    public Board() {
        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        loadImages();

        map = Map.getInstance();
        resetTanksAndMap();

        timer = new Timer(DELAY, this);
        timer.start(); // Every DELAY ms the timer will call the actionPerformed() method
    }

    private void resetTanksAndMap() {
        playerTank = new PlayerTank(PLAYER_SPAWN_X, PLAYER_SPAWN_Y, TANK_WIDTH, TANK_HEIGHT, TANK_SPEED, tankImages);

        tanks = new ArrayList<>();
        missiles = new ArrayList<>();

        tanks.add(playerTank);

        tanks.add(new EnemyTank(EnemyTankSpawner.SPAWN_POSITIONS[0][0],
                EnemyTankSpawner.SPAWN_POSITIONS[0][1], TANK_WIDTH, TANK_HEIGHT, TANK_SPEED, enemyTankImages));
        tanks.add(new EnemyTank(EnemyTankSpawner.SPAWN_POSITIONS[1][0],
                EnemyTankSpawner.SPAWN_POSITIONS[1][1], TANK_WIDTH, TANK_HEIGHT, TANK_SPEED, enemyTankImages));

        map.resetMap(bricksImage, steelBricksImage, phoenixImage);

        enemyTankSpawner = new EnemyTankSpawner(tanks, enemyTankImages);

        commandMap.put(KeyEvent.VK_LEFT, new MoveLeftCommand(playerTank));
        commandMap.put(KeyEvent.VK_UP, new MoveUpCommand(playerTank));
        commandMap.put(KeyEvent.VK_RIGHT, new MoveRightCommand(playerTank));
        commandMap.put(KeyEvent.VK_DOWN, new MoveDownCommand(playerTank));
        commandMap.put(KeyEvent.VK_SPACE, new FireCommand(playerTank, missiles));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Tank tank : tanks) {
            tank.draw(g);
        }

        for (var obstacle : map.getObstacles()) {
            obstacle.draw(g);
        }

        for (Missile missile : missiles) {
            missile.draw(g);
        }

        drawGameEndScreen(g);

        drawPlayerLives(g);

        drawEnemyTanksLeftToDestroy(g);

        Toolkit.getDefaultToolkit().sync(); // synchronises the painting on systems that buffer graphics events
    }

    private void drawEnemyTanksLeftToDestroy(Graphics g) {
        g.drawString("Tanks left:", ENEMIES_LEFT_POSITION[0], ENEMIES_LEFT_POSITION[1]);
        for (int i = 0; i < enemyTankSpawner.getNumberOfTanksToSpawn() + tanks.size() - 1; i++) {
            g.drawImage(enemyTankLivesImage, ENEMIES_LEFT_POSITION[0] + 30 * (i % TANKS_LEFT_ROW_WIDTH),
                    ENEMIES_LEFT_POSITION[1] + 10 + 30 * (i / TANKS_LEFT_ROW_WIDTH), null);
        }
    }

    private void drawPlayerLives(Graphics g) {
        g.drawString("Lives:", PLAYER_LIVES_POSITION[0], PLAYER_LIVES_POSITION[1]);
        for (int i = 0; i < playerTank.getLives(); i++) {
            g.drawImage(tankLivesImage, PLAYER_LIVES_POSITION[0] + 30 * i, PLAYER_LIVES_POSITION[1] + 10, null);
        }
    }

    private void drawGameEndScreen(Graphics g) {
        g.setColor(Color.white);
        if (gameWon) {
            g.drawImage(gameWonImage, GAME_END_IMAGE_POSITION[0], GAME_END_IMAGE_POSITION[1], null);
            g.drawString("Press enter to play again", PLAY_AGAIN_INSTRUCTIONS_POSITION[0],
                    PLAY_AGAIN_INSTRUCTIONS_POSITION[1]);
        } else if (gameOver) {
            g.drawImage(gameOverImage, GAME_END_IMAGE_POSITION[0], GAME_END_IMAGE_POSITION[1], null);
            g.drawString("Press enter to play again", PLAY_AGAIN_INSTRUCTIONS_POSITION[0],
                    PLAY_AGAIN_INSTRUCTIONS_POSITION[1]);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) { // one game cycle
        if (gameOver) {
            return;
        }
        if (enemyTankSpawner.getNumberOfTanksToSpawn() + tanks.size() == 1) {
            gameWon = true;
        }

        enemyTankSpawner.spawnTanks();

        missiles.forEach(Missile::move);

        doMissilesCollisions();

        for (Tank tank : tanks) {
            if (tank.shouldRandomlyFire() && tank.canFire()) {
                missiles.add(tank.fire());
            }
            tank.update();
            int xLast = tank.getX();
            int yLast = tank.getY();
            tank.move();
            Rectangle tankRect = tank.getBounds();

            doTankAndObstaclesCollisions(tank, xLast, yLast, tankRect);

            doTanksCollisions(tank, xLast, yLast, tankRect);
        }

        repaint();
    }

    private void doTanksCollisions(Tank tank, int xLast, int yLast, Rectangle tankRect) {
        for (Tank otherTank : tanks) {
            if (tank != otherTank && tankRect.intersects(otherTank.getBounds())) {
                tank.setX(xLast);
                tank.setY(yLast);
                tank.stopTank();
            }
        }
    }

    private void doTankAndObstaclesCollisions(Tank tank, int xLast, int yLast, Rectangle tankRect) {
        for (var obstacle : map.getObstacles()) {
            if (tankRect.intersects(obstacle.getBounds())) {
                tank.setX(xLast);
                tank.setY(yLast);
                tank.stopTank();
            }
        }
    }

    private void doMissilesCollisions() {
        List<Tank> tanksToBeRemoved = new ArrayList<>();
        List<Missile> missilesToBeRemoved = new ArrayList<>();
        List<Obstacle> obstaclesToBeRemoved = new ArrayList<>();
        for (Missile missile : missiles) {
            Rectangle missileRect = missile.getBounds();

            doMissilesAndTanksCollisions(tanksToBeRemoved, missilesToBeRemoved, missile, missileRect);

            doMissilesAndObstaclesCollisions(missilesToBeRemoved, obstaclesToBeRemoved, missile, missileRect);
        }
        map.getObstacles().removeAll(obstaclesToBeRemoved);
        missiles.removeAll(missilesToBeRemoved);
        tanks.removeAll(tanksToBeRemoved);
    }

    private void doMissilesAndObstaclesCollisions(List<Missile> missilesToBeRemoved, List<Obstacle> obstaclesToBeRemoved, Missile missile, Rectangle missileRect) {
        for (Obstacle obstacle : map.getObstacles()) {
            if (missileRect.intersects(obstacle.getBounds())) {
                missilesToBeRemoved.add(missile);
                if (obstacle.getType() == ObstacleType.DESTRUCTIBLE) {
                    obstaclesToBeRemoved.add(obstacle);
                } else if (obstacle.getType() == ObstacleType.PLAYER_BASE) {
                    obstaclesToBeRemoved.add(obstacle);
                    gameOver = true;
                }
            }
        }
    }

    private void doMissilesAndTanksCollisions(List<Tank> tanksToBeRemoved, List<Missile> missilesToBeRemoved, Missile missile, Rectangle missileRect) {
        for (Tank tank : tanks) {
            if (missileRect.intersects(tank.getBounds())) {
                missilesToBeRemoved.add(missile);
                removeTank(tanksToBeRemoved, tank);
            }
        }
    }

    private void removeTank(List<Tank> tanksToBeRemoved, Tank tank) {
        if (tank == playerTank) {
            respawnPlayerTank();
        } else {
            tanksToBeRemoved.add(tank);
        }
    }

    private void respawnPlayerTank() {
        int lives = playerTank.getLives() - 1;
        playerTank.setLives(lives);
        if (lives > 0) {
            playerTank.setX(PLAYER_SPAWN_X);
            playerTank.setY(PLAYER_SPAWN_Y);
        } else {
            gameOver = true;
        }
    }

    private void loadImages() {
        ImageIcon ii = new ImageIcon("img/YellowTank-up.png");
        tankImages[Direction.UP.ordinal()] = ii.getImage();
        ii = new ImageIcon("img/YellowTank-down.png");
        tankImages[Direction.DOWN.ordinal()] = ii.getImage();
        ii = new ImageIcon("img/YellowTank-left.png");
        tankImages[Direction.LEFT.ordinal()] = ii.getImage();
        ii = new ImageIcon("img/YellowTank-right.png");
        tankImages[Direction.RIGHT.ordinal()] = ii.getImage();
        ii = new ImageIcon("img/YellowTank-up-small.png");
        tankLivesImage = ii.getImage();

        ii = new ImageIcon("img/GrayTank-up.png");
        enemyTankImages[Direction.UP.ordinal()] = ii.getImage();
        ii = new ImageIcon("img/GrayTank-down.png");
        enemyTankImages[Direction.DOWN.ordinal()] = ii.getImage();
        ii = new ImageIcon("img/GrayTank-left.png");
        enemyTankImages[Direction.LEFT.ordinal()] = ii.getImage();
        ii = new ImageIcon("img/GrayTank-right.png");
        enemyTankImages[Direction.RIGHT.ordinal()] = ii.getImage();
        ii = new ImageIcon("img/GrayTank-up-small.png");
        enemyTankLivesImage = ii.getImage();

        ii = new ImageIcon("img/GameOver.png");
        gameOverImage = ii.getImage();
        ii = new ImageIcon("img/YouWin.png");
        gameWonImage = ii.getImage();

        ii = new ImageIcon("img/Bricks.png");
        bricksImage = ii.getImage();
        ii = new ImageIcon("img/SteelBricks.png");
        steelBricksImage = ii.getImage();
        ii = new ImageIcon("img/Phoenix.png");
        phoenixImage = ii.getImage();
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();
            if (commandMap.get(key) != null) {
                commandMap.get(key).stopExecution();
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (commandMap.get(key) != null) {
                commandMap.get(key).execute();
            }
            if ((gameOver || gameWon) && key == KeyEvent.VK_ENTER) {
                resetTanksAndMap();
                gameOver = false;
                gameWon = false;
            }
        }
    }
}