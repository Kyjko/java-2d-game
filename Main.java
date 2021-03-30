/***
 * @author Bognár Miklós
 ***/

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.LinkedList;


public class Main extends Canvas implements Runnable {

    Thread mainThread;
    boolean running = false;
    LinkedList<Entity> entities;
    Guy player;
    public static int START_X = 300, START_Y = 300;
    public static int WIDTH = 1920, HEIGHT = 1080;

    public static float moveSpeed = 0.125f; //default move speed for player

    public static float gr = 0.005f;

    static double FPS;
    static float mappedFPS;
    JFrame f;
    Map map;
    HUD hud;
    Inventory inventory;

    //camera coordinates
    public static float camX = 0, camY = 0;

    public Main(){
        f = new JFrame("나의 아드밴쳐 ");
        //set WIDTH and HEIGHT based on system properties
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        WIDTH = dim.width;
        HEIGHT = dim.height;
        f.setSize(WIDTH, HEIGHT);
        f.setVisible(true);
        f.setResizable(false);
        f.setLocationRelativeTo(null);
        f.setFocusable(true);
        f.requestFocus();
        f.add(this);
        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                int c = e.getKeyCode();

                if (c == KeyEvent.VK_A) {
                    player.velX = -moveSpeed;
                }
                if (c == KeyEvent.VK_D) {
                    player.velX = moveSpeed;
                }
                if (c == KeyEvent.VK_W && !player.isJumping) {
                    player.velY = -moveSpeed;
                }
                if (c == KeyEvent.VK_S && !player.isJumping) {
                    player.velY = moveSpeed;
                }
                if(c == KeyEvent.VK_SPACE) {
                    player.jump();
                }
                if(c == KeyEvent.VK_E){
                    inventory.isVisible = inventory.isVisible ? false : true;
                }
                if(c == KeyEvent.VK_R) {
                    player.resetPos();
                }
                //handle escape key
                if(c == KeyEvent.VK_ESCAPE) {
                    System.out.println("Exiting...");
                    stop();

                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int c = e.getKeyCode();

                if (c == KeyEvent.VK_A) {
                    player.velX = 0;
                }
                if (c == KeyEvent.VK_D) {
                    player.velX = 0;
                }
                if (c == KeyEvent.VK_W) {
                    player.velY = 0;
                }
                if (c == KeyEvent.VK_S) {
                    player.velY = 0;
                }
            }
        });

        entities = new LinkedList<Entity>();
        //add dummy entity
        player = new Guy(START_X, START_Y, "You", Type.Player);
        entities.add(player);

        //init map
        map = new Map(this, 50);
        inventory = new Inventory(WIDTH*3/4, HEIGHT/2, WIDTH/4, HEIGHT/2);
        System.out.println("<Map initialized>");

        //init hud
        hud = new HUD(WIDTH*3/4, 0, WIDTH/4, 140);

        start();
    }

    public synchronized void start(){
        System.out.println("Starting...");
        mainThread = new Thread(this);
        System.out.println("Starting thread " + mainThread.getName() + "...");
        mainThread.start();
        running = true;
    }

    public synchronized void stop(){
        try {
            mainThread.interrupt();
            System.out.println("Stopping thread " + mainThread.getName() + "...");
            System.exit(-1);

        } catch(Exception ex){System.err.println("Error while stopping thread " + mainThread.getName() + ":\n"); ex.printStackTrace();}
    }

    long lastTime = System.nanoTime();

    public void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();
        Graphics2D g2d = (Graphics2D) g;

        //get mouse position
        float mx = MouseInfo.getPointerInfo().getLocation().x;
        float my = MouseInfo.getPointerInfo().getLocation().y;

        if(mx >= inventory.x && my >= inventory.y) {
            inventory.isMouseIn = true;
        } else {
            inventory.isMouseIn = false;
        }

        //fps counter
        FPS = -(1000000.0 / (lastTime - (lastTime = System.nanoTime())));

        //dynamically set moveSpeed depending on FPS so game wont behave strangely, maybe
        mappedFPS = map((float)FPS, 0, 6, 0, 1);
        //System.out.println(mappedFPS);
        moveSpeed = 0.075f/mappedFPS;
        //dynamically set gr
        gr = 0.0025f/mappedFPS;

        //camera logic (probably the spaghettiest ever)
        if(player.x >= WIDTH/2 && player.y >= HEIGHT/2 && player.x <= 5/2*WIDTH && player.y < 5/2*HEIGHT) {
            camX = -player.x + WIDTH / 2;
            camY = -player.y + HEIGHT / 2;
        } else if(player.x < WIDTH/2 && player.y >= HEIGHT/2 && player.y < 5/2*HEIGHT){
            camX = 0;
            camY = -player.y + HEIGHT/2;
        } else if(player.x > 5/2*WIDTH && player.y >= HEIGHT/2 && player.y < 5/2*HEIGHT) {
            camX = -5/2*WIDTH;
            camY = -player.y + HEIGHT/2;
        } else if(player.x < WIDTH/2 && player.y < HEIGHT/2) {
            camX = 0;
            camY = 0;
        } else if(player.x < WIDTH/2 && player.y > 5/2*HEIGHT) {
            camX = 0;
            camY = -5/2*HEIGHT;
        } else if(player.x > 5/2*WIDTH && player.y > 5/2*HEIGHT) {
            camX = -5/2*WIDTH;
            camY = -5/2*HEIGHT;
        } else if(player.x > 5/2*WIDTH && player.y < HEIGHT/2) {
            camX = -5/2*WIDTH;
            camY = 0;
        }
        //!camera logic

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        //camera
        g2d.translate(camX, camY);

        //render Map
        map.render(g);

        //render entities
        for(int i = 0; i < entities.size(); i++){
            Entity e = entities.get(i);
            e.render(g);
        }

        g2d.translate(-camX, -camY);
        //!camera

        //render hud
        hud.render(g);
        inventory.render(g);

        g.dispose();
        bs.show();
    }

    public void run(){
        while(running) {
            render();

        }

        System.out.println("Thread " + mainThread.getName() + "Successfully stopped");
    }

    //mapping function cause java doesnt have one apparently
    float map(float s, float a1, float a2, float b1, float b2) {
        return b1 + (s-a1)*(b2-b1)/(a2-a1);
    }

    public static void main(String[] args) {
        System.setProperty("sun.java2d.opengl", "true");
        System.out.println("OpenGL hardware acceleration enabled");
        new Main();
    }


}
