package levels;

import GameStates.GameState;
import main.Game;
import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class LevelManager {

    private Game game;
    private BufferedImage[] levelSprite;
    private ArrayList<Level> levels;
    private int lvlIndex = 0;

    public LevelManager (Game game){
        this.game = game;
        importOutsideSprites();
        levels = new ArrayList<>();
        buildAllLevels();
    }
    public void loadNextLevel() {
        lvlIndex++;
        if (lvlIndex >= levels.size()) {
            lvlIndex = 0;
            System.out.println("No more levels! Game completed!");
            GameState.state = GameState.MENU;
        }
        Level newLevel = levels.get(lvlIndex);
        game.getPlaying().getEnemyManager().loadEnemies(newLevel);
        game.getPlaying().getPlayer().loadLvlData(newLevel.getLevelData());
        game.getPlaying().setMaxLevelOffset(newLevel.getLvlOffset());
    }

    private void buildAllLevels() {
        BufferedImage[] allLevels = LoadSave.GetAllLevels();
        for(BufferedImage img : allLevels)
            levels.add(new Level(img));
    }

    private void importOutsideSprites() {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.Level_Atlas);
        levelSprite = new BufferedImage[72];
        for(int j = 0; j < 12;j++)
            for(int i = 0; i< 6; ++i)
            {
                int index = j*6 + i;
                levelSprite[index]= img.getSubimage(i*17, j*17, 16, 16);
            }
    }

    public void draw(Graphics g, int LevelOffset){
        for(int j=0; j< Game.Tiles_In_Height; j++)
            for(int i=0; i<levels.get(lvlIndex).getLevelData()[0].length; i++){
                int index = levels.get(lvlIndex).getSpriteIndex(i, j);
                g.drawImage(levelSprite[index], i * Game.Tiles_Size - LevelOffset, j * Game.Tiles_Size, Game.Tiles_Size, Game.Tiles_Size, null);
            }
    }

    public void update(){

    }
    public Level getCurrentLvl(){
        return levels.get(lvlIndex);
    }
    public int getAmountOfLevels(){
        return levels.size();
    }


}
