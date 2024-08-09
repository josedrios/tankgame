package tankrotationexample.game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Speed extends GameObject{
    float x,y;
    BufferedImage img;
    private Rectangle hitbox;
    public Speed(float x, float y, BufferedImage img) {
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
        if(with instanceof Tank){
            System.out.println("Speed: Tank");
        } else if(with instanceof Bullet){
            System.out.println("Speed: Bullet");
        }
    }

    public void drawImage(Graphics buffer) {
        buffer.drawImage(this.img, (int)x, (int)y, null);
    }
}
