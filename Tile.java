public class Tile {
    float x, y;
    float sizeX, sizeY;
    Type type;

    public Tile(float x, float y, float sizeX, float sizeY, Type type){
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.x = x;
        this.y = y;
        this.type = type;
    }

}
