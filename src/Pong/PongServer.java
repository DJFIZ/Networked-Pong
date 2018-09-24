//PongServer.java
//Manages
package Pong;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class PongServer implements Runnable {

    private int numPlayers;
    private GameState gameState;

    private int paddle1Y;
    private int paddle2Y;

    // Named-constants for the dimensions
    private static final int HEIGHT = 500,
                             WIDTH = 700;

    private ServerBall serverBall;
    private ServerSocket serverSocket;


    PongServer(String portNumber) {
        //stores value for who scored last, initialized to 0 as neither player has scored
        int lastPoint = 0;
        serverBall = new ServerBall(lastPoint, WIDTH, HEIGHT);
        gameState = GameState.BOTH_NOT_CONNECTED;
        int pN = Integer.parseInt(portNumber);
        numPlayers = 0;
        try {
            serverSocket = new ServerSocket(pN);
        } catch (IOException e) {
            System.err.println("ERROR: Could not listen on port " + pN);
            System.exit(-1);
        }
    }


    @Override
    public void run() {
        acceptConnections();
        handleBall();
    }

    private void acceptConnections() {
        System.out.println("SERVER: Server initiated. Awaiting connections...");
        while (numPlayers < 2) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            numPlayers++;
            System.out.println("SERVER: Player #" + numPlayers + " has connected.");
            serverConnection sC = new serverConnection(socket, numPlayers);
            if (numPlayers == 1) {
                System.out.println("SERVER: Awaiting connection from Player #2.");
            }
            Thread t = new Thread(sC);
            t.start();
        }
        System.out.println("SERVER: Both players have connected. Server will no longer accept new connections.");
        gameState = GameState.BOTH_CONNECTED;
    }


    private void handleBall() {
        System.out.println("WERE HANDLING");
        Thread ballThread = new Thread(() -> {
        while (true) {
            if (gameState == GameState.BOTH_CONNECTED) {
                serverBall.checkCollision(paddle1Y, paddle2Y);
                serverBall.move();
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        });
        ballThread.start();
    }


    private class serverConnection implements Runnable {

        private Socket socket;
        private DataInputStream dataIn;
        private ObjectOutputStream objOut;
        ServerPacket sP;
        private int playerID;
        private int paddleHeight = 80;

        serverConnection(Socket s, int id) {
            socket = s;
            playerID = id;
            paddle1Y = HEIGHT/2 - paddleHeight/2;
            paddle2Y = HEIGHT/2 - paddleHeight/2;
            try {
                dataIn = new DataInputStream(socket.getInputStream());
                objOut = new ObjectOutputStream(socket.getOutputStream());
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("IOException from serverConnection constructor.");
            }
            sendPacket();
        }


        @Override
        public void run() {
            try {
                while (true) {
                    //if (dataIn.readInt() == 1000 && gameState == GameState.BOTH_CONNECTED) {
                    //    System.out.println("WERE PLAYING");
                    //    gameState = GameState.PLAYING;
                    //}if (playerID == lastPoint && gameState == GameState.BOTH_CONNECTED && dataIn.readInt() == 1000) {
                    //    gameState = GameState.PLAYING;
                    //}
                    if (playerID == 1) {
                        paddle1Y = dataIn.readInt();
                    } else if (playerID == 2) {
                        paddle2Y = dataIn.readInt();
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("IOException from run() sC");
            }
        }


        private void sendPacket(){
            Thread sendThread = new Thread(() -> {
                while(true) {
                    //System.out.println(serverBall.getY());
                    //System.out.println(serverBall.getX());
                    try {
                        if(playerID == 1)
                            sP = new ServerPacket(playerID, serverBall.getX(), serverBall.getY(), paddle2Y, gameState);
                        else
                            sP = new ServerPacket(playerID, serverBall.getX(), serverBall.getY(), paddle1Y, gameState);
                        objOut.writeObject(sP);
                        objOut.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            sendThread.start();
        }
    }
}