package battleCity;

import javax.swing.*;
import java.awt.*;

public class Application extends JFrame {

    public Application() {
        add(new Board());

        setSize(800, 750);
        setTitle("Battle city");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Passing null to the setLocationRelativeTo() method centers the window on the screen.
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            Application ex = new Application();
            ex.setVisible(true);
        });
    }
}
