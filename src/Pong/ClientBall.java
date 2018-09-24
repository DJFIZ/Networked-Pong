//ClientBall.java
//handles displaying the ball to the clients, with all physics being handled server-side

package Pong;

import java.awt.*;

class ClientBall {

    private int x;
    private int y;

    ClientBall(int w, int h){
        x = w/2;
        y = h/2;
    }

    void render(Graphics g){
        g.setColor(Color.WHITE);
        int ballWidth = 20;
        int ballHeight = 20;
        g.fillOval(x - ballWidth /2,y - ballHeight /2, ballWidth, ballHeight);
    }

    void setX(int n) {x = n;}

    void setY(int n) {y = n;}
}
