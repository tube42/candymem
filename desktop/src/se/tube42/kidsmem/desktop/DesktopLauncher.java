package se.tube42.kidsmem.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import se.tube42.kidsmem.*;
import se.tube42.kidsmem.data.*;

/*
public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new App(), config);
	}
}
*/

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


public class DesktopLauncher
{
    public static void main(String[] args )
    {
        World.sys = new DesktopHandler();
        App app = new App();
        new LwjglApplication( app, "Candy Memory", 320, 480);
    }
}
