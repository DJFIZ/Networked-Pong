//ClientConnection.java
//handles the necessary client-side network functions for pong
package Pong;

import javax.swing.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;

class ClientConnection {

    private DataOutputStream dataOut;
    private ObjectInputStream objIn;
    ServerPacket sP;

    //constructor initializes the socket at the given hostName and portNumber
    ClientConnection(String hostName, int portNumber) {
        Socket clientSocket = null;
        sP = new ServerPacket(0, 0, 0, 210, GameState.BOTH_NOT_CONNECTED); //creates a 'default' server packet
        System.out.println("CLIENT: Client initiated.");
        try {
            clientSocket = new Socket(hostName, portNumber); //creates socket
        } catch (UnknownHostException e) { //catches error when host cannot be reached at hostName
            JOptionPane.showMessageDialog(null, "Don't know about host: " + hostName);
            System.exit(1);
        } catch (IOException e) { //catches error when host can't be reached at portNumber
            JOptionPane.showMessageDialog(null, "Couldn't get I/O for the connection to: " + portNumber);
            System.exit(1);
        }
        try {
            dataOut = new DataOutputStream(clientSocket.getOutputStream()); //Output stream to send paddle position to server
            objIn = new ObjectInputStream(clientSocket.getInputStream()); //Input stream to receive server packets
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        receivePacket();
    }

    //sends paddle position to server
    void sendMovement(int n) {
        try {
            dataOut.writeInt(n);
            dataOut.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("IOException from sendMovement() PongClient");
        }
    }


    //Constantly receives packets from server, overwriting sP
    private void receivePacket() {
        Thread receiveThread = new Thread(() -> {
            while (true) {
                try {
                    sP = (ServerPacket) objIn.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        receiveThread.start();
    }
}