
package se.tube42.kidsmem.desktop;

import com.badlogic.gdx.*;
import com.badlogic.gdx.backends.lwjgl.*;

import se.tube42.kidsmem.*;
import se.tube42.kidsmem.data.*;


class DesktopHandler implements SystemHandler
{
    public static SystemHandler instance = null;

    public void showMessage(String msg)
    {
    	System.out.println("MESSAGE " + msg);
    }
    public void setFullscreen(boolean fs)
    {
    	System.out.println("fullscreen = " + fs);
    }
}


public class DesktopMain
{
    public static void main(String[] args )
    {
    	World.sys = new DesktopHandler();

        KidsMemoryApp app = new KidsMemoryApp();
        new LwjglApplication( app, "KidsMemory", 320, 480);
    }
}
