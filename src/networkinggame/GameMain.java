package networkinggame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

   
public class GameMain extends JPanel {    // main class for the game
   
    // Define constants for the game
    static final String TITLE = "Fighter Flight";
    static final int CANVAS_WIDTH = 800;    // width and height of the game screen
    static final int CANVAS_HEIGHT = 600;
    static final int PLANE_SPEED = 7;
    private Timer timer;
    private List<Player> players;
    private int numOfPlayers;
    private int playerNum;
    private Client networkHandler;

    // ......

    // Enumeration for the states of the game.
    static enum GameState {
       INITIALIZED, PLAYING, PAUSED, GAMEOVER, DESTROYED
    }
    static GameState state;   // current state of the game

    // Define instance variables for the game objects
    // ......
    // ......

    // Handle for the custom drawing panel
    private GameCanvas canvas;

    // Constructor to initialize the UI components and game objects
    public GameMain(Client client) {
       // Initialize the game objects
       gameInit();
       this.networkHandler = client;
       // UI components
       canvas = new GameCanvas();
       canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
       add(canvas);
       
       // Other UI components such as button, score board, if any.
       // ......
       // Start the game.
    }

    // All the game related codes here

    // Initialize all the game objects, run only once in the constructor of the main class.
    public void gameInit() {
        state = GameState.INITIALIZED;
        timer = new Timer();
        players = new ArrayList<>();
    }

    // Shutdown the game, clean up code that runs only once.
    public void gameShutdown() {
        state = GameState.GAMEOVER;
    }

    // To start and re-start the game.
    public void gameStart() { 
        state = GameState.PLAYING;
        // init postion for each plane of 2 team
        int distance = (int)(CANVAS_WIDTH/Math.ceil((float)numOfPlayers/2));  
        int x_init1 = -distance/2;
        int x_init2 = -distance/2;
        int y_init1, y_init2;
        if (playerNum % 2 == 0) {
            y_init1 = 0;
            y_init2 = CANVAS_HEIGHT-46;
        }
        else {
            y_init1 = CANVAS_HEIGHT-46;
            y_init2 = 0;
        }
        for (int i = 0; i < numOfPlayers; i++) {
            if (i % 2 == 0) {
                x_init1 += distance;
                players.add(new Player("/assets/Plane"+Integer.toString(i+1)+".png", 
                        x_init1, y_init2, (i%2 == playerNum%2)));
            }
            else {
                x_init2 += distance;
                players.add(new Player("/assets/Plane"+Integer.toString(i+1)+".png", 
                        x_init2, y_init1, (i%2 == playerNum%2)));
            }
        }
        timer.scheduleAtFixedRate(new ScheduleTask(), 1000, 33);
    }

    private class ScheduleTask extends TimerTask {

        @Override
        public void run() {
            // Game loop
        //    while (state != GameState.GAMEOVER) {
               if (state == GameState.PLAYING) {   // not paused
                  // Update the state and position of all the game objects,
                  // detect collisions and provide responses.
                  gameUpdate();
        //       }
               // Refresh the display
               repaint();
            }
        }
    }

    // Update the state and position of all the game objects,
    // detect collisions and provide responses.
    public void gameUpdate() {
        for (int i = 0; i < numOfPlayers; i ++) {
            List<Bullet> bullets = players.get(i).getBullets();
            int bulletsSize = bullets.size();
            for (int j = 0; j < bulletsSize; j++) {
                bullets.get(j).move();
            }
        }
        players.get(playerNum).getPlane().move();
        networkHandler.sendData(updateStateOnNetwork(1, playerNum));
        checkCollision();
    }
    
    private void stopGame() {
        state = GameState.GAMEOVER;
        timer.cancel();
    }
    
