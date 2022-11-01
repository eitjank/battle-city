import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;


public class Board extends JPanel implements ActionListener {

    private Timer timer;
    private final int DELAY = 10;
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
    private ArrayList<Tank> enemyTanks;
    private AITankController aiTankController;
    private Map map;
    private boolean gameOver = false;
    private boolean gameWon = false;

    public Board() {
        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        loadImages();

        resetTanksAndMap();

        timer = new Timer(DELAY, this);
        timer.start(); // Every DELAY ms the timer will call the actionPerformed() method
    }

    private void resetTanksAndMap() {
        playerTank = new Tank(PLAYER_SPAWN_X, PLAYER_SPAWN_Y, 52, 52, 2, PlayerType.Player1);

        enemyTanks = new ArrayList<>();
        enemyTanks.add(new Tank(60, 50, 52, 52, 2, PlayerType.Computer));
        enemyTanks.add(new Tank(100, 200, 52, 52, 2, PlayerType.Computer));

        map = new Map();

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
            for (Missile missile : tank.missiles) {
                g.fillRect(missile.x, missile.y, Missile.size, Missile.size);
            }

        }

        for (var obstacle : map.obstacles) {
            switch (obstacle.type) {

                case Destructible -> {
                    g.drawImage(bricksImage, obstacle.x, obstacle.y, null);
                }
                case Indestructible -> {
                    g.drawImage(steelBricksImage, obstacle.x, obstacle.y, null);
                }
                case PlayerBase -> {
                    g.drawImage(phoenixImage, obstacle.x, obstacle.y, null);
                }
            }
        }

        g.setColor(Color.white);
        for (var missile : playerTank.missiles) {
            g.fillRect(missile.x, missile.y, Missile.size, Missile.size);
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
        for (int i = 0; i < aiTankController.nTanksToSpawn + enemyTanks.size(); i++) {
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
        if (aiTankController.nTanksToSpawn + enemyTanks.size() == 0) {
            gameWon = true;
        }
        int playerLastX = playerTank.x;
        int playerLastY = playerTank.y;

        playerTank.move();
        playerTank.update();

        Rectangle playerRect = playerTank.getBounds();

        for (var obstacle : map.obstacles) {
            if (playerRect.intersects(obstacle.getBounds())) {
                playerTank.x = playerLastX;
                playerTank.y = playerLastY;
            }
        }

        for (var tank : enemyTanks) {
            if (playerRect.intersects(tank.getBounds())) {
                playerTank.x = playerLastX;
                playerTank.y = playerLastY;
            }
        }

        aiTankController.spawnTanks();
        aiTankController.controlTanks();

        enemyTanks.forEach(c -> {
            c.update();
            int xLast = c.x;
            int yLast = c.y;
            c.move();
            Rectangle tankRect = c.getBounds();
            for (var obstacle : map.obstacles) {
                if (tankRect.intersects(obstacle.getBounds())) {
                    c.x = xLast;
                    c.y = yLast;
                    c.dx = 0;
                    c.dy = 0;
                }
            }
            enemyTanks.forEach(t -> {
                if (c != t) {
                    if (tankRect.intersects(t.getBounds())) {
                        c.x = xLast;
                        c.y = yLast;
                        c.dx = 0;
                        c.dy = 0;
                    }
                }
            });
            if (tankRect.intersects(playerRect)) {
                c.x = xLast;
                c.y = yLast;
                c.dx = 0;
                c.dy = 0;
            }

            ArrayList<Obstacle> obstaclesToBeRemoved = new ArrayList<>();
            ArrayList<Missile> missilesToBeRemoved = new ArrayList<>();

            c.missiles.forEach(Missile::move);
            c.missiles.forEach(m -> {
                Rectangle missileRect = m.getBounds();
                map.obstacles.forEach(o -> {
                    if (missileRect.intersects(o.getBounds())) {
                        if (o.type == ObstacleType.Destructible) {
                            obstaclesToBeRemoved.add(o);
                        } else if (o.type == ObstacleType.PlayerBase) {
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
            c.missiles.removeAll(missilesToBeRemoved);
            map.obstacles.removeAll(obstaclesToBeRemoved);
        });

        playerTank.missiles.forEach(Missile::move);

        ArrayList<Tank> tanksToBeRemoved = new ArrayList<>();
        ArrayList<Missile> missilesToBeRemoved = new ArrayList<>();

        for (var missile : playerTank.missiles) {
            Rectangle missileRect = missile.getBounds();
            for (var tank : enemyTanks) {
                if (missileRect.intersects(tank.getBounds())) {
                    tanksToBeRemoved.add(tank);
                    missilesToBeRemoved.add(missile);
                }
            }
        }
        enemyTanks.removeAll(tanksToBeRemoved);
        playerTank.missiles.removeAll(missilesToBeRemoved);

        ArrayList<Obstacle> obstaclesToBeRemoved = new ArrayList<>();
        missilesToBeRemoved = new ArrayList<>();

        for (var missile : playerTank.missiles) {
            Rectangle missileRect = missile.getBounds();
            for (var obstacle : map.obstacles) {

                if (missileRect.intersects(obstacle.getBounds())) {
                    if (obstacle.type == ObstacleType.Destructible) {
                        obstaclesToBeRemoved.add(obstacle);
                    } else if (obstacle.type == ObstacleType.PlayerBase) {
                        obstaclesToBeRemoved.add(obstacle);
                        gameOver = true;
                    }
                    missilesToBeRemoved.add(missile);
                }
            }
        }
        map.obstacles.removeAll(obstaclesToBeRemoved);
        playerTank.missiles.removeAll(missilesToBeRemoved);


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
            if (gameOver || gameWon) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    resetTanksAndMap();
                    gameOver = false;
                    gameWon = false;
                }
            }
            playerTank.keyPressed(e);
        }
    }
}