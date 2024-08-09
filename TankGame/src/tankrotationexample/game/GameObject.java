package tankrotationexample.game;

import tankrotationexample.Resources.ResourceManager;

import java.awt.*;


public abstract class GameObject {

    public static GameObject newInstance(String type, float x, float y) throws UnsupportedOperationException {
        if(!"0".equals(type) && !"2".equals(type) && !"3".equals(type) && !"4".equals(type) && !"5".equals(type) && !"6".equals(type) && !"7".equals(type) && !"9".equals(type)){
            System.out.println("Error Value: "+type);
        }
        return switch(type){
            case "9","3" -> new Wall(x, y, ResourceManager.getSprite("unbreak"));
            case "2" -> new BreakableWall(x, y, ResourceManager.getSprite("break1"));
            case "4" -> new Ammo(x, y, ResourceManager.getSprite("ammo"));
            case "5" -> new Speed(x, y, ResourceManager.getSprite("speed"));
            case "6" -> new Raygun(x, y, ResourceManager.getSprite("raygun"));
            default -> throw new UnsupportedOperationException();
        };
    }

    public abstract void drawImage(Graphics g);
    public abstract Rectangle getHitbox();

    public abstract void collides(GameObject obj2);
}
