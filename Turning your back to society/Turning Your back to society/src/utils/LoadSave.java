package utils;

import Entities.Smr;
import main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import static utils.Constant.EnemyConstants.SMR;


public class LoadSave {
    public static final String Player_Atlas = "THESPRITE.png";
    public static final String Level_Atlas = "Thetileset.png";
    public static final String Menu_Buttons = "button_atlas.png";
    public static final String Menu_Background = "menu_background.png";
    public static final String PAUSE_BACKGROUND = "pause_menu.png";
    public static final String SOUND_BUTTONS = "sound_button.png";
    public static final String URM_BUTTONS = "urm_buttons.png";
    public static final String VOLUME_BUTTONS = "volume_buttons.png";

    public static final String Playing_BG_img = "background0.png";
    public static final String ENEMYANI = "Inamic_Sheet.png";
    public static final String STATUS_BAR = "health_power_bar.png";
    public static final String COMPLETED_IMG = "completed_sprite.png";
    public static BufferedImage GetSpriteAtlas(String filename){
        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourceAsStream("/" + filename);
        if(is != null) {
            try {
                img = ImageIO.read(is);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return img;
    }
    public static BufferedImage[] GetAllLevels(){
        URL url = LoadSave.class.getResource("/mape");
        File file = null;

        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        File[] files = file.listFiles();
        File[] filesSorted = new File[files.length];

        for(int i = 0; i < filesSorted.length; i++)
            for(int j = 0; j < filesSorted.length; j++){
                if(files[j].getName().equals((i+1) + ".png")){
                    filesSorted[i] = files[j];
                }
            }

        BufferedImage[] imgs = new BufferedImage[filesSorted.length];

        for(int i = 0; i < imgs.length; i++) {
            try {
                imgs[i] = ImageIO.read(filesSorted[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return imgs;
    }
}

