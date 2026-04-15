package Entities;
import main.Game;
import utils.LoadSave;
import static utils.HelpMethods.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import static utils.Constant.PlayerConstants.*;
import static utils.Constant.PlayerConstants.IDLE;
import GameStates.Playing;

public class Player extends Entity{
    //Animations
    private BufferedImage[][] animations;
    private int aniTick, aniIndex, aniSpeed = 30;
    private int playeraction = PARRY;
    private boolean moving = false, attacking = false, parrying = false, walkback = false;
    private boolean left, up, right, down, jump;
    private float playerSpeed = 1.0f * Game.Scale;

    //Collision
    private int[][] lvlData;
    private float xDrawoffset = 12 * Game.Scale;
    private float yDrawoffset = 11 * Game.Scale;

    //Jumping animation + Gravity
    private float airSpeed = 0f;
    private float gravity = 0.03f * Game.Scale;
    private float jumpspeed = -2.5f * Game.Scale;
    private float fallSpeedAfterCollision = 0.5f * Game.Scale;
    private boolean inAir = false;
    //StatusBarUI
    private BufferedImage statusBarImg;

    private int statusBarWidth = (int)(192*Game.Scale);
    private int statusBarHeight = (int)(58*Game.Scale);
    private int statusBarX = (int) (10* Game.Scale);
    private int statusBarY = (int) (10* Game.Scale);

    private int healthBarWidth = (int) (150 * Game.Scale);
    private int healthBarHeight = (int) (4* Game.Scale);
    private int healthBarXStart = (int) (34 * Game.Scale);
    private int healthBarYStart = (int) (14* Game.Scale);

    private int maxHealth = 100;
    private int currentHealth = maxHealth;
    private int healthWidth = healthBarWidth;
    private int flipX = 0;
    private int flipW = 1;
    private boolean attackChecked;

    //AttackBox
    private Rectangle2D.Float attackBox;
    private Playing playing;
    public Player(float x, float y, int width, int height, Playing playing) {
        super(x, y, width, height);
        this.playing = playing;
        loadAnimations();
        initHitbox(x, y, 30* Game.Scale,  70* Game.Scale);
        initAttackBox();
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int)(20*Game.Scale), (int)(40*Game.Scale));
    }

    public void update(){
        updateHealthBar();
        if(currentHealth <= 0) {
            playing.setGameOver(true);
            return;
        }
        updateAttackBox();
        updatePos();
        if(attacking)
            checkAttack();
        updateAnimationTick();
        setAnimation();
    }

    private void checkAttack() {
        if(attackChecked || aniIndex != 1)
            return;
        attackChecked = true;
        playing.checkEnemyHit(attackBox);

    }

    private void updateAttackBox() {
        if(right){
            attackBox.x = hitbox.x + hitbox.width + (int)(Game.Scale * 10);
        }else if(walkback){
            attackBox.x = hitbox.x - hitbox.width - (int)(Game.Scale * 10);
        }
        attackBox.y = hitbox.y + (Game.Scale * 10);
    }

    private void updateHealthBar() {
        healthWidth = (int) ((currentHealth / (float)maxHealth) * healthBarWidth);
    }

    public void render(Graphics g, int LevelOffset){
        int renderX = (int) (hitbox.x - xDrawoffset) - LevelOffset;
        if (flipW == -1) {
            renderX += width;
        }
        g.drawImage(animations[playeraction][aniIndex], (int) (hitbox.x - xDrawoffset) - LevelOffset + flipX, (int) (hitbox.y - yDrawoffset), 143*flipW, 198, null);
        //drawhitbox(g);
        //drawAttackBox(g, LevelOffset);
        drawUI(g);
    }

    private void drawAttackBox(Graphics g, int lvlOffsetX) {
        g.setColor(Color.red);
        g.drawRect((int)attackBox.x - lvlOffsetX, (int)attackBox.y, (int)attackBox.width, (int)attackBox.height);
    }

    private void drawUI(Graphics g) {
        g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
        g.setColor(Color.red);
        g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);
    }

    private void updateAnimationTick() {
        aniTick++;
        if(aniTick >= aniSpeed){
            aniTick=0;
            aniIndex++;
            if(aniIndex >= GetSpriteAmount(playeraction)){
                aniIndex=0;
                attacking = false;
                parrying = false;
                attackChecked = false;
            }
        }
    }
    private void setAnimation() {

        int startAni = playeraction;
        if(moving)
        {
            playeraction=RUNNING;
        }
        else
        {
            playeraction=IDLE;
        }
        if(attacking){
            playeraction=ATTACK;
        }
        if(parrying){
            playeraction=PARRY;
        }
        if(startAni != playeraction)
        {
            resetAniTick();
        }
    }

    private void resetAniTick() {
        aniTick=0;
        aniIndex=0;
    }

    private void updatePos() {
        moving = false;


        if(jump){
            jump();
        }
        if(!inAir) {
            if ((!walkback && !right) || (right && walkback))
                return;
        }
        float xSpeed = 0;
        if (walkback) {
            xSpeed -= playerSpeed;
            flipX = 143;
            flipW = -1;
        }
        if (right) {
            xSpeed += playerSpeed;
            flipX = 0;
            flipW = 1;
        }
        if(!inAir)
            if(!IsEntityOnFloor(hitbox,lvlData))
                inAir=true;

        if(inAir){
            if(CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)){
                hitbox.y += airSpeed;
                airSpeed += gravity;
                updateXPos(xSpeed);
            }else {
                hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox);
                if (airSpeed > 0)
                    resetInAir();
                else
                    airSpeed = fallSpeedAfterCollision;
                updateXPos(xSpeed);
            }
        }else{
            updateXPos(xSpeed);
        }
        moving = true;

    }


    private void jump() {
        if(inAir)
            return;
        inAir = true;
        airSpeed = jumpspeed;

    }


    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    private void updateXPos(float xSpeed) {
        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
            hitbox.x += xSpeed;
        } else {
            hitbox.x = GetEntityXPosNextToWall(hitbox);//, xSpeed);
        }
    }

    public void changeHealth(int value){
        currentHealth += value;
        if(currentHealth <= 0){
            currentHealth = 0;
            //gameOver();
        }else if(currentHealth >= maxHealth){
            currentHealth = maxHealth;
        }
    }
    private void loadAnimations() {
            BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.Player_Atlas);
            animations = new BufferedImage[6][7];
            for(int j=0; j < animations.length; j++)
                for (int i = 0; i < animations[j].length; i++)
                    animations[j][i] = img.getSubimage(i * 143, j*197, 143, 197);

            statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);

    }
    public void loadLvlData(int[][] lvlData){
        this.lvlData=lvlData;
        if (!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
    }

    public void resetDirBooleans(){
        walkback=false;
        right=false;
        up=false;
        down=false;
    }

    public boolean isParrying() {
        return parrying;
    }

    public void setParrying(boolean parrying) {
        this.parrying = parrying;
    }

    public boolean isAttacking() {
        return attacking;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }
    public boolean isWalkback(){
        return walkback;
    }
    public void setWalkback(boolean walkback){
        this.walkback = walkback;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }
    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public void resetAll() {
        resetDirBooleans();
        inAir = false;
        attacking = false;
        moving = false;
        playeraction = PARRY;
        currentHealth = maxHealth;

        hitbox.x = x;
        hitbox.y = y;

        if (!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
    }
    public int getCurrentHealth()
    {
        return currentHealth;
    }

    public void setCurrentHealth(int currentHealth)
    {
        this.currentHealth =  currentHealth;
    }
}
