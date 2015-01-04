package se.tube42.kidsmem;

import com.badlogic.gdx.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.audio.*;

import se.tube42.kidsmem.data.*;
import se.tube42.kidsmem.service.*;
import se.tube42.kidsmem.scene.*;
import se.tube42.kidsmem.logic.*;

import se.tube42.lib.tweeny.*;
import se.tube42.lib.ks.*;
import se.tube42.lib.scene.*;
import se.tube42.lib.util.*;


import static se.tube42.kidsmem.data.Constants.*;

public class KidsMemoryApp extends BaseApp
implements ApplicationListener, InputProcessor
{

    // additional layser
    private BackgroundScene scene_bg;

    public KidsMemoryApp()
    {
        super(320, 480);
    }

    public void onCreate(SceneManager mgr, Item bgc)
    {
        World.mgr = mgr;
        World.color_bg = bgc;

        SettingsHelper.load();
        AssetHelper.load();

        World.color_bg = bgc;
        World.color_bg.set(0, 0);
        World.color_bg.set(1, 0);
        World.color_bg.set(2, 0);

        for(int i = 0; i < 3; i++)	bgc.set(i, 0, 1);

        // this must come first:
        scene_bg = new BackgroundScene();
        mgr.setBackground(scene_bg);

        // initial resource loading
        Scene sm = new MenuScene(mgr);
        mgr.setScene(sm);
    }

    public void onResize(int sw, int sh)
    {
        SizeHelper.resize(sw, sh);
    }

    public void onUpdate(float dt, long dtl)
    {
        ServiceProvider.service(dtl);
    }
}
