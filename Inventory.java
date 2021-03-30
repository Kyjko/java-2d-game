import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

//TODO: finish class description and stuff

public class Inventory {

    float x, y;
    int width, height;
    boolean isVisible = false;
    boolean isMouseIn = false;
    int resX = 10;
    int resY = 20;
    float alpha = .7f;

    BufferedImage[] items_images = new BufferedImage[3];

    LinkedList<Item>items = new LinkedList<Item>();

    public Inventory(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        //add textures of items
        for(int i = 0; i < items_images.length; i++){
            items_images[i] = ImageUtilityClass.readImage("items_sheet.png").getSubimage(i*50, 0, 50, 50);
        }

        //add test items
        items.add(new Item(Type.Junk));
        items.add(new Item(Type.Gold));
        items.add(new Item(Type.Loot));
    }

    public void render(Graphics g) {
        alpha = isMouseIn ? .9f : .7f;
        if(isVisible){
            g.setColor(new Color(0, 0, 0, alpha));
            g.fillRect((int)x, (int)y, width, height);
            g.setColor(Color.DARK_GRAY);
            g.drawRect((int)x, (int)y, width, height);

            //render items in inventory
            for(int i =0 ; i < items.size(); i++){
                float xx = x + (i*width/resX)%width;
                float yy = y + (int)((i*width/resX)/width)*resY;

                Item item = items.get(i);
                if(item.type == Type.Junk){
                    g.drawImage(items_images[1], (int)xx, (int)yy, width/resX, height/resY, null);
                } else if(item.type == Type.Gold){
                    g.drawImage(items_images[0], (int)xx, (int)yy, width/resX, height/resY, null);
                } else if(item.type == Type.Loot){
                    g.drawImage(items_images[2], (int)xx, (int)yy, width/resX, height/resY, null);
                }


            }
        }
    }

}
