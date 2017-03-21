/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkinggame;

import javax.swing.ImageIcon;

/**
 *
 * @author linhsama
 */
public class Bullet extends Sprite {
    
    public static final int BULLET_SPEED = 4;
    
    public Bullet(String imgPath, int x, int y) {
        setX(x);
        setY(y);
        setImg(imgPath);
    }
    
    // Called at every Timestep
    public void move() {
        setY(getY()+BULLET_SPEED*getY_dir());
    }
}
