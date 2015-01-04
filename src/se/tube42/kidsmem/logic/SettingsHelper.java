package se.tube42.kidsmem.logic;

import se.tube42.kidsmem.data.*;
import se.tube42.kidsmem.service.*;

public class SettingsHelper
{

    public static void load()
    {
        Settings.sound = ServiceProvider.load("set_sound", true);
        Settings.fullscreen = ServiceProvider.load("set_full", false);
        Settings.size = ServiceProvider.load("set_size", 1);

    }

    public static void save()
    {
        ServiceProvider.save("set_sound", Settings.sound);
        ServiceProvider.save("set_full", Settings.fullscreen);
        ServiceProvider.save("set_size", Settings.size);
        ServiceProvider.flushStorage();
    }

}
