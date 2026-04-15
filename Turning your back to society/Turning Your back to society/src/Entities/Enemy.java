package Entities;
import main.Game;

import java.awt.geom.Rectangle2D;

import static utils.Constant.EnemyConstants.*;
import static utils.Constant.EnemyConstants.IDLE;
import static utils.Constant.PlayerConstants.*;
import static utils.Constant.PlayerConstants.PARRY;
import static utils.HelpMethods.*;
import static utils.Constant.Directions.*;

public class Enemy extends Entity{
    protected int aniIndex, enemyState = IDLE, enemyType;
    protected int aniTick, aniSpeed = 40;
    protected boolean firstUpdate = true, inAir = false;
    protected float fallSpeed, walkSpeed =0.5f * Game.Scale;
    protected int walkDir = LEFT;
    protected float gravity = 0.08f * Game.Scale;
    protected int tileY;
    protected float attackdistance = Game.Tiles_Size;
    protected int maxHealth;
    protected int currentHealth;
    protected boolean active = true;
    protected boolean attackChecked;
    public Enemy(float x, float y, int width, int height, int EnemyType) {
        super(x, y, width, height);
        this.enemyType = EnemyType;
        initHitbox(x, y, width, height);
        maxHealth = GetMaxHealth(enemyType);
        currentHealth = maxHealth;
    }

    protected void firstUpdateCheck(int[][] lvlData){
        if (!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
        firstUpdate=false;
    }
    protected void updateInAir(int[][] lvlData){
        if(CanMoveHere(hitbox.x, hitbox.y + fallSpeed, hitbox.width, hitbox.height, lvlData)){
            hitbox.y += fallSpeed;
            fallSpeed = gravity;
        }else{
            inAir = false;
            hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox);
            tileY = (int) (hitbox.y/Game.Tiles_Size);
        }
    }

    protected void move(int[][] lvlData){
        float xSpeed = 0;
        if(walkDir == LEFT)
            xSpeed = -walkSpeed;
        else
            xSpeed = +walkSpeed;
        if(CanMoveHere(hitbox.x +xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)){
            if(isFloor(hitbox, xSpeed, lvlData))
            {
                hitbox.x += xSpeed;
                return;
            }
        }
        changeWalkDir();
    }
    protected void turnTowardsPlayer(Player player){
        if(player.hitbox.x > hitbox.x)
            walkDir = RIGHT;
        else
            walkDir = LEFT;

    }
    protected boolean canSeePlayer(int[][] lvlData, Player player){
        int playerTileY = (int) (player.getHitbox().y / Game.Tiles_Size);
        if(playerTileY == tileY){
            if(isPlayerinRange(player)){
                if(IsSightClear(lvlData, hitbox, player.hitbox, tileY)){
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean isPlayerinRange(Player player) {
        int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);
        return absValue <= attackdistance * 5;
    }
    protected boolean isPlayerCloseForAttack(Player player){
        int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);
        return absValue <= attackdistance;
    }

    protected void newState(int enemyState){
        this.enemyState =enemyState;
        aniTick = 0;
        aniIndex = 0;
    }
    public void hurt(int amount){
        currentHealth -= amount;
        if(currentHealth <= 0)
            newState(DEAD);
        else
            newState(HIT);
    }
    protected void checkEnemyHit(Rectangle2D.Float attackBox, Player player) {
        if(attackBox.intersects(player.hitbox)){
            player.changeHealth(-GetEnemyDmg(enemyType));
        }
        attackChecked = true;
    }
    protected void updateAniTick(){
        aniTick++;
        if(aniTick >= aniSpeed){
            aniTick = 0;
            aniIndex++;
            if(aniIndex >= getSpriteAmount(enemyType, enemyState)){
                aniIndex = 0;
                switch (enemyState){
                    case ATAC,HIT -> enemyState = IDLE;
                    case DEAD -> active = false;
                }
            }
        }
    }



    protected void changeWalkDir() {
        if(walkDir == LEFT)
            walkDir = RIGHT;
        else
            walkDir = LEFT;
    }
    public void resetEnemy(){
        hitbox.x = x;
        hitbox.y = y;
        firstUpdate = true;
        currentHealth = maxHealth;
        newState(IDLE);
        active = true;
        fallSpeed = 0;
    }

    public int getAniIndex(){
        return aniIndex;
    }
    public int getEnemyState(){
        return enemyState;
    }
    public boolean isActive(){
        return active;
    }

}
