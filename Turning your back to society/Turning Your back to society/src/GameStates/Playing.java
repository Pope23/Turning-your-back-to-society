package GameStates;

import Entities.EnemyManager;
import Entities.Player;
import UI.GameOverOverlay;
import UI.LevelCompletedOverlay;
import UI.PauseOverlay;
import levels.LevelManager;
import main.Game;
import utils.LoadSave;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.sql.*;

public class Playing extends State implements Statemethods {
    private Player player;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private PauseOverlay pauseOverlay;
    private GameOverOverlay gameOverOverlay;
    private LevelCompletedOverlay levelCompletedOverlay;
    private boolean paused;

    // Moving map (camera)
    private int xLevelOffset;
    private int leftBorder = (int) (0.2 * Game.Game_Width);
    private int rightBorder = (int) (0.8 * Game.Game_Width);
    private int maxLevelOffsetX;
    // bg
    private BufferedImage backgroundImg;
    private boolean gameOver;
    private boolean lvlCompleted;

    public Playing(Game game) {
        super(game);
        initClasses();
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.Playing_BG_img);
        calcLvlOffset();
        loadStartLevel();
    }
    public void loadNextLvl(){
        resetAll();
        levelManager.loadNextLevel();
    }

    private void loadStartLevel() {
        enemyManager.loadEnemies(levelManager.getCurrentLvl());
    }

    private void calcLvlOffset() {
        maxLevelOffsetX = levelManager.getCurrentLvl().getLvlOffset();
    }

    private void initClasses() {
        levelManager = new LevelManager(game);
        enemyManager = new EnemyManager(this);
        player = new Player(200, 200, (int) (143 * Game.Scale), (int) (198 * Game.Scale), this);
        player.loadLvlData(levelManager.getCurrentLvl().getLevelData());
        pauseOverlay = new PauseOverlay(this);
        gameOverOverlay = new GameOverOverlay(this);
        levelCompletedOverlay = new LevelCompletedOverlay(this);
    }

    @Override
    public void update() {
        if(paused){
            pauseOverlay.update();
        } else if(lvlCompleted){
            levelCompletedOverlay.update();
        } else if(!gameOver){
            levelManager.update();
            player.update();
            enemyManager.update(levelManager.getCurrentLvl().getLevelData(), player);
            checkCloseToBorder();
        }
    }

    private void checkCloseToBorder() {
        int playerX = (int) player.getHitbox().x;
        int diff = playerX - xLevelOffset;

        if (diff > leftBorder) {
            xLevelOffset += diff - leftBorder;
        } else if (diff < rightBorder) {
            xLevelOffset += diff - rightBorder;
        }
        if (xLevelOffset > maxLevelOffsetX) {
            xLevelOffset = maxLevelOffsetX;

        } else if (xLevelOffset < 0) {
            xLevelOffset = 0;
        }
    }


    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, Game.Game_Width, Game.Game_Height, null);
        levelManager.draw(g, xLevelOffset);
        player.render(g, xLevelOffset);
        enemyManager.draw(g, xLevelOffset);
        if(paused) {
            g.setColor(new Color(0,0,0,150));
            g.fillRect(0, 0, Game.Game_Width, Game.Game_Height);
            pauseOverlay.draw(g);
        }else if(gameOver) {
            gameOverOverlay.draw(g);
        }else if(lvlCompleted){
            levelCompletedOverlay.draw(g);
        }

    }

    public void resetAll(){
        gameOver = false;
        paused = false;
        lvlCompleted = false;
        player.resetAll();
        enemyManager.resetAllEnemies();
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        if(!gameOver) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                player.setAttacking(true);
            } else if (e.getButton() == MouseEvent.BUTTON3) {
                player.setParrying(true);
            }
        }
    }

    public void mouseDragged(MouseEvent e) {
        if(!gameOver)
            if (paused)
                pauseOverlay.mouseDragged(e);
    }


    @Override
    public void mousePressed(MouseEvent e) {
        if(!gameOver) {
            if (paused)
                pauseOverlay.mousePressed(e);
            else if(lvlCompleted)
                levelCompletedOverlay.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(!gameOver) {
            if (paused)
                pauseOverlay.mouseReleased(e);
            else if (lvlCompleted)
                levelCompletedOverlay.mouseReleased(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(!gameOver) {
            if (paused)
                pauseOverlay.mouseMoved(e);
            else if (lvlCompleted)
                levelCompletedOverlay.mouseMoved(e);
        }
    }
    public void setMaxLevelOffset(int lvlOffset){
        this.maxLevelOffsetX = lvlOffset;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(gameOver) {
            gameOverOverlay.keyPressed(e);
        }
        else {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                    player.setUp(true);
                    break;
                case KeyEvent.VK_A:
                    player.setWalkback(true);
                    break;
                case KeyEvent.VK_S:
                    player.setDown(true);
                    break;
                case KeyEvent.VK_D:
                    player.setRight(true);
                    break;
                case KeyEvent.VK_SPACE:
                    player.setJump(true);
                    break;
                case KeyEvent.VK_BACK_SPACE:
                    GameState.state = GameState.MENU;
                    break;
                case KeyEvent.VK_P:
                    paused = !paused;
                    break;
                case KeyEvent.VK_K:
                    PreparedStatement pstmnt = null;
                    Connection c = game.c;
                    try {
                        String sql = "INSERT INTO SAVE_DATA (SAVE_ID,HEALTH) " +
                                "VALUES (?, ?);";
                        pstmnt = c.prepareStatement(sql);

                        if(checkIfEmpty(c)==0) {
                            pstmnt.setInt(1,1);
                        }
                        else {
                            int idx = getIdx(c);
                            pstmnt.setInt(1, idx+1);
                        }
                        pstmnt.setInt(2,game.getPlaying().getPlayer().getCurrentHealth());
                        System.out.println("S-a salvat viata!");
                        pstmnt.executeUpdate();

                        c.commit();
                        //c.close();
                    } catch ( Exception E ) {
                        System.err.println( E.getClass().getName() + ": " + E.getMessage() );
                        System.exit(0);
                    }
                    finally {
                        if (pstmnt != null) {
                            try { pstmnt.close(); } catch (SQLException e2) { e2.printStackTrace(); }
                        }
                    }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(!gameOver) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                    player.setUp(false);
                    break;
                case KeyEvent.VK_A:
                    player.setWalkback(false);
                    break;
                case KeyEvent.VK_S:
                    player.setDown(false);
                    break;
                case KeyEvent.VK_D:
                    player.setRight(false);
                    break;
                case KeyEvent.VK_SPACE:
                    player.setJump(false);
                    break;
            }
        }
    }

    public void windowFocusLost() {
        player.resetDirBooleans();
    }

    public Player getPlayer() {
        return player;
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        enemyManager.checkEnemyHit(attackBox);
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
    public void unpauseGame() {
        paused = false;
    }
    public EnemyManager getEnemyManager(){
        return enemyManager;
    }

    public void setLevelCompleted(boolean lvlCompleted) {
        this.lvlCompleted = lvlCompleted;
    }
    private int checkIfEmpty(Connection c) {
        int rowCount=0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT COUNT(*) FROM SAVE_DATA";
            pstmt = c.prepareStatement(sql);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                rowCount = rs.getInt(1);
            }
        } catch(Exception except) {
            except.printStackTrace();
        }
        return rowCount;
    }

    private int getIdx(Connection c) {
        int idx=0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT SAVE_ID FROM SAVE_DATA";
            pstmt = c.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while(rs.next()) {
                idx = rs.getInt(1);
            }
        } catch ( Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return idx;
    }
}

