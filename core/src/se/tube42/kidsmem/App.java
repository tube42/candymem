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
		World.zoom = new Item(1);
		World.zoom.setImmediate(0, 1);

        bgc.setImmediate(0, 0);
        bgc.setImmediate(1, 0);
        bgc.setImmediate(2, 0);

        IOHelper.loadSettings();
        IOHelper.loadStatistics();
		AssetHelper.load();
		SceneHelper.reset();

        // this must come first:
        mgr.setBackground(new BackgroundScene());

		// initial resource loading
		SceneHelper.showMenu();
    }

	// use onPreDraw for the scene switch animation
	public void onPreDraw(SpriteBatch sb)
    {
		super.onPreDraw(sb);
		camera.zoom = World.zoom.get(0);
		camera.update();
	}


    public void onResize(int sw, int sh)
    {
		UI.resize(sw, sh);
		UI.scale =  (int)( 0.5f + Math.min( sw / CANVAS_W ,sh / CANVAS_H));
		if(UI.scale < 1)
			UI.scale = 1;
		else if(UI.scale > 2)
			UI.scale = 4;
    }

    public void onUpdate(float dt, long dtl)
    {
        ServiceProvider.service(dtl);
    }
}