package battleCity;

import battleCity.commands.MoveDownCommand;
import battleCity.entities.Missile;
import battleCity.entities.PlayerTank;
import battleCity.enums.Direction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

public class PlayerTankTest {

    @Test
    void shouldTankMove() {
        Image[] tankImages = new Image[1];
        ImageIcon ii = new ImageIcon("img/YellowTank-up.png");
        tankImages[0] = ii.getImage();

        PlayerTank playerTank = new PlayerTank(0,0,10,10,5, tankImages);

        playerTank.setDx(10);

        playerTank.move();

        Assertions.assertEquals(10,playerTank.getX());
        Assertions.assertEquals(0,playerTank.getY());
    }

    @Test
    void shouldTankChangeMoveDirectionWhenMoveDownCommandIsExecuted() {
        Image[] tankImages = new Image[1];
        ImageIcon ii = new ImageIcon("img/YellowTank-up.png");
        tankImages[0] = ii.getImage();

        PlayerTank playerTank = new PlayerTank(0,0,10,10,5, tankImages);

        new MoveDownCommand(playerTank).execute();

        Assertions.assertEquals(Direction.DOWN,playerTank.getDirection());
        Assertions.assertEquals(5,playerTank.getDy());
        Assertions.assertEquals(0,playerTank.getDx());
    }

    @Test
    void shouldTankNotFireTwoMissilesImmediately() {
        Image[] tankImages = new Image[1];
        ImageIcon ii = new ImageIcon("img/YellowTank-up.png");
        tankImages[0] = ii.getImage();

        PlayerTank playerTank = new PlayerTank(0,0,10,10,5, tankImages);

        Assertions.assertNotNull(playerTank.fire());
        Assertions.assertNull(playerTank.fire());
    }

    @Test
    void shouldTankFireMissile() {
        Image[] tankImages = new Image[1];
        ImageIcon ii = new ImageIcon("img/YellowTank-up.png");
        tankImages[0] = ii.getImage();

        PlayerTank playerTank = new PlayerTank(0,0,10,10,5, tankImages);

        Missile firedMissile = playerTank.fire();

        Assertions.assertNotNull(firedMissile);;
    }


}
