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

/**
 *
 * @author linhsama
 */
public class Multithreading {
    
    private Multithreading multi;

    public Multithreading() {
        if (multi == null) {
            multi = new Multithreading();
        }
    }
    
    
}
