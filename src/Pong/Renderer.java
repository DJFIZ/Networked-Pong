//Renderer.java
//renders pong objects to be displayed
package Pong;

import javax.swing.*;
import java.awt.*;

public class Renderer extends JPanel {
    private static final long serialVersionUID = 1L;

    @Override
    protected void paintComponent(Graphics g) {
       super.paintComponent(g);

        PongHandler.pong.render(g);
    }
}
