package tankrotationexample.Resources;

import tankrotationexample.game.Bullet;
import tankrotationexample.game.Sound;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

public class ResourceManager {
    private final static Map<String, BufferedImage> sprites = new HashMap();
    private final static Map<String, Sound> sound = new HashMap<>();
    private final static Map<String, List<BufferedImage>> animations = new HashMap<>();
    private static final Map<String, Integer> animationInfo = new HashMap<>(){{
        put("bullethit", 4);
        put("bulletshoot", 4);
        put("powerpick", 4);
        put("puffsmoke", 4);
        put("rocketflame", 4);
        put("rockethit", 4);
    }};
    private final static Map<String, Sound> sounds = new HashMap<>();

    private static BufferedImage loadSprite(String path) throws IOException{
        return ImageIO.read(
                Objects.requireNonNull(ResourceManager
                        .class
                        .getClassLoader()
                        .getResource(path)));
    }

    private static void initAnimation() {
        String baseName = "animations/%s/%s_%04d.png";
        animationInfo.forEach((animationName, frameCount) -> {
            List <BufferedImage> frames = new ArrayList<>();
            try{
                for (int i = 0; i < frameCount; i++) {
                    String spritePath = baseName.formatted(animationName, animationName, i);
                    frames.add(loadSprite(spritePath));
                }
                ResourceManager.animations.put(animationName, frames);
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        });
    }
    public static List<BufferedImage> getAnimation(String type){
        if(!ResourceManager.animations.containsKey(type)){
            throw new RuntimeException("%s is missing from animations resources".formatted(type));
        }
        return ResourceManager.animations.get(type);
    }

    public static Sound getSound(String type){
        if(!ResourceManager.sounds.containsKey(type)){
            throw new RuntimeException("%s is missing from animations resources".formatted(type));
        }
        return ResourceManager.sounds.get(type);
    }

    public static void initSprites(){
        try{
            ResourceManager.sprites.put("tank1", loadSprite("tank/tank1.png"));
            ResourceManager.sprites.put("tank2", loadSprite("tank/tank2.png"));
            ResourceManager.sprites.put("bullet", loadSprite("bullet/bullet.png"));
            ResourceManager.sprites.put("bullet2", loadSprite("bullet/bullet2.png"));
            ResourceManager.sprites.put("ammo", loadSprite("powerups/ammo.png"));
            ResourceManager.sprites.put("raygun", loadSprite("powerups/raygun.png"));
            ResourceManager.sprites.put("speed", loadSprite("powerups/speed.png"));
            ResourceManager.sprites.put("unbreak", loadSprite("wall/unbreak.jpg"));
            ResourceManager.sprites.put("break1", loadSprite("wall/break1.png"));
            ResourceManager.sprites.put("break2", loadSprite("wall/break2.png"));
            ResourceManager.sprites.put("floor", loadSprite("floor/floor.jpg"));
            ResourceManager.sprites.put("menu", loadSprite("menu/title.png"));
        }catch(IOException e){
            throw new RuntimeException();
        }
    }
    public static void initSounds(){

    }
    private static Sound loadSound(String path) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream ais = AudioSystem.getAudioInputStream(
                Objects.requireNonNull(ResourceManager.class.getClassLoader().getResource(path))
        );
        Clip c = AudioSystem.getClip();
        c.open(ais);
        Sound s = new Sound(c);
        s.setVolume(1f);
        return s;
    }
    public static void loadResources(){
        ResourceManager.initSprites();
        ResourceManager.initAnimation();
    }

    public static BufferedImage getSprite(String type) {
        if(!ResourceManager.sprites.containsKey(type)){
            throw new RuntimeException("%s is missing from sprite resources".formatted(type));
        }
        return ResourceManager.sprites.get(type);
    }

    public static void main(String[] args){

        ResourceManager.loadResources();
        System.out.println();
    }
}
