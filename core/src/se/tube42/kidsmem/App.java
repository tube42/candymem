package se.tube42.kidsmem;

import com.badlogic.gdx.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.audio.*;

import se.tube42.lib.tweeny.*;
import se.tube42.lib.ks.*;
import se.tube42.lib.item.*;
import se.tube42.lib.scene.*;
import se.tube42.lib.util.*;


import se.tube42.kidsmem.data.*;
import se.tube42.kidsmem.view.*;
import se.tube42.kidsmem.control.*;

import static se.tube42.kidsmem.data.Constants.*;

public class App extends BaseApp
implements ApplicationListener, InputProcessor
{
    // additional layser
    private BackgroundScene scene_bg;

    public App()
    {

    }

    public void onCreate(SceneManager mgr, Item bgc)
    {
        ServiceProvider.init();

        // set size before loading assets
        onResize(UIC.sw, UIC.sh);

        World.mgr = mgr;
        World.bgc = bgc;
        bgc.setImmediate(0, 0);
        bgc.setImmediate(1, 0);
        bgc.setImmediate(2, 0);

        IOHelper.loadSettings();
        IOHelper.loadStatistics();
        AssetHelper.load();

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