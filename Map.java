import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class Map {

    static LinkedList<Tile>tiles;
    static BufferedImage[] tile_textures;
    Main m;
    int res;
    //default tile sizes incase something goes wrong with initializing dynamic values
    static float tileSizeX = 30, tileSizeY = 30;
    static BufferedImage terrainMap = null, tilesheet = null;

    public Map(Main m, int res) {
        this.m = m;
        tiles = new LinkedList<Tile>();
        this.res = res;

        //get dynamic widths and heights for tiles
        tileSizeX = Main.WIDTH/res;
        tileSizeY = Main.HEIGHT/res;

        terrainMap = ImageUtilityClass.readImage("terrain.png");

        //init Tiles on map, hardcoding map width and height
        for(int i = 0; i < 150; i++){
            for(int j = 0; j < 150; j++) {
                //read tiles from terrain.png
                int color = terrainMap.getRGB(i, j);
                String hexColor = ("#" + Integer.toHexString(color)).trim();



                if(hexColor.equals("#ffffff00")) {

                    //sand
                    tiles.add(new Tile(i*tileSizeX, j*tileSizeY, tileSizeX, tileSizeY, Type.Sand));
                }
                if(hexColor.equals("#ff00ff00")) {
                    //grass

                    tiles.add(new Tile(i*tileSizeX, j*tileSizeY, tileSizeX, tileSizeY, Type.Grass));
                }
                if(hexColor.equals("#ff0000ff")){
                    //water
                    tiles.add(new Tile(i*tileSizeX, j*tileSizeY, tileSizeX, tileSizeY, Type.Water));
                }
                if(hexColor.equals("#ff787878")) {
                    //concrete
                    tiles.add(new Tile(i*tileSizeX, j*tileSizeY, tileSizeX, tileSizeY, Type.Concrete));

                }
            }
        }

        tilesheet = ImageUtilityClass.readImage("tiles_sheet.png");

        tile_textures = new BufferedImage[4];

        for(int i = 0; i < tile_textures.length; i++) {
            tile_textures[i] = tilesheet.getSubimage(i*39, 0, 39, 22);
        }


    }

    public void render(Graphics g){

        for(Tile tile:tiles) {
            //determine if tile needs to be rendered
            if(tile.x+tileSizeX >= m.player.x-Main.WIDTH && tile.x <= m.player.x+Main.WIDTH && tile.y+tileSizeY >= m.player.y-Main.HEIGHT && tile.y <= m.player.y+Main.HEIGHT) {
                if (tile.type == Type.Grass) {
                    g.drawImage(tile_textures[1], (int)tile.x, (int)tile.y, (int)tile.sizeX, (int)tile.sizeY, null);
                }
                if (tile.type == Type.Water) {
                    g.drawImage(tile_textures[0], (int)tile.x, (int)tile.y, (int)tile.sizeX, (int)tile.sizeY, null);
                }
                if (tile.type == Type.Concrete) {
                    g.drawImage(tile_textures[3], (int)tile.x, (int)tile.y, (int)tile.sizeX, (int)tile.sizeY, null);
                }
                if (tile.type == Type.Sand) {
                    g.drawImage(tile_textures[2], (int)tile.x, (int)tile.y, (int)tile.sizeX, (int)tile.sizeY, null);
                }
            }

        }

    }
}
