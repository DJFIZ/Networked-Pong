// PongHandler.java
// Manages the users inputs and the display
package Pong;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PongHandler extends Component implements Runnable, KeyListener {

    public static PongHandler pong;

    //Dimensions of the game
    private final int WIDTH = 700;
    public final int HEIGHT = 500;

    private GameState gameState;
    private Renderer renderer;
    private JFrame jframe;
    private Paddle paddle1;
    private Paddle paddle2;
    private ClientBall ball;

    private boolean wKey, sKey, upArrow, downArrow, spaceBar; //respective boolean values for when a key is pressed/released


    //Constructor creates the display and objects to be displayed
    PongHandler() {
        jframe = new JFrame("Pong");
        JPanel jpanel = new JPanel();
        jpanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        jframe.getContentPane().add(jpanel);
        jframe.pack();
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.add(renderer = new Renderer());
        jframe.addKeyListener(this);
        ball = new ClientBall(WIDTH, HEIGHT);
        paddle1 = new Paddle(this, 1);
        paddle2 = new Paddle(this, 2);
    }

    //allows the jframe to display
    void setVisible() {
        jframe.setVisible(true);
    }

    //renders the various elements of the game
    void render(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.setColor(Color.WHITE);
        g.drawLine(WIDTH / 2, 0, WIDTH / 2, HEIGHT);
        ball.render(g);
        paddle1.render(g);
        paddle2.render(g);
    }

    //getter methods for the games dimensions
    int getWIDTH() {
        return WIDTH;
    }

    int getHEIGHT() {
        return HEIGHT;
    }

    //setter method for updating the gameState as need be
    void setGameState() {
        gameState = PongClient.cC.sP.getGameState();
    }

    //updates opponents paddle to position 'n'
    void updateOpponentPaddle(int n, ClientConnection cC) {
        if (cC.sP.getPlayerID() == 1) {
            paddle2.setY(n);
            renderer.repaint();
        } else {
            paddle1.setY(n);
            renderer.repaint();
        }
    }

    //updates position of the ball via coordinates
    void updateBall(int x, int y) {
        ball.setX(x);
        ball.setY(y);
        renderer.repaint();
    }

    //listens for actions performed
    //'space' will start the game
    //'w' or upArrow will move the current users paddle up and send its location to the other client via server
    //'s' or downArrow will move the current users paddle down and send its location to the other client via server
    private void actionPerformed() {
        if (spaceBar && gameState == GameState.BOTH_CONNECTED) {
            PongClient.cC.sendMovement(1000);
        }
        if (wKey || upArrow) {
            if (PongClient.cC.sP.getPlayerID() == 1) {
                paddle1.moveUp();
                PongClient.cC.sendMovement(paddle1.getY());
            } else {
                paddle2.moveUp();
                PongClient.cC.sendMovement(paddle2.getY());
            }
            renderer.repaint();
        }
        if (sKey || downArrow) {
            if (PongClient.cC.sP.getPlayerID() == 1) {
                paddle1.moveDown();
                PongClient.cC.sendMovement(paddle1.getY());
            } else {
                paddle2.moveDown();
                PongClient.cC.sendMovement(paddle2.getY());
            }
            renderer.repaint();
        }
    }

    @Override
    public void run() {
        pong = this;
        pong.repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    //listens for keys pressed
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_W)
            wKey = true;
        if (keyCode == KeyEvent.VK_S)
            sKey = true;
        if (keyCode == KeyEvent.VK_UP)
            upArrow = true;
        if (keyCode == KeyEvent.VK_DOWN)
            downArrow = true;
        if (keyCode == KeyEvent.VK_SPACE)
            spaceBar = true;
        actionPerformed();
    }

    //listens for keys released
    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_W)
            wKey = false;
        if (keyCode == KeyEvent.VK_S)
            sKey = false;
        if (keyCode == KeyEvent.VK_UP)
            upArrow = false;
        if (keyCode == KeyEvent.VK_DOWN)
            downArrow = false;
        if (keyCode == KeyEvent.VK_SPACE)
            spaceBar = false;
        actionPerformed();
    }
}
