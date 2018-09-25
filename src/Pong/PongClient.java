//PongClient.java
//Basic client
package Pong;

import java.awt.*;

public class PongClient extends Component implements Runnable {

    private int pN; //portNumber

    private String hN; //hostName

    static ClientConnection cC;

    //Constructor receives and assigns portNumber and hostName
    PongClient(String[] args) {
        this.hN = args[0];
        this.pN = Integer.parseInt(args[1]);
    }

    //runs a constant loop to have pong update the gameState, opponent's paddle position, and the position of the ball
    private void updateGame() {
        PongHandler.pong.setVisible(); //makes the display visible once both players connect
        while (true) {
            PongHandler.pong.setGameState();
            PongHandler.pong.updateOpponentPaddle(cC.sP.getOpponentPaddle(), cC);
            PongHandler.pong.updateBall(cC.sP.getBallX(), cC.sP.getBallY());
        }
    }


    //Creates a thread of pongHandler but catches itself in a loop until both players have connected
    @Override
    public void run() {
        int i = 0; //local variable to ensure connecting message is only displayed once
        Thread handlerThread = new Thread(new PongHandler());
        handlerThread.start();
        connectToServer();
        while (cC.sP.getGameState() == GameState.BOTH_NOT_CONNECTED) {
            PongHandler.pong.setGameState();
            if (i == 0) {
                System.out.println("CLIENT: Waiting on other player to connect...");
                i++;
            }
        }
        updateGame();
    }

    //creates a new object of ClientConnection at the given hostName, portNumber
    private void connectToServer() {
        cC = new ClientConnection(hN, pN);
    }
}