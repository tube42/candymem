package se.tube42.kidsmem;

/*
 * This is an interface to OS dependent stuff.
 * The system puts an impletation into World.sys
 */

public interface SystemHandler
{
    public void showMessage(String msg);
    public void setFullscreen(boolean fs);
}
