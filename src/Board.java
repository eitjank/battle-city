import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;


public class Board extends JPanel implements ActionListener {

    private Timer timer;
    private static final int DELAY = 10;
    private Image tankImageUp;
    private Image tankImageDown;
    private Image tankImageLeft;
    private Image tankImageRight;
    private Image tankLivesImage;
    private Image enemyTankImageUp;
    private Image enemyTankImageDown;
    private Image enemyTankImageLeft;
    private Image enemyTankImageRight;
    private Image enemyTankLivesImage;

    private Image bricksImage;
    private Image steelBricksImage;
    private Image phoenixImage;
    private Image gameWonImage;
    private Image gameOverImage;

    private Tank playerTank;
    private int lives = 3;
    private static final int PLAYER_SPAWN_X = 8 * 26;
    private static final int PLAYER_SPAWN_Y = 16 * 26;
    private static final int TANKS_LEFT_ROW_WIDTH = 4;
    private List<Tank> enemyTanks;
    private AITankController aiTankController;
    private Map map;
    private boolean gameOver = false;
    private boolean gameWon = false;

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
        playerTank = new Tank(PLAYER_SPAWN_X, PLAYER_SPAWN_Y, 52, 52, 2, PlayerType.Player1);

        enemyTanks = new ArrayList<>();
        enemyTanks.add(new Tank(60, 50, 52, 52, 2, PlayerType.Computer));
        enemyTanks.add(new Tank(100, 200, 52, 52, 2, PlayerType.Computer));

        map.resetMap();

        aiTankController = new AITankController(enemyTanks);

        lives=3;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        switch (playerTank.getDirection()) {
            case LEFT -> {
                g.drawImage(tankImageLeft, playerTank.getX(), playerTank.getY(), null);
            }
            case RIGHT -> {
                g.drawImage(tankImageRight, playerTank.getX(), playerTank.getY(), null);
            }
            case UP -> {
                g.drawImage(tankImageUp, playerTank.getX(), playerTank.getY(), null);
            }
            case DOWN -> {
                g.drawImage(tankImageDown, playerTank.getX(), playerTank.getY(), null);
            }
        }
        for (Tank tank : enemyTanks) {
            switch (tank.getDirection()) {
                case LEFT -> {
                    g.drawImage(enemyTankImageLeft, tank.getX(), tank.getY(), null);
                }
                case RIGHT -> {
                    g.drawImage(enemyTankImageRight, tank.getX(), tank.getY(), null);
                }
                case UP -> {
                    g.drawImage(enemyTankImageUp, tank.getX(), tank.getY(), null);
                }
                case DOWN -> {
                    g.drawImage(enemyTankImageDown, tank.getX(), tank.getY(), null);
                }
            }

            g.setColor(Color.white);
            for (Missile missile : tank.getMissiles()) {
                g.fillRect(missile.getX(), missile.getY(), Missile.SIZE, Missile.SIZE);
            }

        }

        for (var obstacle : map.getObstacles()) {
            switch (obstacle.getType()) {

                case DESTRUCTIBLE -> {
                    g.drawImage(bricksImage, obstacle.getX(), obstacle.getY(), null);
                }
                case INDESTRUCTIBLE -> {
                    g.drawImage(steelBricksImage, obstacle.getX(), obstacle.getY(), null);
                }
                case PLAYER_BASE -> {
                    g.drawImage(phoenixImage, obstacle.getX(), obstacle.getY(), null);
                }
            }
        }

        g.setColor(Color.white);
        for (var missile : playerTank.getMissiles()) {
            g.fillRect(missile.getX(), missile.getY(), Missile.SIZE, Missile.SIZE);
        }

        if (gameWon) {
            g.drawImage(gameWonImage, 110, 150, null);
            g.drawString("Press enter to play again", 200, 550);
        } else if (gameOver) {
            g.drawImage(gameOverImage, 130, 160, null);
            g.drawString("Press enter to play again", 200, 550);
        }

        g.drawString("Lives:", 610, 20);
        for (int i = 0; i < lives; i++) {
            g.drawImage(tankLivesImage, 605 + 30 * i, 30, null);
        }

        g.drawString("Tanks left:", 610, 200);
        for (int i = 0; i < aiTankController.getnTanksToSpawn() + enemyTanks.size(); i++) {
            g.drawImage(enemyTankLivesImage, 605 + 30 * (i % TANKS_LEFT_ROW_WIDTH),
                    210 + 30 * (i / TANKS_LEFT_ROW_WIDTH), null);
        }

