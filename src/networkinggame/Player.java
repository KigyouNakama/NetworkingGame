/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkinggame;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author linhsama
 */
public class Player {
    
    public static final int LIVES = 8;
    private Plane plane;
    private Heart heart;
    private int lives;
    private List<Bullet> bullets;
    private int packetNumber;
    private boolean mikata;
    
    public Player(String imgPath, int x, int y, boolean mikata) {
        packetNumber = 0;
        lives = LIVES;
        plane = new Plane(imgPath, x, y);
        bullets = new ArrayList<>();
        this.mikata = mikata;
    }
    
    public void decreaseLives(){
        lives = lives - 1;
    }
    
    public int getLives() {
        return lives;
    }
    
    public void setLives(int lives) {
        this.lives = lives;
    }
    
    public boolean isAlive() {
        return lives > 0;
    }
    
    public Plane getPlane() {
        return plane;
    }
    
    public int getPacketNumber() {
        return packetNumber;
    }
    
    public void setPacketNumber(int num) {
        packetNumber = num;
    }
    
    public Bullet getLastBullet() {
        return bullets.get(bullets.size()-1);
    }
    
    public List<Bullet> getBullets() {
        return bullets;
    }
    
    public void addBullet() {
        Bullet bullet = new Bullet("/assets/bullet.png", 
                (int)(plane.getX() + plane.getWidth() / 2), plane.getY());
        bullet.setY_dir(-1);
        bullets.add(bullet);
    }
    
    public void setPlanePosition(int x, int y) {
        if (!mikata){
            plane.setX(GameMain.CANVAS_WIDTH - x - plane.getWidth());
            plane.setY(GameMain.CANVAS_HEIGHT - y - plane.getHeight());
        }
        else {
            plane.setX(x);
            plane.setY(y);
        }
    }
    
    public void setBulletPosition(int bulletNumber, int x, int y, int dx, int dy){
        Bullet bullet = bullets.get(bulletNumber);
        if (!mikata){
            bullet.setX(GameMain.CANVAS_WIDTH - x - bullet.getWidth());
            bullet.setY(GameMain.CANVAS_HEIGHT - y - bullet.getHeight());
    //	bullet.setXDir(dy);
            bullet.setY_dir(-dy);
        }			
        else {
            bullet.setX(x);
            bullet.setY(y);
    //	bullet.setXDir(dx);
            bullet.setY_dir(dy);
        }
    }
}
