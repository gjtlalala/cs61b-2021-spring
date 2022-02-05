package byow.Core;

//import javax.swing.text.Position;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.List;
import java.util.Set;

public class Room {
    private Position p;//left down
    private int width;
    private int height;
    //private Position door;

    public Room(Position p,int width,int height){
        this.p = p;
        this.width = width;
        this.height = height;
    }
    public Room(Position p){

    }
    public boolean detectOverlapRoom(List<Room> existingroom,Room room){
        return true;
    }
    public void drawRoom(TETile[][] t){
        for(int dy = 0; dy < height; dy++) {
            for(int dx = 0; dx < width; dx++) {
                t[p.x + dx][p.y + dy] = Tileset.FLOOR;
            }
        }

    }
}
