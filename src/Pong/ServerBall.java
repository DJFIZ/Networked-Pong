//ServerBall.java
//Handles the movement and collision connection of the ball on the server side
package Pong;

import java.util.Random;

class ServerBall {

    private int x,                 //x coord for the ball
                y,                 //y coord for the ball
                xMovement,         //speed and direction of the ball on the x-axis
                yMovement,         //speed and direction o the ball on the y-axis
                gameWidth,         //
                gameHeight,        //
                speed = 1,         //
                ballWidth = 20,    //
                ballHeight = 20,   //
                lastPoint;

    private Random rand;

    //Constructor creates a new ball in the middle of the map and gives it a...
    // - random direction if this is the first ball of the game
    // - or a direction based on who scored last, with the ball being 'served' from the player who scored
    ServerBall(int lP, int w, int h){
        rand = new Random();
        lastPoint = lP;
        gameWidth = w;
        gameHeight = h;
        resetBall();
    }

    //generates a random direction for the ball at the beginning of the game
    private int randomDirection(){
        if((rand.nextInt(2)+1) == 1)
            return 1;
        else
            return -1;
    }

    //handles updating the x and y coords of the ball based on the current movement values
    void move() {
        x += xMovement;
        y += yMovement;

        //inverts y direction if ball collides with wall
        if (y < ballHeight / 2) {
            yMovement = -yMovement;
        }
        if (y > gameHeight - ballHeight / 2) {
            yMovement = -yMovement;
        }
        //resets the ball to the centre of the screen once it passes either paddle
        if (x <= ballWidth/2 || x >= gameWidth - (ballWidth/2))
            resetBall();
    }

    //checks if ball is colliding with either paddle
    //inverts x or y direction if ball collides with a paddle
    void checkCollision(int p1, int p2) {
        int paddleHeight = 80;
        int paddleWidth = 20;

        //collision with paddle 1s vertical face
        if (x == 20 + paddleWidth) {
            if (y >= p1 && y <= p1 + paddleHeight) {
                xMovement = -xMovement;
            }
            //collision with paddle 2s vertical face
        } else if (x == gameWidth - 20 - paddleWidth) {
            if (y >= p2 && y <= p2 + paddleHeight) {
                xMovement = -xMovement;
            }
            //collision with paddle 1s horizontal faces
        } else if (y == p2 || y == p2 + paddleHeight) {
            if (x >= gameWidth - 20 - paddleWidth && x <= gameWidth - 20) {
                yMovement = -yMovement;
            }
            //collision with paddle 2s horizontal faces
        } else if (y == p1 || y == p1 + paddleHeight) {
            if (x >= 20 && x <= paddleWidth + 20) {
                yMovement = -yMovement;
            }
        }
    }


    //resets the ball to the middle of the screen with a random direction, maintaining the same speed
    void resetBall(){
        x = gameWidth/2;
        y = gameHeight/2;
        //if(lastPoint == 0){
            xMovement = speed * randomDirection();
        //}
        //else if(lastPoint == 1){
        //    xMovement = speed;
        //}
        //else if(lastPoint == 2){
        //    xMovement = speed * -1;
        //}
        yMovement = speed * randomDirection();
    }

    //getter methods to retrieve ball coords
    int getX() {return x;}

    int getY() {return y;}


}
