package tankrotationexample.game;
import javax.sound.midi.SysexMessage;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.util.List;

public class Animation {
    float x,y;
    private List<BufferedImage> frames;
    private long timeSinceUpdate = 0;
    private long delay = 90;
    private int currentFrame = 0;
    private boolean isRunning = false;

    public Animation(float x, float y, List<BufferedImage> frames){
        this.x = x;
        this.y = y;
        this.frames = frames;
        isRunning = true;
    }

    public void update(){
        if(timeSinceUpdate + delay < System.currentTimeMillis()){
            this.timeSinceUpdate = System.currentTimeMillis();
            this.currentFrame++;
            //this.currentFrame = (currentFrame + 1) % this.frames.size();
            if(this.currentFrame == this.frames.size()){
                isRunning = false;
            }
        }
    }

    public void drawImage(Graphics2D g){
        if(isRunning){
            g.drawImage(this.frames.get(currentFrame), (int)x, (int)y, null);
        }
    }
}
