
package se.tube42.kidsmem.data;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.audio.*;

import se.tube42.lib.tweeny.*;
import se.tube42.lib.scene.*;
import se.tube42.lib.item.*;

import se.tube42.kidsmem.*;
import se.tube42.kidsmem.view.*;

// World static data
public class World
{
    // Items
	public static Item bgc;
	public static Item zoom;

	// screen and board
	public static SceneManager mgr;
	public static MenuScene scene_menu;
	public static GameScene scene_game;

    // misc:
    public static SystemHandler sys = null;
}
