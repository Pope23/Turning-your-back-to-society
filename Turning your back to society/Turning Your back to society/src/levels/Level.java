package levels;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Entities.Smr;
import main.Game;

import static utils.HelpMethods.*;

public class Level {
    private BufferedImage img;
    private int[][] lvlData;
    private ArrayList<Smr> smrs;
    private int levelTilesWide;
    private int maxTilesOffset;
    private int maxLevelOffsetX;
    private int OffSet;

    public Level(BufferedImage img) {
        this.img=img;
        smrs = new ArrayList<>();
        createLevelData();
        createEnemies();
        calcLvlOffsets();
    }
    private void calcLvlOffsets() {
        for(int i = 0; i < 3; i++){
            OffSet += 700;
        }
        levelTilesWide = img.getWidth();
        maxTilesOffset = levelTilesWide * Game.Tiles_Size - OffSet;
        maxLevelOffsetX = maxTilesOffset;
    }

    private void createEnemies() {
        smrs = GetSmrs(img);
    }

    private void createLevelData() {
        lvlData = GetLevelData(img);
    }

    public int getSpriteIndex(int x, int y){
        return lvlData[y][x];
    }
    public int[][] getLevelData(){
        return lvlData;
    }
    public int getLvlOffset(){
        return maxTilesOffset;
    }
    public ArrayList<Smr> getSmrs(){
        return smrs;
    }
}
