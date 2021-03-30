import java.awt.*;

public abstract class Entity {
    protected float x, y;
    protected String name;
    protected Type type;
    public Entity(float x, float y, String name, Type type){
        this.x = x;
        this.y = y;
        this.type = type;
        this.name = name;
    }

    public abstract void render(Graphics g);
    public abstract Rectangle getCollisionBox();
}
