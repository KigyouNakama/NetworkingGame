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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author linhsama
 */
public class NetworkManager {
    
    private final int CANVAS_WIDTH = 800; 
    private int gate = 1234;
    private Socket client;
    private ServerSocket server;
    private int numOfPlayers;
    private List<PrintWriter> clientList;
    
    /**
     * initialize server
     * @param numOfPlayers set number of players
     * @throws IOException 
     */
    public NetworkManager(int numOfPlayers) {
        try {
            clientList = new ArrayList<>(); // init clientList
            server = new ServerSocket(gate);
            this.numOfPlayers = numOfPlayers;
            waitingForClient();
        } catch (IOException ex) {
            Logger.getLogger(NetworkManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void waitingForClient() throws IOException {
        while (true) {
            client = server.accept();
            System.out.println("connect from " + client.getRemoteSocketAddress());
            clientList.add(new PrintWriter(client.getOutputStream(), true));
            Thread clientHandler = new Thread(new ClientHandler(client, clientList.size()-1));
            clientHandler.start();
        }
    }
    
    /**
     * handler each client in each thread
     */
    public class ClientHandler implements Runnable {

        private int playerNum;
        private BufferedReader in;
        private PrintWriter out;
        private String receivedData;
        
        public ClientHandler(Socket client, int playerNum) throws IOException {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);
            this.playerNum = playerNum;
        }
        
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
        
        public void handleData(String receiveData) {
            String data[] = receiveData.trim().split(",");
            // check if enough players
            if (data[0].equals("1")) {
                // enough players
                if (clientList.size() == numOfPlayers) {
                    String sentData = "";
                    sentData += "1,"; // start game signal 
                    sentData += Integer.toString(numOfPlayers).concat(",");
                    sendToAll(sentData); // ready for battle
                }
                else if (clientList.size() < numOfPlayers) {
                    
                }
                else {
                    
                }
            }
            else {
                sendToOther(receiveData);
            }
        }
        
        public void sendToOther(String data) {
            for (int i = 0; i < clientList.size(); i++) {
                if (i != playerNum) 
                    clientList.get(i).println(data);            
            }
        }
        
        public void sendToAll(String data) {
            for (int i = 0; i < clientList.size(); i++) {
                clientList.get(i).println(data+i);            
            }
        }
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
            NetworkManager server = new NetworkManager(1);
            
        
    }
    
}
