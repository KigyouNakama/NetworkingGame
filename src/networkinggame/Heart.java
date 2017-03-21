/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkinggame;

/**
 *
 * @author mylaptop
 */

public class Heart extends Sprite {
    
    private int colour;

    public Heart(int initX, int initY, int colour) {
        setImg("/assets/Life" + Integer.toString(colour) + ".png");
        setX(initX);
        setY(initY);
    }
    
}