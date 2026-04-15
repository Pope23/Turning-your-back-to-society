package utils;

import main.Game;

public class Constant {
    public static class EnemyConstants{
        public static final int SMR = 0;
        public static final int IDLE = 2;
        public static final int ATAC = 0;
        public static final int WALK = 1;
        public static final int HIT = 3;
        public static final int DEAD = 4;

        public static final int SMR_Width_Default = 143;
        public static final int SMR_Height_Default = 198;

        public static final int SMR_Width = (int)(SMR_Width_Default * Game.Scale);
        public static final int SMR_Height = (int)(SMR_Height_Default * Game.Scale);
        public static final int SMR_DrawOffsetX = (int)(25*Game.Scale);
        public static final int SMR_DrawOffsetY = (int)(55*Game.Scale);

        public static int getSpriteAmount(int enemytype, int enemy_state){
            switch (enemytype){
                case SMR:
                    switch (enemy_state){
                        case IDLE:
                            return 1;
                        case WALK:
                            return 5;
                        case DEAD:
                        case HIT:
                        case ATAC:
                            return 3;
                        default:
                            break;
                    }
            }
            return 0;
        }
        public static int GetMaxHealth(int enemy_type){
            switch (enemy_type){
                case SMR:
                    return 30;
                default:
                    return 1;
            }
        }

        public static int GetEnemyDmg(int enemy_type){
            switch (enemy_type){
                case SMR:
                    return 10;
                default:
                    return 0;
            }
        }
    }
    public static class UI{
        public static class Buttons{
            public static final int B_WIDTH_DEFAULT=140;
            public static final int B_HEIGHT_DEFAULT=56;
            public static final int B_WIDTH = (int) (B_WIDTH_DEFAULT* Game.Scale);
            public static final int B_HEIGHT = (int) (B_HEIGHT_DEFAULT* Game.Scale);
        }
        public static class PauseButtons {
            public static final int SOUND_SIZE_DEFAULT = 42;
            public static final int SOUND_SIZE = (int) (SOUND_SIZE_DEFAULT * Game.Scale);
        }

        public static class URMButtons {
            public static final int URM_DEFAULT_SIZE = 56;
            public static final int URM_SIZE = (int) (URM_DEFAULT_SIZE * Game.Scale);

        }

        public static class VolumeButtons {
            public static final int VOLUME_DEFAULT_WIDTH = 28;
            public static final int VOLUME_DEFAULT_HEIGHT = 44;
            public static final int SLIDER_DEFAULT_WIDTH = 215;

            public static final int VOLUME_WIDTH = (int) (VOLUME_DEFAULT_WIDTH * Game.Scale);
            public static final int VOLUME_HEIGHT = (int) (VOLUME_DEFAULT_HEIGHT * Game.Scale);
            public static final int SLIDER_WIDTH = (int) (SLIDER_DEFAULT_WIDTH * Game.Scale);
        }


    }
    public static class Directions{
        public static final int LEFT = 0;
        public static final int UP = 1;
        public static final int RIGHT = 2;
        public static final int DOWN = 3;
    }

    public static class PlayerConstants{
        public static final int PARRY = 0;
        public static final int RUNNING = 1;
        public static final int ATTACK = 2;
        public static final int IDLE = 3;
        public static final int DYING = 4;
        public static final int HURT = 5;


        public static int GetSpriteAmount(int player_action){
            switch(player_action)
            {
                case DYING:
                case RUNNING:
                    return 5;
                case HURT:
                case IDLE:
                    return 3;
                case ATTACK:
                    return 7;
                case PARRY:
                    return 6;
                default:
                    return 1;
            }
        }
    }
}
