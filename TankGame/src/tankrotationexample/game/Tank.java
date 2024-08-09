package tankrotationexample.game;

import com.sun.management.GarbageCollectorMXBean;
import tankrotationexample.GameConstants;
import tankrotationexample.Launcher;
import tankrotationexample.Resources.ResourceManager;
import tankrotationexample.Resources.ResourcePool;
import tankrotationexample.game.GameWorld;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author anthony-pc
 */
public class Tank extends GameObject{
    private float staticX;
    private float staticY;
    private float x;
    private float y;
    private float screen_x, screen_y;
    private float vx;
    private float vy;
    private float angle;
    private float R = 3;
    private float ROTATIONSPEED = 2.0f;

    private BufferedImage img;
    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;
    private boolean shootPressed;
    private Rectangle hitbox;
    List <Bullet> ammo = new ArrayList<>();
    long timeSinceLastShot = 0L;
    long cooldown = 2000;
    Bullet currentChargeBullet = null;

    Tank(float x, float y, float vx, float vy, float angle, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.img = img;
        this.angle = angle;
        this.hitbox = new Rectangle((int)x,(int)y,this.img.getWidth(), this.img.getHeight());
        centerScreen();
    }


    public Rectangle getHitbox(){
        return this.hitbox.getBounds();
    }

    void setX(float x){ this.x = x; }

    void setY(float y) { this. y = y;}

    public float getX(){
        return this.x;
    }

    public float getY(){
        return this.y;
    }

    public float getScreen_x(){
        return screen_x;
    }

    public float getScreen_y(){
        return screen_y;
    }

    void toggleUpPressed() {
        this.UpPressed = true;
    }

    void toggleDownPressed() {
        this.DownPressed = true;
    }

    void toggleRightPressed() {
        this.RightPressed = true;
    }

    void toggleLeftPressed() {
        this.LeftPressed = true;
    }

    void unToggleUpPressed() {
        this.UpPressed = false;
    }

    void unToggleDownPressed() {
        this.DownPressed = false;
    }

    void unToggleRightPressed() {
        this.RightPressed = false;
    }

    void unToggleLeftPressed() {
        this.LeftPressed = false;
    }

    void update(GameWorld gameWorld) {
        staticX = x;
        staticY = y;
        if (this.UpPressed) {
            this.moveForwards();
        }

        if (this.DownPressed) {
            this.moveBackwards();
        }

        if (this.LeftPressed) {
            this.rotateLeft();
        }

        if (this.RightPressed) {
            this.rotateRight();
        }
        if (this.shootPressed && ((this.timeSinceLastShot + this.cooldown) < System.currentTimeMillis())){
            this.timeSinceLastShot = System.currentTimeMillis();
            var b = new Bullet(x,y, angle, ResourceManager.getSprite("bullet"));
            this.ammo.add(b);
            gameWorld.addGameObject(b);
            gameWorld.anims.add(new Animation(x, y, ResourceManager.getAnimation("bulletshoot")));
        }
            this.ammo.forEach(bullet -> bullet.update());
            centerScreen();
            this.hitbox.setLocation((int) x, (int) y);
    }

    private void rotateLeft() {
        this.angle -= this.ROTATIONSPEED;
    }

    private void rotateRight() {
        this.angle += this.ROTATIONSPEED;
    }

    private void moveBackwards() {
        vx =  Math.round(R * Math.cos(Math.toRadians(angle)));
        vy =  Math.round(R * Math.sin(Math.toRadians(angle)));
        x -= vx;
        y -= vy;
       checkBorder();
       centerScreen();
    }

    private void moveForwards() {
        vx = Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = Math.round(R * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
        checkBorder();
        centerScreen();
    }

    private void centerScreen(){
        this.screen_x = this.x - GameConstants.GAME_WORLD_WIDTH/4f;
        this.screen_y = this.y - GameConstants.GAME_WORLD_HEIGHT/2f;

        if(this.screen_x < 0){
            this.screen_x = 0;
        }

        if(this.screen_y < 0){
            this.screen_y = 0;
        }

        if(this.screen_x > GameConstants.GAME_WORLD_WIDTH - GameConstants.GAME_SCREEN_WIDTH/2f){
            this.screen_x = GameConstants.GAME_WORLD_WIDTH - GameConstants.GAME_SCREEN_WIDTH/2f;
        }

        if(this.screen_y > GameConstants.GAME_WORLD_HEIGHT - GameConstants.GAME_SCREEN_HEIGHT/2f){
            this.screen_y = GameConstants.GAME_SCREEN_HEIGHT - GameConstants.GAME_SCREEN_HEIGHT/2f;
        }
    }

    private void checkBorder() {
        if (x < 30) {
            x = 30;
        }
        if (x >= GameConstants.GAME_WORLD_WIDTH - 88) {
            x = GameConstants.GAME_WORLD_WIDTH - 88;
        }
        if (y < 40) {
            y = 40;
        }
        if (y >= GameConstants.GAME_WORLD_HEIGHT - 80) {
            y = GameConstants.GAME_WORLD_HEIGHT - 80;
        }
    }

    @Override
    public String toString() {
        return "x=" + x + ", y=" + y + ", angle=" + angle;
    }


    public void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, rotation, null);
        g2d.setColor(Color.RED);
        //g2d.rotate(Math.toRadians(angle), bounds.x + bounds.width/2, bounds.y + bounds.height/2);
        //g2d.drawRect((int)x,(int)y,this.img.getWidth(), this.img.getHeight());
        this.ammo.forEach(b -> b.drawImage(g2d));


        g2d.setColor(Color.green);
        g2d.drawRect((int)x, (int)y-20, 100, 15);
        long currentWidth = 100 - ((this.timeSinceLastShot + this.cooldown) - System.currentTimeMillis())/20;
        if(currentWidth > 100){
            currentWidth = 100;
        }
        g2d.fillRect((int)x, (int)y-20, (int)currentWidth, 15);
    }
    public void collides(GameObject with){
        if(with instanceof Bullet){
            System.out.println("Tank: Bullet");
        } else if(with instanceof Wall){
            if (UpPressed || DownPressed) {
                x = staticX;
                y = staticY;
            }
        } else if (with instanceof BreakableWall) {
            if (UpPressed || DownPressed) {
                x = staticX;
                y = staticY;
            }
        } else if(with instanceof Speed){
            System.out.println("Tank: Speed");
            R=9;
            //((PowerUp)with).applyPower(this);
        }
    }

    public void toggleShootPressed() {
        this.shootPressed=true;
    }

    public void untoggleShootPressed() {
        this.shootPressed=false;
    }
}
