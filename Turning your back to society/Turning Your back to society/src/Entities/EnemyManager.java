package Entities;

import GameStates.Playing;
import levels.Level;
import utils.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utils.Constant.EnemyConstants.*;

public class EnemyManager {

    private Playing playing;
    private BufferedImage[][] smrArray;
    private ArrayList<Smr> Smrs = new ArrayList<>();
    public EnemyManager(Playing playing){
        this.playing = playing;
        loadEnemyImg();
    }

    public void loadEnemies(Level level) {
        Smrs = level.getSmrs();
        System.out.println("Loaded enemies: " + Smrs.size());
    }

    public void update(int[][] lvlData, Player player){
        boolean isEnemyActive = false;
        for(Smr c : Smrs)
            if(c.isActive()) {
                c.update(lvlData, player);
                isEnemyActive = true;
            }
        if(!isEnemyActive)
            playing.setLevelCompleted(true);
    }
    public void draw(Graphics g, int xLvlOffset){
        drawSmrs(g, xLvlOffset);
    }

    private void drawSmrs(Graphics g, int xLvlOffset) {
        for(Smr c : Smrs) {
            if(c.isActive()) {
                g.drawImage(smrArray[c.getEnemyState()][c.getAniIndex()],
                        (int)(c.getHitbox().x - xLvlOffset - 65 + c.flipX()),
                        (int) c.getHitbox().y, 143 * c.flipW(), 198, null);
            }
        }
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox){
        for(Smr c : Smrs)
            if(c.isActive())
                if(attackBox.intersects(c.getHitbox())){
                    c.hurt(10);
                    return;
                }
    }

    private void loadEnemyImg() {
        smrArray = new BufferedImage[5][5];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.ENEMYANI);
        for(int j = 0; j < smrArray.length; j++)
            for(int i = 0; i < smrArray[j].length; i++)
            {
                smrArray[j][i] = temp.getSubimage(i * SMR_Width_Default, j * SMR_Height_Default, SMR_Width_Default, SMR_Height_Default);
            }

    }

    public void resetAllEnemies() {
        for(Smr c : Smrs)
            c.resetEnemy();
    }
}
