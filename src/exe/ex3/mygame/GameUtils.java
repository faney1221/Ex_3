package exe.ex3.mygame;

import java.awt.*;
import java.awt.Color;

public class GameUtils {

        public static int getIntColor (Color c, int code) {
            if(c==null){return -1;}
            if(c.equals(Color.BLUE)){return 1;}

            if(c.equals(Color.RED)){return 2;}
            if(c.equals(Color.GREEN)){return 3;}
            if(c.equals(Color.YELLOW)){return 4;}
            if(c.equals(Color.CYAN)){return 5;}
            if(c.equals(Color.MAGENTA)){return 6;}
            if(c.equals(Color.PINK)){return 7;}
            if(c.equals(Color.GRAY)){return 8;}
            if(c.equals(Color.DARK_GRAY)){return 9;}
            return c.getRGB()&0xFF;
        }
    }


