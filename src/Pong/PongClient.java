//PongClient.java
//Basic client
package Pong;

import java.awt.*;

public class PongClient extends Component implements Runnable {

    private int pN;

    private String hN;

    static ClientConnection cC;


    PongClient(String[] args){
        this.hN = args[0];
        this.pN = Integer.parseInt(args[1]);
    }

    private void updateGame(){
            PongHandler.pong.setVisible();
            while(true) {
                PongHandler.pong.setGameState();
                PongHandler.pong.updateOpponentPaddle(cC.sP.getOpponentPaddle(), cC);
                PongHandler.pong.updateBall(cC.sP.getBallX(), cC.sP.getBallY());
            }
    }


    @Override
    public void run() {
        int i =0;
        Thread handlerThread = new Thread(new PongHandler());
        handlerThread.start();
        connectToServer();
        while(cC.sP.getGameState() == GameState.BOTH_NOT_CONNECTED){
            PongHandler.pong.setGameState();
            if(i == 0) {
                System.out.println("CLIENT: Waiting on other player to connect...");
                i++;
            }
        }
        updateGame();
    }

    private void connectToServer(){
        cC = new ClientConnection(hN, pN);
    }
}