    private void checkCollision() {
        boolean team1 = false;
        boolean team2 = false;
        for (int i = 0; i < numOfPlayers; i++) {
            if (i%2 == 0 && players.get(i).isAlive()) team1 = true; 
            else if (i%2 == 1 && players.get(i).isAlive()) team2 = true;
        }
        
        if ((team1 && !team2) || (!team1 && team2)) {
            stopGame();
        } 

        // Collision of Bullet with Plane
        
        for (int i = 0; i < numOfPlayers; i++) {
            if((i % 2) != (playerNum % 2)) {
                for(int k = 0 ; k < players.get(playerNum).getBullets().size(); k++){
                    if(players.get(i).isAlive()) {
                        if (Physics.testIntersection(players.get(playerNum).getBullets().get(k).getCircle(), 
                                players.get(i).getPlane().getRect())){
                            players.get(i).decreaseLives();
                            players.get(playerNum).getBullets().remove(k);
                            networkHandler.sendData(updateStateOnNetwork(3, i)); // send lives of enemy
                            networkHandler.sendData(updateStateOnNetwork(4, k)); // send bullet disappear
                        }
                    }
                }
            }
        }
    }

    // Refresh the display. Called back via repaint(), which invoke the paintComponent().
    private void gameDraw(Graphics2D g2d, ImageObserver observer) {
        switch (state) {
            case INITIALIZED:
                // ......
                break;
            case PLAYING:
                for (int i = 0; i < players.size(); i ++) {
                    List<Bullet> bullets = players.get(i).getBullets();
                    int bulletsSize = bullets.size();
                    for (int j = 0; j < bulletsSize; j++) {
                        bullets.get(j).draw(g2d, observer);
                    }
                }
                for (int i = 0; i < players.size(); i++) {
                    players.get(i).getPlane().draw(g2d, observer);
                }
                break;
            case PAUSED:
                // ......
                break;
            case GAMEOVER:
                gameFinished(g2d);
                break;
        }
        // ...... 
    }
    
    private void gameFinished(Graphics2D g2d) {
        Font font = new Font("Verdana", Font.BOLD, 18);
        FontMetrics metr = this.getFontMetrics(font);
        g2d.setColor(Color.BLACK);
        g2d.setFont(font);
        String message = "LOSE";
        for (int i = 0; i < numOfPlayers; i++) {
            if (((i % 2) == (playerNum % 2)) && players.get(i).isAlive()) {
                message = "WIN";
            }
        }

        g2d.drawString(message,
                        (CANVAS_WIDTH - metr.stringWidth(message)) / 2,
                        CANVAS_WIDTH / 2);
    }

