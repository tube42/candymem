package se.tube42.kidsmem.control;

import se.tube42.lib.service.*;
import se.tube42.kidsmem.data.*;

public class IOHelper
{
    public static void loadSettings()
    {
        Settings.sound = StorageService.load("set_sound", true);
        Settings.fullscreen = StorageService.load("set_full", false);
        Settings.size = StorageService.load("set_size", 1);
    }

    public static void saveSettings()
    {
        StorageService.save("set_sound", Settings.sound);
        StorageService.save("set_full", Settings.fullscreen);
        StorageService.save("set_size", Settings.size);
        StorageService.flush();
    }

    public static void loadStatistics()
    {
        for(int i = 0; i < 3; i++) {
            Statistics.best[i] = StorageService.load("best" + i, -1);
            Statistics.count[i] = StorageService.load("count" + i, 0);
        }
    }

    public static void saveStatistics()
    {
        for(int i = 0; i < 3; i++) {
            StorageService.save("best" + i, Statistics.best[i] );
            StorageService.save("count" + i, Statistics.count[i] );
        }
        StorageService.flush();
    }
}
