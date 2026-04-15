package GameStates;

import UI.MenuButton;
import main.Game;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.sql.*;

public class Menu extends State implements Statemethods {

    private MenuButton[] buttons = new MenuButton[3];
    private BufferedImage backgroundImg;
    private int menuX, menuY, menuWidth, menuHeight;

    public Menu(Game game) {
        super(game);
        loadButtons();
        loadBackground();
    }

    private void loadBackground() {
        backgroundImg= LoadSave.GetSpriteAtlas(LoadSave.Menu_Background);
        menuWidth = (int) (backgroundImg.getWidth() * Game.Scale);
        menuHeight = (int) (backgroundImg.getHeight() * Game.Scale);
        menuX = Game.Game_Width/2 - 950;
        menuY = (int) (45 * Game.Scale);
    }

    private void loadButtons() {
        buttons[0]=new MenuButton(Game.Game_Width/2, (int) (150 * Game.Scale), 0, GameState.PLAYING);
        buttons[1]=new MenuButton(Game.Game_Width/2, (int) (220 * Game.Scale), 1, GameState.OPTIONS);
        buttons[2]=new MenuButton(Game.Game_Width/2, (int) (290 * Game.Scale), 2, GameState.QUIT);
    }

    @Override
    public void update() {
        for(MenuButton menuButton : buttons)
        {
            menuButton.update();
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImg, menuX, menuY, menuWidth, menuHeight, null);
        for(MenuButton menuButton : buttons)
            menuButton.draw(g);

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        for(MenuButton mb : buttons)
        {
            if(isIn(e, mb)) {
                mb.setMousePressed(true);
            }
            break;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for(MenuButton mb : buttons)
        {
            if(isIn(e, mb)){
                if(mb.isMousePressed()){
                    mb.applyGameState();
                }
                break;
            }
        }
        resetButtons();
    }

    private void resetButtons() {
        for(MenuButton mb : buttons) {
            mb.resetBools();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for(MenuButton mb : buttons)
        {
            mb.setMouseOver(false);
        }
        for(MenuButton mb : buttons)
        {
            if(isIn(e, mb))
            {
                mb.setMouseOver(true);
                break;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_K){
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                String sql = "SELECT * FROM SAVE_DATA ORDER BY SAVE_ID DESC LIMIT 1;";
                pstmt = game.c.prepareStatement(sql);
                rs = pstmt.executeQuery();

                if(rs.next()) {
                    game.getPlaying().getPlayer().setCurrentHealth(rs.getInt("HEALTH"));
                    System.out.println("S-a incarcat viata!");
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            } finally {
                try {
                    if (rs != null) rs.close();
                    if (pstmt != null) pstmt.close();
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
            GameState.state = GameState.PLAYING;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
