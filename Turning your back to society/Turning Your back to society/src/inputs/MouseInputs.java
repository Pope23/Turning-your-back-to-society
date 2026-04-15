package inputs;

import GameStates.GameState;
import main.GamePannel;

import java.awt.event.*;

public class MouseInputs implements MouseListener, MouseMotionListener {

    private GamePannel gamepanel;
    public MouseInputs(GamePannel gamepanel){
        this.gamepanel = gamepanel;
    }
    @Override
    public void mouseDragged(MouseEvent e){
        switch (GameState.state) {
            case PLAYING:
                gamepanel.getGame().getPlaying().mouseDragged(e);
                break;
            default:
                break;

        }

    }
    @Override
    public void mouseMoved(MouseEvent e){
        switch (GameState.state){
            case MENU:
                gamepanel.getGame().getMenu().mouseMoved(e);
            case PLAYING:
                gamepanel.getGame().getPlaying().mouseMoved(e);
            default:
                break;
        }
    }
    @Override
    public void mouseClicked(MouseEvent e){
        switch (GameState.state){
            case PLAYING:
                gamepanel.getGame().getPlaying().mouseClicked(e);
            default:
                break;
        }
    }
    @Override
    public void mousePressed(MouseEvent e){
        switch (GameState.state){
            case MENU:
                gamepanel.getGame().getMenu().mousePressed(e);
            case PLAYING:
                gamepanel.getGame().getPlaying().mousePressed(e);
            default:
                break;
        }
    }
    @Override
    public void mouseReleased(MouseEvent e){
        switch (GameState.state){
            case MENU:
                gamepanel.getGame().getMenu().mouseReleased(e);
            case PLAYING:
                gamepanel.getGame().getPlaying().mouseReleased(e);
            default:
                break;
        }
    }
    @Override
    public void mouseEntered(MouseEvent e){

    }
    @Override
    public void mouseExited(MouseEvent e){

    }


}
