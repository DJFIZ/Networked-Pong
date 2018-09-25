//ServerPacket.java
//ServerPacket is an object sent to each client containing the necessary information to play multi-player pong
package Pong;

import java.io.Serializable;

class ServerPacket implements Serializable {

    private static final long serialVersionUID = 1L;

    private int playerID,               //ID of the player (1 or 2)
            ballX,                  //x coord of ball
            ballY,                  //y coord of ball
            opponentPaddle;         //position (y coord) of opponents paddle
    private GameState gameState;        //state of the game

    //Constructor assigns values to the necessary variable based on the input args
    ServerPacket(int pID, int x, int y, int oP, GameState gS) {
        playerID = pID;
        ballX = x;
        ballY = y;
        opponentPaddle = oP;
        gameState = gS;
    }

    //getter methods to allow clients to receive the data contained within each packet
    int getPlayerID() {
        return playerID;
    }

    int getBallX() {
        return ballX;
    }

    int getBallY() {
        return ballY;
    }

    int getOpponentPaddle() {
        return opponentPaddle;
    }

    GameState getGameState() {
        return gameState;
    }
}
