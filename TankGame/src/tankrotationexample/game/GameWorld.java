package tankrotationexample.game;


import tankrotationexample.GameConstants;
import tankrotationexample.Launcher;
import tankrotationexample.Resources.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Objects;
import java.util.List;

/**
 * @author anthony-pc
 */
public class GameWorld extends JPanel implements Runnable {

    private BufferedImage world;
    private Tank t1;
    private Tank t2;
    private final Launcher lf;
    private long tick = 0;
    List<GameObject> gobjs = new ArrayList<>(1000);
    List <Animation> anims = new ArrayList<>();


    /**
     *
     */
    public GameWorld(Launcher lf) {
        this.lf = lf;
    }

    @Override
    public void run() {
        try {
            while (true) {
                this.tick++;
                this.t1.update(this); // update tank
                this.t2.update(this);
                this.anims.forEach(animation -> animation.update());
                this.checkCollision();
                this.repaint();
                // redraw game
                /*
                 * Sleep for 1000/144 ms (~6.9ms). This is done to have our 
                 * loop run at a fixed rate per/sec. 
                */
                Thread.sleep(1000 / 144);
            }
        } catch (InterruptedException ignored) {
            System.out.println(ignored);
        }
    }

    private void checkCollision() {

        for (int i = 0; i < this.gobjs.size(); i++){
            GameObject obj1 = this.gobjs.get(i);
            if(obj1 instanceof Wall || obj1 instanceof BreakableWall || obj1 instanceof Ammo ||obj1 instanceof Raygun ||obj1 instanceof Speed|| obj1 instanceof Bullet){
                continue;
            }
            for(int j=0; j < this.gobjs.size(); j++){
                if(i == j) continue;
                GameObject obj2 = this.gobjs.get(j);

                if(obj1.getHitbox().intersects(obj2.getHitbox())){
                    System.out.println(obj1 + " collided with " + obj2);
                    obj1.collides(obj2);
                    obj2.collides(obj1);
                    if(obj1 instanceof Bullet){
                        gobjs.remove(obj1);
                    }
                    if(obj1 instanceof Bullet){
                        gobjs.remove(obj2);
                    }
                }
            }
        }
    }



    /**
     * Reset game to its initial state.
     */
    public void resetGame() {
        this.tick = 0;
        this.t1.setX(300);
        this.t1.setY(300);
    }

    /**
     * Load all resources for Tank Wars Game. Set all Game Objects to their
     * initial state as well.
     */
    public void InitializeGame() {
        this.world = new BufferedImage(GameConstants.GAME_WORLD_WIDTH,
                GameConstants.GAME_WORLD_HEIGHT,
                BufferedImage.TYPE_INT_RGB);

        /*
        *   0 = nothing
        *   9 = unbreakables but not collidable
        *   3 = unbreakables
        *   2 = breakables
        *   4 = attack speed
        *   5 = speed
        *   6 = damage
        */

        InputStreamReader isr = new InputStreamReader(Objects.requireNonNull(ResourceManager.class.getClassLoader().getResourceAsStream("map/map.csv")));
        //this.anims.add(new Animation(100, 100, ResourceManager.getAnimation("bullethit")));
        //this.anims.add(new Animation(150, 100, ResourceManager.getAnimation("bulletshoot")));
        //this.anims.add(new Animation(200, 100, ResourceManager.getAnimation("powerpick")));
        //this.anims.add(new Animation(250, 100, ResourceManager.getAnimation("puffsmoke")));
        //this.anims.add(new Animation(300, 100, ResourceManager.getAnimation("rocketflame")));
        //this.anims.add(new Animation(350, 100, ResourceManager.getAnimation("rockethit")));

        try(BufferedReader mapReader = new BufferedReader(isr)){
            int row= 0;
            String[] gameItems;
            while(mapReader.ready()){
                gameItems = mapReader.readLine().strip().split(",");
                for(int col = 0; col < gameItems.length;col++){
                    String gameObject = gameItems[col];
                    if("0".equals(gameObject)) continue;
                    this.gobjs.add(GameObject.newInstance(gameObject, col*30, row*30));
                }
                row++;
            }
        }catch(IOException e){
            throw new RuntimeException(e);
        }

        t1 = new Tank(300, 300, 0, 0, (short) 0, ResourceManager.getSprite("tank1"));
        TankControl tc1 = new TankControl(t1, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE);
        this.lf.getJf().addKeyListener(tc1);
        this.gobjs.add(t1);

        t2 = new Tank(900, 300, 0, 0, (short) 180, ResourceManager.getSprite("tank2"));
        TankControl tc2 = new TankControl(t2, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_SHIFT);
        this.lf.getJf().addKeyListener(tc2);
        this.gobjs.add(t2);

        //refer back to Term Project Notes to implement 2nd player
    }

    private void drawFloor(Graphics2D buffer){
        BufferedImage floor = ResourceManager.getSprite("floor");
        for(int i = 0; i < GameConstants.GAME_WORLD_WIDTH; i+=514){
            for (int j = 0; j < GameConstants.GAME_WORLD_HEIGHT; j+=360){
                buffer.drawImage(floor, i, j, null);
            }
        }
    }

    private void renderMiniMap(Graphics2D g2, BufferedImage world){
        BufferedImage mm = world.getSubimage(0, 0, GameConstants.GAME_WORLD_WIDTH, GameConstants.GAME_WORLD_HEIGHT);
        g2.scale(.2,.2);
        g2.drawImage(mm, (GameConstants.GAME_SCREEN_WIDTH*5)/2 - (GameConstants.GAME_WORLD_WIDTH/2), (GameConstants.GAME_SCREEN_HEIGHT*5) - (GameConstants.GAME_WORLD_HEIGHT)-190, null);
    }

    private void renderSplitScreen(Graphics2D g2, BufferedImage world){
        BufferedImage lh = world.getSubimage((int)this.t1.getScreen_x(),(int)this.t1.getScreen_y(), GameConstants.GAME_SCREEN_WIDTH/2, GameConstants.GAME_SCREEN_HEIGHT);
        BufferedImage rh = world.getSubimage((int)this.t2.getScreen_x(), (int)this.t2.getScreen_y(), GameConstants.GAME_SCREEN_WIDTH/2, GameConstants.GAME_SCREEN_HEIGHT);
        g2.drawImage(lh,0,0,null);
        g2.drawImage(rh,GameConstants.GAME_SCREEN_WIDTH/2+4,0,null);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Graphics2D buffer = world.createGraphics();
        this.drawFloor(buffer);
        this.gobjs.forEach(gameObject -> gameObject.drawImage(buffer));
        this.t1.drawImage(buffer);
        this.t2.drawImage(buffer);
        this.anims.forEach(animation -> animation.drawImage(buffer));
        //g2.drawImage(world, 0, 0, null);
        renderSplitScreen(g2, world);
        renderMiniMap(g2,world);
    }

    public void addGameObject(GameObject obj){
        this.gobjs.add(obj);
    }
    public void removeGameObject(GameObject obj){
        this.gobjs.remove(obj);
    }
}
