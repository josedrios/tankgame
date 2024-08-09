package tankrotationexample.game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BreakableWall extends GameObject{
    float x,y;
    BufferedImage img;
    private Rectangle hitbox;
    public BreakableWall(float x, float y, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.img = img;
        this.hitbox = new Rectangle((int)x,(int)y,this.img.getWidth(), this.img.getHeight());
    }
    public Rectangle getHitbox(){
        return this.hitbox.getBounds();
    }

    @Override
    public void collides(GameObject with) {
        System.out.println("bwall col");
        if(with instanceof Bullet){
            System.out.println("bwall and bullet");
        }
    }

    public void drawImage(Graphics buffer) {
        buffer.setColor(Color.RED);
        buffer.drawRect((int)x,(int)y,this.img.getWidth(), this.img.getHeight());
        buffer.drawImage(this.img, (int)x, (int)y, null);
    }

    @Override
    public String toString() {
        return "BreakableWall{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
