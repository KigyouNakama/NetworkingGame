/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkinggame;

import java.awt.Graphics2D;
import java.awt.Image;
import static networkinggame.GameMain.PLANE_SPEED;

/**
 *
 * @author linhsama
 */
public class Plane extends Sprite {
    
    public Plane(String imgPath, int x, int y) {
        setX(x);
        setY(y);
        setImg(imgPath);
    }
    
    public void move() {
        int x = getX()+getDx();
        int y = getY()+getDy();
        if (x >= 0 && (x + getWidth()) <= GameMain.CANVAS_WIDTH)
            setX(x);
        if (y >= 0 && (y + getHeight()) <= GameMain.CANVAS_HEIGHT)
            setY(y);
    }
    
    
}
