/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkinggame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 * singleton class
 * @author linhsama
 */
public class Client {
    
    private static Client instance;
    private Socket client;
    private int playerNum;
    private BufferedReader in;
    private PrintWriter out;
    private Thread thread;
    private JFrame frame;
    private GameMain game;
    
    public Client(String serverAdress) {
        if (instance == null) {
            try {
                instance = this;
                client = new Socket(serverAdress, 1234);
                System.out.println("connect to " + client.getRemoteSocketAddress());
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                out = new PrintWriter(client.getOutputStream(), true);
                sendData("1");  // notify server
                thread = new Thread(new NetworkHandler());
                thread.start();
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static Client getInstance() {
        return instance;
    }
    
    
    public void sendData(String data) {
        if (!data.isEmpty())
            out.println(data);
    }
    
    /**
     * handle network information transfer in special thread
     */
    public class NetworkHandler implements Runnable {

        private String receivedData;
           
        @Override
        public void run() {
            try {
                while (true) {
                    receivedData = in.readLine();
                    handleData(receivedData);
                }
            } catch (IOException ex) {
                Logger.getLogger(NetworkManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        public void handleData(String inputData) {
            String data[] = inputData.trim().split(",");
            if (data[0].equals("1")) {   // ready for battle
                game = new GameMain(getInstance());
                frame = new JFrame();
                frame.getContentPane().add(game);
                frame.pack();
                frame.setVisible(true);
            }
            game.updateStateFromNetwork(inputData);
        }
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
    //    Client client = new Client();
    }
        
}
