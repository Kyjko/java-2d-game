import java.awt.*;
import java.awt.image.BufferedImage;

public class HUD {

    public float x, y, width, height;

    static float HP = 1;
    static float MANA = 1;
    static float PROGRESS = 0.5f;

    BufferedImage profileImage = null;

    public HUD(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        profileImage = ImageUtilityClass.readImage("figure_1.png").getSubimage(0, 0, 35, 50);
    }

    public void render(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect((int)x, (int)y, (int)width-100, (int)height);
        g.setColor(Color.CYAN);
        g.drawRect((int)x, (int)y, (int)width-100, (int)height);
        g.drawImage(profileImage, (int)x, (int)y, (int)width/4, (int)height, null);

        //render hp, mana and progress bars
        g.setColor(Color.RED);
        g.fillRect((int)x+120, (int)y+25, (int)(width*2/5*HP), 20);
        g.setColor(Color.BLUE);
        g.fillRect((int)x+120, (int)y+50, (int)(width*2/5*MANA), 20);
        g.setColor(Color.GREEN);
        g.fillRect((int)x+120, (int)y+75, (int)(width*2/5*PROGRESS), 20);
    }
}