    // Process a key-pressed event. Update the current state.
    public void gameKeyPressed(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_UP:
                players.get(playerNum).getPlane().setDy(-PLANE_SPEED);
                break;
            case KeyEvent.VK_DOWN:
                players.get(playerNum).getPlane().setDy(PLANE_SPEED);
                break;
            case KeyEvent.VK_LEFT:
                players.get(playerNum).getPlane().setDx(-PLANE_SPEED);
                break;
            case KeyEvent.VK_RIGHT:
                players.get(playerNum).getPlane().setDx(PLANE_SPEED);
                break;
            case KeyEvent.VK_SPACE:
                players.get(playerNum).addBullet();
                networkHandler.sendData(updateStateOnNetwork(2, playerNum));
                break;
        }
    }
    
    public void gameKeyReleased(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_UP:
                players.get(playerNum).getPlane().setDy(0);
                break;
            case KeyEvent.VK_DOWN:
                players.get(playerNum).getPlane().setDy(0);
                break;
            case KeyEvent.VK_LEFT:
                players.get(playerNum).getPlane().setDx(0);
                break;
            case KeyEvent.VK_RIGHT:
                players.get(playerNum).getPlane().setDx(0);
                break;
        }
    }
    // Other methods
    // ......

    // Custom drawing panel, written as an inner class.
    class GameCanvas extends JPanel implements KeyListener {
        // Constructor
        public GameCanvas() {
           setFocusable(true);  // so that can receive key-events
           requestFocus();
           addKeyListener(this);
        }

        // Override paintComponent to do custom drawing.
        // Called back by repaint().
        @Override
        public void paintComponent(Graphics g) {
           Graphics2D g2d = (Graphics2D)g;
           super.paintComponent(g2d);   // paint background
     //      setBackground(Color.BLACK);  // may use an image for background

           // Draw the game objects
           gameDraw(g2d, this);
        }

        // KeyEvent handlers
        @Override
        public void keyPressed(KeyEvent e) {
           gameKeyPressed(e.getKeyCode());
        }

        @Override
        public void keyReleased(KeyEvent e) {
            gameKeyReleased(e.getKeyCode());
        }

        @Override
        public void keyTyped(KeyEvent e) { }
    }
    
    public void updateStateFromNetwork(String inputString) {
        String data[] = inputString.trim().split(",");
        String opCode = data[0];
        if (opCode.equals("a")) { // 0 opCode
            int playerNumber = Integer.parseInt(data[1].trim());    		
            for (int i=0; i<numOfPlayers; i++) {
                if (i == playerNumber) { // data[1] playerNum on network 					
                    int x = Integer.valueOf(data[2].trim()); // data[2,3] airplane position
                    int y = Integer.valueOf(data[3].trim()); 
                    players.get(i).setPlanePosition(x, y);
                }
            }
    	}
        else if (opCode.equals("b")) {
            int playerNumber = Integer.parseInt(data[1].trim());
            for (int i = 0; i < numOfPlayers; i++) {
                if (i == playerNumber) {
                    int x = Integer.valueOf(data[2].trim()); // data[2,3] airplane position
                    int y = Integer.valueOf(data[3].trim());
                    int y_dir = Integer.valueOf(data[4].trim());
                    Player player = players.get(i);
                    player.getBullets().add(new Bullet("/assets/bullet.png", x, y));
                    player.setBulletPosition(player.getBullets().size()-1, x, y, 0, y_dir);
                }
            }
        }
        else if (opCode.equals("c")) {
            int playerNumber = Integer.parseInt(data[1].trim());
            for (int i = 0; i < numOfPlayers; i++) {
                if (i == playerNumber) {
                    int lives = Integer.valueOf(data[2].trim());
                    players.get(i).setLives(lives);
                }
            }
        }
        else if (opCode.equals("d")) {
            int playerNumber = Integer.parseInt(data[1].trim());
            for (int i = 0; i < numOfPlayers; i++) {
                if (i == playerNumber) {
                    int index = Integer.valueOf(data[2].trim()); // data[2,3] airplane position
                    players.get(i).getBullets().remove(index);
                }
            }
        }
        else if (opCode.equals("1")) {
            numOfPlayers = Integer.valueOf(data[1].trim());
            playerNum = Integer.valueOf(data[2].trim());
            gameStart();
        }
        
    }
    
    public String updateStateOnNetwork(int packetType, int index) {
        
        String data = "";
    	// a op code - networkPlayerNum - networkPacket
    	if (packetType == 1){    	
            data += "a,";
            data += Integer.toString(playerNum).concat(",");
        //    data += Integer.toString(players.get(playerNum).getPacketNumber()).concat(",");
        //    players.get(playerNum).setPacketNumber(players.get(playerNum).getPacketNumber() + 1);
            data += Integer.toString(players.get(playerNum).getPlane().getX()).concat(",");
            data += Integer.toString(players.get(playerNum).getPlane().getY());
    	} 
        else if (packetType == 2) {
            data += "b,";
            data += Integer.toString(playerNum).concat(",");
            data += Integer.toString(players.get(playerNum).getLastBullet().getX()).concat(",");
            data += Integer.toString(players.get(playerNum).getLastBullet().getY()).concat(",");
            data += Integer.toString(players.get(playerNum).getLastBullet().getY_dir());
        }
        else if (packetType == 3) {	
            data += "c,";
            data += Integer.toString(playerNum).concat(",");
            data += Integer.toString(players.get(playerNum).getLives());
    	}
        else if (packetType == 4) {
            data += "d,";
            data += Integer.toString(playerNum).concat(",");
            data += Integer.toString(index);
    	}
        return data;
    }

}