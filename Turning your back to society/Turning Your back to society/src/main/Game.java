package main;

import GameStates.GameState;
import GameStates.Playing;
import GameStates.Menu;
import utils.LoadSave;

import java.awt.Graphics;
import java.sql.*;

public class Game implements Runnable{

    private GameWindow gamewindow;
    private GamePannel gamepannel;
    private Thread gamethread;
    private final int fps_set = 120;
    private final int ups_set = 200;

    private Playing playing;
    private Menu menu;

    public final static int Tiles_Default_Size=32;
    public final static float Scale = 2f;
    public final static int Tiles_In_Width= 50;
    public final static int Tiles_In_Height= 14;
    public final static int Tiles_Size = (int)(Tiles_Default_Size*Scale);
    public final static int Game_Width= Tiles_Size * Tiles_In_Width;
    public final static int Game_Height= Tiles_Size * Tiles_In_Height;
    public Connection c;



    public Game(Connection c){
        initClasses();
        this.c = c ;
        gamepannel=new GamePannel(this);
        gamewindow=new GameWindow(gamepannel);
        gamepannel.setFocusable(true);
        gamepannel.requestFocus();
        startGameLoop();
    }

    private void initClasses() {
        menu = new Menu(this);
        playing = new Playing(this);
    }

    private void startGameLoop(){
        gamethread= new Thread(this);
        gamethread.start();
    }

    public void update(){
        switch (GameState.state){
            case MENU:
                menu.update();
                break;
            case PLAYING:
                playing.update();
                break;
            case OPTIONS:
            case QUIT:
                System.exit(0);
            default:
                break;
        }
    }
    public void render(Graphics g){

        switch (GameState.state) {
            case MENU:
                menu.draw(g);
                break;
            case PLAYING:
                playing.draw(g);
                break;
            default:
                break;
        }
    }

    @Override
    public void run(){

        double timePerFrame = 1000000000.0/ fps_set;
        double timePerUpdate = 1000000000.0/ ups_set;

        long previoustime = System.nanoTime();

        int frames = 0;
        int updates = 0;
        long lastCheck = System.currentTimeMillis();

        double deltaU = 0;
        double deltaF = 0;

        while(true){
            long currentTime = System.nanoTime();

            deltaU += (currentTime-previoustime) / timePerUpdate;
            deltaF += (currentTime-previoustime) / timePerFrame;
            previoustime = currentTime;

            if(deltaU >= 1)
            {
                updates++;
                deltaU--;
            }

            if(deltaF >= 1){
                gamepannel.repaint();
                frames++;
                deltaF--;
            }
            if(System.currentTimeMillis() - lastCheck >=1000){
                lastCheck=System.currentTimeMillis();
                System.out.println("FPS " + frames + " | UPS: " + updates);
                frames = 0;
                updates = 0;
            }
        }
    }
    public void windowFocusLost(){
        if(GameState.state == GameState.PLAYING){
            playing.getPlayer().resetDirBooleans();
        }
    }
    public Menu getMenu(){
        return menu;
    }
    public Playing getPlaying(){
        return playing;
    }
}
