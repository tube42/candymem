package se.tube42.kidsmem.control;

import se.tube42.lib.service.*;
import se.tube42.kidsmem.data.*;

public class SettingsHelper
{
    public static void load()
    {
        Settings.sound = StorageService.load("set_sound", true);
        Settings.fullscreen = StorageService.load("set_full", false);
        Settings.size = StorageService.load("set_size", 1);

    }

    public static void save()
    {
        StorageService.save("set_sound", Settings.sound);
        StorageService.save("set_full", Settings.fullscreen);
        StorageService.save("set_size", Settings.size);
        StorageService.flush();
    }

}
