//HostOrJoin.java
//Enables a user to choose between hosting their own server or joining an existing one
package Pong;

import javax.swing.*;

public class HostOrJoin {

    private boolean resolved;

    //Constructor
    HostOrJoin(){     //Initializes new JFrame for use in prompting for user input
        resolved = false;                               //Initializes resolved to false, boolean implemented later
    }

    //Prompts whether a user would like to 'host' or 'join' a server
    public void setUp(){
        Object[] options = {"Host", "Join"};
        int n = JOptionPane.showOptionDialog(null,
                "Would you like to host or join a server?",
                "Pong",JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);
                if (n == JOptionPane.YES_OPTION)
                    host();
                else if (n == JOptionPane.NO_OPTION){
                    Join newJoin = new Join();
                    newJoin.join();
                }
                else
                    System.exit(0);
    }

    //Upon 'host' being chosen, user is prompted to enter the preferred port number to host the server on
    private void host(){
        String input = JOptionPane.showInputDialog(null,
                "Enter preferred port number:",
                "Pong",JOptionPane.PLAIN_MESSAGE);
        if (input == null)
            System.exit(0);
        String regex = "\\d+";
        //bulletproof loop to ensure entry consists of numbers
        while (!input.matches(regex)) {
            JOptionPane.showMessageDialog(null,
                    "Please enter a valid port number",
                    "Pong",
                    JOptionPane.PLAIN_MESSAGE);
            input = JOptionPane.showInputDialog(null,
                    "Enter preferred port number:",
                    "Pong",
                    JOptionPane.PLAIN_MESSAGE);
            if (input == null)
                System.exit(0);
        }
        Thread serverThread = new Thread(new PongServer(input)); //starts a new thread to handle server implementation
        serverThread.start();
        String[] args = new String[2];
        args[0] = "127.0.0.1";
        args[1] = input;
        //'resolved' boolean implemented below to allow the user to re-enter their option
        // upon deciding to cancel the action of closing the server
        while(!resolved) {
            int reply = JOptionPane.showConfirmDialog(null,
                "Would you like to join this server?",
                "Pong",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null);
            if (reply == JOptionPane.YES_OPTION) {
                Thread clientThread = new Thread(new PongClient(args)); //starts a new thread to handle client implementation
                clientThread.start();
                resolved = true;
            } else if (reply == JOptionPane.NO_OPTION) {
                JOptionPane.showMessageDialog(null,
                        "This server will run until the JVM is terminated.",
                        "WARNING",
                        JOptionPane.PLAIN_MESSAGE);
                resolved = true;
            } else {
                int m = JOptionPane.showConfirmDialog(null,
                        "Doing this will close the server. Do you wish to exit?",
                        "WARNING",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.PLAIN_MESSAGE,
                        null);
                if (m == JOptionPane.NO_OPTION) {
                    resolved = false;
                }
                else {
                    System.exit(0);
                }
            }
        }
    }
}