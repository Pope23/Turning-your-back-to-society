package inputs;

import GameStates.GameState;
import main.GamePannel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import static utils.Constant.Directions.*;

public class KeyBoardInputs implements KeyListener {

    private GamePannel gamepanel;
    public KeyBoardInputs(GamePannel gamepanel)
    {
        this.gamepanel= gamepanel;
    }

    @Override
    public void keyTyped(KeyEvent e){
    }
    @Override
    public void keyReleased(KeyEvent e){
        switch (GameState.state) {
            case MENU:
                gamepanel.getGame().getMenu().keyReleased(e);
                break;
            case PLAYING:
                gamepanel.getGame().getPlaying().keyReleased(e);
                break;
            default:
                break;
        }
    }
    @Override
    public void keyPressed(KeyEvent e){
        switch (GameState.state) {
            case MENU:
                gamepanel.getGame().getMenu().keyPressed(e);
                break;
            case PLAYING:
                gamepanel.getGame().getPlaying().keyPressed(e);
                break;
            default:
                break;
        }
    }
}
