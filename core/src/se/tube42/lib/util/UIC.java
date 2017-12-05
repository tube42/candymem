package se.tube42.lib.util;

import com.badlogic.gdx.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.*;

/**
 * UI constants
 */
public class UIC
{
    // general stuff
    public static int sw, sh;
    public static int scale;

    // -----------------------------------------------------

    public static void resize(int w, int h)
    {
        if(UIC.sw == w && UIC.sh == h)
            return;

        UIC.sw = w;
        UIC.sh = h;
        UIC.scale = (int) Math.min(4, Math.max(1, 0.5f + h / 400f));


        System.out.println("resize " + w + "x" + h + " => scale=" + scale);
    }
}
