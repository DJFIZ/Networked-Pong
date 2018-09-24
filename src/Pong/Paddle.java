//Paddle.java
//Paddle is an object
package Pong;

import java.awt.*;

class Paddle {

    private int x,                      //x coord of the paddle
                paddleWidth = 20,       //width of the paddle
                paddleHeight = 80,      //height of the paddle
                speed = 10;             //speed at which the paddle moves up and down
    private double y;                   //y coord of the paddle


    //Constructor creates paddle at either side of the GUI based on what number paddle is being created
    Paddle(PongHandler handler, int paddleNum) {

        if (paddleNum == 1)
            x = 20;
        if (paddleNum == 2)
            x = handler.getWIDTH() - paddleWidth - 20;
            y = handler.getHEIGHT()/2 - paddleHeight/2;
        }

        //renders the paddle for display
        void render(Graphics g) {
            g.setColor(Color.WHITE);
            g.fillRect(x, (int)y, paddleWidth , paddleHeight);
        }

        //increments the paddles y coord based on the speed
        void moveUp() {
            if (y - speed > 0)
                y -= speed;
            else
                y = 0;
        }

        //decrements the paddles t coord based on the speed
        void moveDown() {
            if (y + paddleHeight + speed < PongHandler.pong.HEIGHT)
                y += speed;
            else
                y = PongHandler.pong.HEIGHT - paddleHeight;
        }

        //setter method utilized when moving the opponents paddle
        void setY(int n){
            y = n;
        }

        //getter method to obtain paddles location on the y axis
        int getY(){
            return (int)y;
        }
    }
