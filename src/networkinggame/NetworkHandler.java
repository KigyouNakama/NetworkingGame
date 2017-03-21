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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author linhsama
 */
public class NetworkHandler {
    private ServerSocket server;
    private PrintWriter writer;
    private BufferedReader reader;
    private Runnable run;
    
    public NetworkHandler(String role) {
        if (role.equals("server")) {
            try {
                server = new ServerSocket(1234);
                waitingForClient();
            } catch (IOException ex) {
                Logger.getLogger(NetworkHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void initRunable(Socket client) {
        run = new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("from client " + 
                            client.getLocalAddress().getHostAddress());
                    String data;
                    BufferedReader in = new BufferedReader(
                                new InputStreamReader(client.getInputStream()));
                    PrintWriter out = new PrintWriter(
                             client.getOutputStream(), true);

                    while ((data = in.readLine()) != null) {
                    
                    }

                    in.close();
                    out.close();
                    client.close();
                } catch(IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        };
    }
    
    public void waitingForClient() throws IOException {
        while (true) {
            Socket clientSocket = server.accept();
            initRunable(clientSocket);
            Thread t = new Thread(run);
            t.start();
        }
    }
    
    
    public void sendingToServer(String data) {
        
    }
}
