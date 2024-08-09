package tankrotationexample.game;

import org.w3c.dom.css.Rect;
import tankrotationexample.GameConstants;
import tankrotationexample.Resources.ResourcePool;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.nio.Buffer;

public class Bullet extends GameObject{
    private float x;
    private float y;
    private float vx;
    private float vy;
    private float angle;
    private float charge = 1f;

    private float R = 4;
    private Rectangle hitbox;

    private BufferedImage img;

    public Bullet(float x, float y, float angle, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.img = img;
        this.angle = angle;
        this.hitbox = new Rectangle((int)x,(int)y,this.img.getWidth(), this.img.getHeight());
    }

    public Rectangle getHitbox(){
        return this.hitbox.getBounds();
    }

    @Override
    public void collides(GameObject with) {
        if(with instanceof BreakableWall){
            System.out.println("bullet hit bwall");
        } else if(with instanceof Wall){
            System.out.println("bullet hit wall");
        }

    }

    void setX(float x){ this.x = x; }

    void setY(float y) { this. y = y;}

    public float getX(){
        return this.x;
    }

    public float getY(){
        return this.y;
    }

    public void increaseCharge(){
        this.charge = this.charge + 0.05f;
    }
    void update() {
        vx = Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = Math.round(R * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
        checkBorder();
        this.hitbox.setLocation((int)x,(int)y);
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
        rotation.scale(this.charge, this.charge);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, rotation, null);
        g2d.setColor(Color.RED);
    }

    public void setHeading(float x, float y, float angle){
        this.x = x;
        this.y = y;
        this.angle = angle;
    }
}
