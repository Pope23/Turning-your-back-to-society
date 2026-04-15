package main;

import inputs.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.StandardSocketOptions;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.*;

import static main.Game.Game_Height;
import static main.Game.Game_Width;
import static utils.Constant.PlayerConstants.*;
import static utils.Constant.Directions.*;

public class GamePannel extends JPanel {

    private MouseInputs mouseinputs;
    private Game game;
    public GamePannel(Game game){
        mouseinputs=new MouseInputs(this);
        this.game = game;
        setPannelSize();
        addKeyListener(new KeyBoardInputs(this));
        addMouseListener(mouseinputs);
        addMouseMotionListener(mouseinputs);
    }
    private void setPannelSize(){
        Dimension size = new Dimension(Game_Width, Game_Height);
        setPreferredSize(size);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        game.render(g);
        game.update();
    }
    public Game getGame(){
        return game;
    }
}
