/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkinggame;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.image.ImageObserver;
import javax.swing.ImageIcon;

/**
 *
 * @author linhsama
 */
public class Sprite {
    private int x;
    private int y;
    private int dx;
    private int dy;
    private int x_dir;
    private int y_dir;
    private int i_width;
    private int i_height;
    private Image img;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDx() {
        return dx;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public int getDy() {
        return dy;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }
    
    public int getX_dir() {
        return x_dir;
    }

    public void setX_dir(int x_dir) {
        this.x_dir = x_dir;
    }

    public int getY_dir() {
        return y_dir;
    }

    public void setY_dir(int y_dir) {
        this.y_dir = y_dir;
    }

    public int getWidth() {
        return i_width;
    }

    public void setWidth(int i_width) {
        this.i_width = i_width;
    }

    public int getHeight() {
        return i_height;
    }

    public void setHeight(int i_height) {
        this.i_height = i_height;
    }

    public Image getImg() {
        return img;
    }

    public void setImg(String path) {
        ImageIcon icon = new ImageIcon(getClass().getResource(path));
        this.img = icon.getImage();
        setWidth(img.getWidth(null));
        setHeight(img.getHeight(null));
    }
    
    public void draw(Graphics2D g2d, ImageObserver observer) {
        g2d.drawImage(getImg(), getX(), getY(), getWidth(), getHeight(), observer);
    }
    
    public Rectangle getRect() {
        return new Rectangle(x, y, getWidth(), getHeight());        
    }

    public Ellipse2D getCircle() {
        return new Ellipse2D.Float(x, y, getWidth(), getHeight());
    }
}
