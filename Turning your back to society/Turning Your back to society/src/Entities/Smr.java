package Entities;


import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static java.awt.Color.*;
import static utils.Constant.Directions.*;
import static utils.Constant.EnemyConstants.*;
import static utils.HelpMethods.*;

public class Smr extends Enemy{
    private Rectangle2D.Float attackBox;
    private int attackBoxOffsetX;
    public Smr(float x, float y) {
        super(x, y, SMR_Width, SMR_Height , SMR);
        initHitbox(x, y, (int)30* Game.Scale, (int)80* Game.Scale);
        initAttackBox();
        this.active = true;
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int)(60*Game.Scale), (int)80*Game.Scale);
        attackBoxOffsetX = (int)(Game.Scale * 30);
    }

    public void update(int[][] lvlData, Player player){
        updateBehaviour(lvlData, player);
        updateAniTick();
        updateAttackBox();
    }

    private void updateAttackBox() {
        attackBox.x = hitbox.x - attackBoxOffsetX;
        attackBox.y = hitbox.y;
    }

    private void updateBehaviour(int[][] lvlData, Player player){
        if(firstUpdate)
            firstUpdateCheck(lvlData);

        if(inAir)
            updateInAir(lvlData);
        else {
            switch (enemyState){
                case IDLE:
                    newState(WALK);
                    break;
                case WALK:
                    if(canSeePlayer(lvlData, player)) {
                        turnTowardsPlayer(player);
                        if (isPlayerCloseForAttack(player))
                            newState(ATAC);
                    }
                    move(lvlData);
                    break;
                case ATAC:
                    if(aniIndex == 0)
                        attackChecked = false;
                    if(aniIndex==2 && !attackChecked)
                        checkEnemyHit(attackBox, player);
                    break;
                case HIT:
                    break;
            }
        }
    }

    public void drawAttackBox(Graphics g, int xLvlOffset){
        g.setColor(Color.red);
        g.drawRect((int)(attackBox.x - xLvlOffset), (int)attackBox.y, (int)attackBox.width, (int)attackBox.height);
    }
    public int flipX() {
        if(walkDir == RIGHT)
            return 143;
        else
            return 0;
    }
    public int flipW() {
        if(walkDir == RIGHT)
            return -1;
        else
            return 1;

    }

}
