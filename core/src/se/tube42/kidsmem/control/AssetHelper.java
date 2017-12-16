package se.tube42.kidsmem.control;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;


import se.tube42.lib.service.*;
import se.tube42.kidsmem.data.*;
import se.tube42.kidsmem.view.*;

import static se.tube42.kidsmem.data.Assets.*;

public class AssetHelper
{

    public static void load()
    {
        String base = "1/";

        font1 = AssetService.createFonts("fonts/Roboto-Regular.ttf",
                  Constants.FONT_CHARSET, UI.scale * 18)[0];

        // patches
        np_shadow = AssetService.loadPatch(base + "shadow.9.png", 6, 5, 6, 5);

        // Textures
        tex_candy2 = AssetService.load(base + "candy2.png", true);
        tex_icons = AssetService.load(base + "icons.png", true);
        tex_candy1 = AssetService.load(base + "candy1.png", true);
        tex_tiles = AssetService.load(base + "tiles.png", true);

        // regions
        reg_candy2 = AssetService.divide(tex_candy2, 6, 2);
        reg_icons = AssetService.divide(tex_icons, 4, 2);
        reg_candy1 = AssetService.divide(tex_candy1, 5, 5);
        reg_tiles = AssetService.divide(tex_tiles, 2, 1);

        Texture tmp = AssetService.load(base + "rect.png", false);
        reg_rect = AssetService.divide(tmp, 1, 1);

        // particles
        pep_explode = AssetService.loadParticle(base, "explode");
        pep_firework = AssetService.loadParticle(base, "firework");
        pep_trail = AssetService.loadParticle(base, "trail");

        // audio
        sound_yes = ServiceProvider.loadSoundArray("yes");
        sound_no  = ServiceProvider.loadSoundArray("no");
        sound_hit  = ServiceProvider.loadSoundArray("hit");
        sound_start = ServiceProvider.loadSound("start");
        sound_end = ServiceProvider.loadSound("end");
        talk_on = ServiceProvider.loadSound("talk_on");
        talk_off = ServiceProvider.loadSound("talk_off");
        talk_hardness = ServiceProvider.loadSoundArray("talk_hardness");
    }
}
