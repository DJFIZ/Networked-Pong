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

    ClientConnection(String hostName, int portNumber) {
        Socket clientSocket = null;
        sP = new ServerPacket(0,0,0, 210, GameState.BOTH_NOT_CONNECTED);
        System.out.println("CLIENT: Client initiated.");
        try {
            clientSocket = new Socket(hostName, portNumber);
        } catch (UnknownHostException e) {
            JOptionPane.showMessageDialog(null,"Don't know about host: " + hostName);
            System.exit(1);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Couldn't get I/O for the connection to: " + portNumber);
            System.exit(1);
        }
        try {
            dataOut = new DataOutputStream(clientSocket.getOutputStream());
            objIn = new ObjectInputStream(clientSocket.getInputStream());
        }catch (IOException ex) {
            ex.printStackTrace();
        }
        receivePacket();
    }

    void sendMovement(int n){
        try {
            dataOut.writeInt(n);
            dataOut.flush();
        } catch (IOException ex){
            ex.printStackTrace();
            System.out.println("IOException from sendMovement() PongClient");
        }
    }

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