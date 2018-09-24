//Join.java
//Creates and manages a frame for players to join a server
package Pong;

import javax.swing.*;

public class Join {

    private JFrame joinFrame = new JFrame("Pong");
    private String regex = "\\d+"; //used to check string (ln28)

    //various elements of the swing GUI
    public JPanel joinPanel;
    public JButton enterButton;
    private JLabel portNumberLabel;
    private JLabel hostNameLabel;
    private JTextField inputHostName;
    private JTextField inputPortNumber;


    //constructor creates the frame, prompts for a hostname and port number
    public Join() {
        enterButton.addActionListener(e -> {
            String[] args = new String[2];
            String hN = inputHostName.getText(); //hostname
            String pN = inputPortNumber.getText(); //portnumber
            //bulletproof loop to ensure only numbers are entered.
            while (!pN.matches(regex)) {
                JOptionPane.showMessageDialog(null, "Please enter a valid port number");
                pN = JOptionPane.showInputDialog("Enter port number:");
            }
            args[0] = hN;
            args[1] = pN;
            Thread clientThread = new Thread(new PongClient(args));
            clientThread.start();
            joinFrame.dispose();
        });
    }

    void join() {
        joinFrame.setContentPane(joinPanel);
        joinFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        joinFrame.pack();
        joinFrame.setLocationRelativeTo(null);
        joinFrame.setVisible(true);
    }
}