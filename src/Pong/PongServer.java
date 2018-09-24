//PongServer.java
//Manages all server-side functions
package Pong;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class PongServer implements Runnable {

    private int numPlayers; //number of players
    private GameState gameState;

    private int paddle1Y; //y location of the paddle of player 1
    private int paddle2Y; //y location of the paddle of player 2

    // Named-constants for the dimensions
    private static final int HEIGHT = 500,
                             WIDTH = 700;

    private ServerBall serverBall;
    private ServerSocket serverSocket;

    //Constructor creates a new ServerBall, initializes the gameState and numPlayers and opens a new serverSocket
    PongServer(String portNumber) {
        int lastPoint = 0;  //stores value for who scored last, initialized to 0 as neither player has scored
        serverBall = new ServerBall(lastPoint, WIDTH, HEIGHT);
        gameState = GameState.BOTH_NOT_CONNECTED;
        int pN = Integer.parseInt(portNumber);
        numPlayers = 0;
        try {
            serverSocket = new ServerSocket(pN); //serverSocket opened at portNumber pN
        } catch (IOException e) { //catches when portNumber could not be opened
            System.err.println("ERROR: Could not listen on port " + pN);
            System.exit(-1);
        }
    }


    @Override
    public void run() {
        acceptConnections();
        handleBall();
    }

    //loops until 2 players have connected
    private void acceptConnections() {
        System.out.println("SERVER: Server initiated. Awaiting connections...");
        while (numPlayers < 2) {
            Socket socket = null; //initializes socket
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
            Thread t = new Thread(sC); //starts a new thread for each client connected
            t.start();
        }
        System.out.println("SERVER: Both players have connected. Server will no longer accept new connections.");
        gameState = GameState.BOTH_CONNECTED; //changes gameState once both have connected
    }


    //manages ball functions in the server so that both clients see the same ball
    private void handleBall() {
        Thread ballThread = new Thread(() -> {
        while (true) {
            if (gameState == GameState.BOTH_CONNECTED) {
                serverBall.checkCollision(paddle1Y, paddle2Y);
                serverBall.move();
                try {
                    Thread.sleep(5); //without a delay, the ball is updated too fast
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        });
        ballThread.start();
    }

    //serverConnection handles each new client that is connected
    private class serverConnection implements Runnable {

        private Socket socket;
        private DataInputStream dataIn; //input
        private ObjectOutputStream objOut;
        ServerPacket sP;
        private int playerID,
                    paddleHeight = 80;

        //Constructor receives which socket it uses and the player ID associated with that socket
        serverConnection(Socket s, int id) {
            socket = s;
            playerID = id;
            paddle1Y = HEIGHT/2 - paddleHeight/2; //Paddles initialized to the vertical center
            paddle2Y = HEIGHT/2 - paddleHeight/2;
            try {
                //input and output streams for sending and receiving
                dataIn = new DataInputStream(socket.getInputStream());
                objOut = new ObjectOutputStream(socket.getOutputStream());
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("IOException from serverConnection constructor.");
            }
            sendPacket();
        }

        //reads data from the input stream and assigns the value to the correct paddle respective to the player ID
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


        //Sends a packet with the relevant game data
        private void sendPacket(){
            Thread sendThread = new Thread(() -> {
                while(true) {
                    //System.out.println(serverBall.getY());
                    //System.out.println(serverBall.getX());
                    try {
                        //changes what paddle location gets sent respective to player ID
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