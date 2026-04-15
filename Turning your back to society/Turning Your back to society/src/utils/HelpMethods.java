package utils;

import Entities.Smr;
import main.Game;

import java.awt.Point;
import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utils.Constant.EnemyConstants.SMR;

public class HelpMethods {


    public static boolean IsTileSolid(int xTile, int yTile, int[][] lvlData){
        int value = lvlData[yTile][xTile];
        if(value >= 72 || value < 0 || (value != 61 && value != 64))
            return true;
        return false;
    }
    private static boolean IsSolid(float x, float y, int[][] lvlData){
        int maxWidth = lvlData[0].length * Game.Tiles_Size;
        if(x < 0 || x >= maxWidth)
            return true;
        if(y < 0 || y >= Game.Game_Height)
            return true;

        float xIndex = x / Game.Tiles_Size;
        float yIndex = y / Game.Tiles_Size;

        return IsTileSolid((int) xIndex, (int) yIndex , lvlData);
    }
    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData) {
        if (!IsSolid(x, y, lvlData))
            if (!IsSolid(x + width, y + height, lvlData))
                if (!IsSolid(x + width, y, lvlData))
                    if (!IsSolid(x, y + height, lvlData))
                        return true;
        return false;
    }

    public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox)//, float xSpeed)
    {
//        int currentTile = (int) (hitbox.x / Game.Tiles_Size);
//        if (xSpeed > 0) {
//            // Right
//            int tileXPos = currentTile * Game.Tiles_Size;
//            int xOffset = (int) (Game.Tiles_Size - hitbox.width);
//            return tileXPos + xOffset - 1;
//        } else {
//            // Left
//            return currentTile * Game.Tiles_Size;
//        }
        return hitbox.x;
    }
    public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox)//, float airSpeed) {
    {
//        int currentTile = (int) (hitbox.y / Game.Tiles_Size);
//        if (airSpeed > 0) {
//            int tileYPos = currentTile * Game.Tiles_Size;
//            int yOffset = (int) (Game.Tiles_Size - hitbox.height);
//            return tileYPos + yOffset -1;
//        } else {
//            return currentTile * Game.Tiles_Size;
//        }
        return hitbox.y;
    }

    public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
        if (!IsSolid(hitbox.x, hitbox.y + hitbox.height +1, lvlData))
            if (!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height +1, lvlData))
                return false;
        return true;
    }
    public static boolean isFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData){
        if(xSpeed > 0)
            return IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData);
        else
            return IsSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
    }

    public static boolean isAllTilesWalkable(int xStart, int xEnd, int y, int[][] lvlData){
        for(int i = 0; i < xStart - xEnd; i++){
            if(IsTileSolid(xStart + i, y, lvlData)){
                return false;
            }
            if (!IsTileSolid(xStart + i, y + 1, lvlData))
                return false;

        }
        return true;
    }
    public static boolean IsSightClear(int[][] lvlData, Rectangle2D.Float hitbox, Rectangle2D.Float hitbox1, int tileY) {
        int firstXTile = (int)(hitbox.x / Game.Tiles_Size);
        int secondXTile = (int)(hitbox1.x / Game.Tiles_Size);
        if(firstXTile > secondXTile)
            return isAllTilesWalkable(secondXTile, firstXTile, tileY, lvlData);
        else
            return isAllTilesWalkable(firstXTile, secondXTile, tileY, lvlData);
    }
    public static int[][] GetLevelData(BufferedImage img){
        int[][] lvlData = new int[img.getHeight()][img.getWidth()];
        for(int j = 0; j < img.getHeight(); j++)
            for(int i = 0; i < img.getWidth(); i++)
            {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getRed();
                if(value >= 72){
                    value = 0;
                }
                lvlData[j][i] = value;
            }
        return lvlData;
    }
    public static ArrayList<Smr> GetSmrs(BufferedImage img){
        ArrayList<Smr> list = new ArrayList<>();
        for(int j = 0; j < img.getHeight(); j++)
            for(int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                if (value == SMR)
                    list.add(new Smr(i * Game.Tiles_Size, j * Game.Tiles_Size));
            }
        return list;
    }

}