        Toolkit.getDefaultToolkit().sync(); // synchronises the painting on systems that buffer graphics events
    }

    @Override
    public void actionPerformed(ActionEvent e) { // one game cycle
        if (gameOver) {
            return;
        }
        if (aiTankController.getnTanksToSpawn() + enemyTanks.size() == 0) {
            gameWon = true;
        }
        int playerLastX = playerTank.getX();
        int playerLastY = playerTank.getY();

        playerTank.move();
        playerTank.update();

        Rectangle playerRect = playerTank.getBounds();

        for (var obstacle : map.getObstacles()) {
            if (playerRect.intersects(obstacle.getBounds())) {
                playerTank.setX(playerLastX);
                playerTank.setY(playerLastY);
            }
        }

        for (var tank : enemyTanks) {
            if (playerRect.intersects(tank.getBounds())) {
                playerTank.setX(playerLastX);
                playerTank.setY(playerLastY);
            }
        }

        aiTankController.spawnTanks();
        aiTankController.controlTanks();

        enemyTanks.forEach(c -> {
            c.update();
            int xLast = c.getX();
            int yLast = c.getY();
            c.move();
            Rectangle tankRect = c.getBounds();
            for (var obstacle : map.getObstacles()) {
                if (tankRect.intersects(obstacle.getBounds())) {
                    c.setX(xLast);
                    c.setY(yLast);
                    c.setDx(0);
                    c.setDy(0);
                }
            }
            enemyTanks.forEach(t -> {
                if (c != t && tankRect.intersects(t.getBounds())) {
                    c.setX(xLast);
                    c.setY(yLast);
                    c.setDx(0);
                    c.setDy(0);
                }
            });
            if (tankRect.intersects(playerRect)) {
                c.setX(xLast);
                c.setY(yLast);
                c.setDx(0);
                c.setDy(0);
            }

            ArrayList<Obstacle> obstaclesToBeRemoved = new ArrayList<>();
            ArrayList<Missile> missilesToBeRemoved = new ArrayList<>();

            c.getMissiles().forEach(Missile::move);
            c.getMissiles().forEach(m -> {
                Rectangle missileRect = m.getBounds();
                map.getObstacles().forEach(o -> {
                    if (missileRect.intersects(o.getBounds())) {
                        if (o.getType() == ObstacleType.DESTRUCTIBLE) {
                            obstaclesToBeRemoved.add(o);
                        } else if (o.getType() == ObstacleType.PLAYER_BASE) {
                            obstaclesToBeRemoved.add(o);
                            gameOver = true;
                        }
                        missilesToBeRemoved.add(m);
                    }
                });

                enemyTanks.forEach(c2 -> {
                    if (c != c2) {
                        Rectangle tankRect2 = c2.getBounds();
                        if (missileRect.intersects(tankRect2)) {
                            missilesToBeRemoved.add(m);
                        }
                    }
                });

                if (missileRect.intersects(playerRect)) {
                    if (lives > 0) {
                        lives--;
                        playerTank.setX(PLAYER_SPAWN_X);
                        playerTank.setY(PLAYER_SPAWN_Y);
                    } else {
                        gameOver = true;
                    }
                }
            });
            c.getMissiles().removeAll(missilesToBeRemoved);
            map.getObstacles().removeAll(obstaclesToBeRemoved);
        });

        playerTank.getMissiles().forEach(Missile::move);

        ArrayList<Tank> tanksToBeRemoved = new ArrayList<>();
        ArrayList<Missile> missilesToBeRemoved = new ArrayList<>();

        for (var missile : playerTank.getMissiles()) {
            Rectangle missileRect = missile.getBounds();
            for (var tank : enemyTanks) {
                if (missileRect.intersects(tank.getBounds())) {
                    tanksToBeRemoved.add(tank);
                    missilesToBeRemoved.add(missile);
                }
            }
        }
        enemyTanks.removeAll(tanksToBeRemoved);
        playerTank.getMissiles().removeAll(missilesToBeRemoved);

        ArrayList<Obstacle> obstaclesToBeRemoved = new ArrayList<>();
        missilesToBeRemoved = new ArrayList<>();

        for (var missile : playerTank.getMissiles()) {
            Rectangle missileRect = missile.getBounds();
            for (var obstacle : map.getObstacles()) {

                if (missileRect.intersects(obstacle.getBounds())) {
                    if (obstacle.getType() == ObstacleType.DESTRUCTIBLE) {
                        obstaclesToBeRemoved.add(obstacle);
                    } else if (obstacle.getType() == ObstacleType.PLAYER_BASE) {
                        obstaclesToBeRemoved.add(obstacle);
                        gameOver = true;
                    }
                    missilesToBeRemoved.add(missile);
                }
            }
        }
        map.getObstacles().removeAll(obstaclesToBeRemoved);
        playerTank.getMissiles().removeAll(missilesToBeRemoved);


        repaint();
    }

    private void loadImages() {
        ImageIcon ii = new ImageIcon("img/YellowTank-up.png");
        tankImageUp = ii.getImage();
        ii = new ImageIcon("img/YellowTank-down.png");
        tankImageDown = ii.getImage();
        ii = new ImageIcon("img/YellowTank-left.png");
        tankImageLeft = ii.getImage();
        ii = new ImageIcon("img/YellowTank-right.png");
        tankImageRight = ii.getImage();
        ii = new ImageIcon("img/YellowTank-up-small.png");
        tankLivesImage = ii.getImage();

        ii = new ImageIcon("img/GrayTank-up.png");
        enemyTankImageUp = ii.getImage();
        ii = new ImageIcon("img/GrayTank-down.png");
        enemyTankImageDown = ii.getImage();
        ii = new ImageIcon("img/GrayTank-left.png");
        enemyTankImageLeft = ii.getImage();
        ii = new ImageIcon("img/GrayTank-right.png");
        enemyTankImageRight = ii.getImage();
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
            playerTank.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if ((gameOver || gameWon) && e.getKeyCode() == KeyEvent.VK_ENTER) {
                resetTanksAndMap();
                gameOver = false;
                gameWon = false;
            }
            playerTank.keyPressed(e);
        }
    }
}