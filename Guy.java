import java.awt.*;
import java.awt.image.BufferedImage;

public class Guy extends Entity{

    float velX, velY;
    //hardcode sizes
    int sizeX = 70, sizeY = 160;

    BufferedImage skin = null;
    BufferedImage runningSkin1, jumpingSkin, runningSkin2 = null;

    boolean isJumping = false;
    boolean isInWater = false;

    public Guy(float x, float y, String name, Type type) {
        super(x, y, name, type);
        velX = 0;
        velY = 0;
        tmpHeight = y;
        //load skins
        skin = ImageUtilityClass.readImage("figure_1.png");
        runningSkin1 = ImageUtilityClass.readImage("figure_run.png");
        runningSkin2 = ImageUtilityClass.readImage("figure_run_2.png");
        jumpingSkin = ImageUtilityClass.readImage("figure_jumping.png");
    }

    float tmpHeight;

    public void jump(){
        if(!isJumping) {
            tmpHeight = y;
            velY = -0.1f/Main.mappedFPS;

            isJumping = true;
        }
    }

    @Override
    public void render(Graphics g) {

        if(x > Main.WIDTH*3-sizeX) {
            x = Main.WIDTH*3-sizeX;
        }
        if(y > Main.HEIGHT*3-sizeY-sizeY) {
            y = Main.HEIGHT*3-sizeY-sizeY;
        }
        if(x < 0){
            x = 0;
        }
        if(y < 0){
            y = 0;
        }
        if(isInWater) {
            x += velX/2;
            y += velY/2;
        } else {
            x += velX;
            y += velY;
        }
        if(isJumping) {
            velY += Main.gr;
        }

        if(isJumping && y >= tmpHeight) {
            isJumping = false;
            velY = 0;
        }

        //test if in water
        if(getTileUnderneath().type == Type.Water) {
            isInWater = true;
        } else {
            isInWater = false;
        }

        if(isJumping) {
            g.drawImage(jumpingSkin, (int)x, (int)y, sizeX, sizeY, null);
        }
        if(!isJumping) {
            if(isInWater) {
                g.drawImage(skin.getSubimage(0, 0, sizeX/2, sizeY/4), (int)x, (int)y, sizeX, sizeY/2, null);
            } else {
                if (velX == 0) {
                    g.drawImage(skin, (int) x, (int) y, sizeX, sizeY, null);
                } else if (velX > 0) {
                    g.drawImage(runningSkin1, (int) x, (int) y, sizeX, sizeY, null);
                } else if (velX < 0) {
                    g.drawImage(runningSkin2, (int) x, (int) y, sizeX, sizeY, null);
                }
            }
        }

        g.setColor(Color.white);
        g.drawString(name, (int)x, (int)y-30);
    }

    public Tile getTileUnderneath() {
        float xx = x+sizeX/2;
        float yy = y+sizeY/4*3;
        for(Tile t:Map.tiles) {
            if (xx >= t.x && xx <= t.x+Map.tileSizeX && yy >= t.y && yy <= t.y + Map.tileSizeY) {
                return t;

            }
        }

        //if Tile is not found for some reason, default it to Type.Water
        return new Tile((int)x, (int)y, Map.tileSizeX, Map.tileSizeY, Type.Water);
    }

    public void resetPos(){
        x = Main.START_X;
        y = Main.START_Y;
    }

    @Override
    public Rectangle getCollisionBox() {
        return new Rectangle((int)x, (int)y, sizeX, sizeY);
    }
}
