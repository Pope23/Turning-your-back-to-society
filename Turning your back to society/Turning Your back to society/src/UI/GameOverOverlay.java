package UI;

import GameStates.GameState;
import GameStates.Playing;
import main.Game;

import java.awt.*;
import java.awt.event.KeyEvent;

public class GameOverOverlay {
    private Playing playing;
    public GameOverOverlay(Playing playing){
        this.playing = playing;
    }

    public void draw(Graphics g){
        g.setColor(new Color(0,0,0,200));
        g.fillRect(0,0, Game.Game_Width, Game.Game_Height);

        g.setColor(Color.white);
        g.drawString("Game Over", Game.Game_Width/2 - 700, 400);
        g.drawString("Press esc to enter Main Menu!", Game.Game_Width/2 - 700, 420);
    }
    public void keyPressed(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            playing.resetAll();
            GameState.state = GameState.MENU;
        }
    }
}
