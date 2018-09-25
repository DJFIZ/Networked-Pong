//ClientBall.java
//handles displaying the ball to the clients, with all physics being handled server-side

package Pong;

import java.awt.*;

class ClientBall {

    private int x, //x coord of the ball
            y; //y coord of the ball

    //constructor takes parameters of width and height of the game to place the ball at the center
    ClientBall(int w, int h) {
        x = w / 2;
        y = h / 2;
    }

    //renders the ball to the display
    void render(Graphics g) {
        g.setColor(Color.WHITE);
        int ballWidth = 20;
        int ballHeight = 20;
        g.fillOval(x - ballWidth / 2, y - ballHeight / 2, ballWidth, ballHeight);
    }

    //setter methods to be used when receiving ball coords from server
    void setX(int n) {
        x = n;
    }

    void setY(int n) {
        y = n;
    }
}
