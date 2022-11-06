package battleCity;

import battleCity.entities.Missile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MissileTest {

    @Test
    void shouldMissileMove() {
        Missile missile = new Missile(0,0,5,0);

        missile.move();

        Assertions.assertEquals(5,missile.getX());
        Assertions.assertEquals(0,missile.getY());
    }
}
