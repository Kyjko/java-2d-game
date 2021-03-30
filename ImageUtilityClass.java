import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageUtilityClass {

    public static BufferedImage readImage(String filename){

        BufferedImage res = null;
        try {
            res = ImageIO.read(new File("res/" + filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
}
